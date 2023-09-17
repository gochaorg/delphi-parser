package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Объявление типа
 */
public sealed interface TypeDecl
    permits TypeDecl.Array, TypeDecl.Interface, TypeDecl.MetaClass, TypeDecl.NewTypeId, TypeDecl.Simple, TypeDecl.StringType, TypeDecl.Structured, TypeDecl.TypeAlias, TypeDecl.Variant, TypeIdent
{
    static TypeDecl of(DelphiParser.TypeDeclContext ctx) {
        if( ctx.strucType()!=null
        &&  !ctx.strucType().isEmpty()
        ) return Structured.of(ctx.strucType());

        if( ctx.typeId()!=null && !ctx.typeId().isEmpty() ){
            boolean typeFlag = ctx.TYPE()!=null
                && ctx.TYPE().getText()!=null
                && ctx.TYPE().getText().length()>0;

            ImList<String,?> namespace = ImListLinked.of();
            ImList<String,?> ident = ImListLinked.of();

            if( ctx.typeId().namespacedQualifiedIdent()!=null
            && !ctx.typeId().namespacedQualifiedIdent().isEmpty()
            && ctx.typeId().namespacedQualifiedIdent().namespaceName()!=null
            && !ctx.typeId().namespacedQualifiedIdent().namespaceName().isEmpty() ){
                namespace = ImListLinked.of(
                    ctx.typeId().namespacedQualifiedIdent().namespaceName().ident()
                ).map(RuleContext::getText);
            }

            if( ctx.typeId().namespacedQualifiedIdent()!=null
            &&  ctx.typeId().namespacedQualifiedIdent().qualifiedIdent()!=null
            && !ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().isEmpty()
            ) {
                ident = ImListLinked.of(
                    ctx.typeId().namespacedQualifiedIdent().qualifiedIdent().ident()
                ).map(RuleContext::getText);
            }

            var name = ident.prepend(namespace);

            ImList<TypeDecl,?> genericValues = ImListLinked.of();
            if( ctx.genericPostfix()!=null
            && !ctx.genericPostfix().isEmpty()
            ) {
                genericValues = ImListLinked
                    .of(ctx.genericPostfix().typeDecl())
                    .map(TypeDecl::of);
            }

            if( typeFlag ){
                return new NewTypeId(
                    name,
                    genericValues
                );
            }else {
                if( genericValues.size()==0 ){
                    return new TypeIdent(
                        name,
                        ImListLinked.of()
                    );
                }

                return new TypeAlias(
                    name,
                    genericValues
                );
            }
        }

        if( ctx.variantType()!=null && !ctx.variantType().isEmpty() ){
            return new Variant();
        }

        if( ctx.stringType()!=null && !ctx.stringType().isEmpty() ){
            return StringType.of(ctx.stringType());
        }

        throw AstParseError.notImplemented(ctx);
    }

    /**
     * Вариант
     */
    record Variant() implements TypeDecl {}

    /**
     * Структурный тип
     */
    sealed interface Structured extends TypeDecl {
        static Structured of( DelphiParser.StrucTypeContext ctx ){
            if( ctx.strucTypePart()!=null
            &&  !ctx.strucTypePart().isEmpty()
            ) return of(ctx.strucTypePart());

            throw AstParseError.notImplemented(ctx);
        }

        static Structured of( DelphiParser.StrucTypePartContext ctx ){
            if( ctx.classDecl()!=null
            &&  !ctx.classDecl().isEmpty()
            )return of( ctx.classDecl() );
            throw AstParseError.notImplemented(ctx);
        }

        static Structured of( DelphiParser.ClassDeclContext ctx ){
            if( ctx.interfaceTypeDecl()!=null
            && !ctx.interfaceTypeDecl().isEmpty()
            ) return Interface.of(ctx.interfaceTypeDecl());

            if(  ctx.classTypeDecl()!=null
            &&  !ctx.classTypeDecl().isEmpty()
            ) return Clazz.of(ctx.classTypeDecl());

            throw AstParseError.notImplemented(ctx);
        }
    }

    ///////////////////////////////

    /**
     * Массив
     * @param indexes тип индекса
     * @param subType тип элемента массива
     * @param packed флаг packed // TODO разобраться что за packed
     */
    record Array(
        ImList<ArrayIndex,?> indexes,
        ArraySubType subType,
        boolean packed
    ) implements Structured, TypeDecl {}

    sealed interface ArrayIndex {}
    record ArrayIndexType(ImList<String,?> typeId) implements ArrayIndex {}
    record ArrayIndexRange(Expression from, Expression to) implements ArrayIndex {}

    sealed interface ArraySubType {
        record Const() implements ArraySubType {}
        record TypeRef( TypeDecl decl ) implements ArraySubType {}
    }

    // ................

    record MetaClass(ImList<String,?> typeId) implements TypeDecl, Structured {}

    // ................

    /**
     * Описание класса
     * @param state абстрактный/запечатанный
     * @param parents родительские классы/интерфейсы
     * @param body содержание
     * @param position позиция в исходном коде
     */
    record Clazz(
        Optional<ClassState> state,
        ImList<TypeIdent,?> parents,
        ImList<ClassItem,?> body,
        SourcePosition position
    ) implements Structured {
        public String toString(){
            var sb = new StringBuilder();
            sb.append("Class\n");

            state.ifPresent(s -> sb.append("  state: ").append(s).append("\n"));

            if(parents.size()>0) {
                sb.append("  parent:\n");
                parents.forEach(p -> {
                    sb.append(indent("    ", p.toString()));
                });
            }

            if(body.size()>0){
                sb.append("  body:\n");
                body.forEach(itm -> {
                    sb.append(indent("    ",itm.toString()));
                });
            }

            return sb.toString();
        }

        static Clazz of(DelphiParser.ClassTypeDeclContext ctx){
            var state = Optional.<ClassState>empty();
            if( ctx.classState()!=null && !ctx.classState().isEmpty() ){
                if(ctx.classState().getText().toLowerCase().startsWith("seal"))
                    state = Optional.of(ClassState.Sealed);
                else if(ctx.classState().getText().toLowerCase().startsWith("abst"))
                    state = Optional.of(ClassState.Abstract);
                else throw AstParseError.unExpected();
            }

            ImList<TypeIdent,?> parents = ImListLinked.of();
            if( ctx.classParent()!=null && !ctx.classParent().isEmpty() ){
                parents = ImListLinked.of(ctx.classParent().genericTypeIdent())
                    .map(TypeIdent::of);
            }

            ImList<ClassItem,?> body =
                ctx.classItem()!=null && !ctx.classItem().isEmpty() ?
                    ImListLinked.of(ctx.classItem()).fmap(
                        ClassItem::of
                    ) :
                    ImListLinked.of();

            // todo methods?
            return new Clazz(state, parents, body, SourcePosition.of(ctx));
        }
    }

    /**
     * Абстрактный или запечатанный класс
     */
    enum ClassState {
        Sealed,
        Abstract
    }

    // ...............

    /**
     * Описание интерфейса
     * @param parents родительский интерфейс(ы)
     * @param itfType обычный или IDispatch
     * @param guid идентификатор
     * @param body содержание интерфейса
     * @param position позиция в исходном коде
     */
    record Interface(
        ImList<TypeIdent,?> parents,
        InterfaceType itfType,
        Optional<String> guid,
        ImList<InterfaceItem,?> body,
        SourcePosition position
    ) implements Structured, TypeDecl {
        static Interface of( DelphiParser.InterfaceTypeDeclContext ctx ){
            ImList<TypeIdent,?> parents = ctx.classParent()!=null && !ctx.classParent().isEmpty() ?
                ImListLinked.of(ctx.classParent().genericTypeIdent()).map(TypeIdent::of)
                : ImListLinked.of();

            Optional<String> guid =
                ctx.interfaceGuid()==null || ctx.interfaceGuid().isEmpty() ?
                    Optional.empty() :
                    Optional.of(ctx.interfaceGuid().getText());

            var itype = switch (ctx.interfaceKey().INTERFACE().getText().toLowerCase()) {
                case "interface" -> InterfaceType.Basic;
                case "dispinterface" -> InterfaceType.DispInterface;
                default -> throw AstParseError.unExpected();
            };

            return new Interface(
                parents,
                itype,
                guid,
                ImListLinked.of(ctx.interfaceItem()).map(InterfaceItem::of),
                SourcePosition.of(ctx)
            );
        }

        @Override
        public String toString(){
            var sb = new StringBuilder();
            sb.append("Interface\n");

            sb.append("  itfType: ").append(itfType).append("\n");
            guid.ifPresent(s -> sb.append("  guid: ").append(s).append("\n"));

            if(parents.size()>0) {
                sb.append("  parent:\n");
                parents.forEach(p -> {
                    sb.append(indent("    ", p.toString()));
                });
            }

            if(body.size()>0){
                body.forEach(itm -> {
                    sb.append(indent("  ",itm.toString()));
                });
            }

            return sb.toString();
        }
    }

    enum InterfaceType {
        Basic,
        DispInterface
    }

    ////////////////////////////////

    /**
     * Некий простой тип
     * @param name имя типа
     */
    record Simple(String name) implements TypeDecl {}

    ////////////////////////////////

    /**
     * Идентификатор типа.
     *
     * <br/>
     * Флаг typeFlag - объявляет новый тип, а не синоним типа.
     *
     * <a href="https://docwiki.embarcadero.com/RADStudio/Alexandria/en/Type_Compatibility_and_Identity_(Delphi)">
     *     Подробности тут
     * </a>
     *
     * <br/>
     * <b>Type Identity</b>
     * <br/>
     *
     * When one type identifier is declared using another type identifier,
     * without qualification, they denote the same type. Thus, given the declarations:
     *
     * <pre>
     * type
     *   T1 = Integer;
     *   T2 = T1;
     *   T3 = Integer;
     *   T4 = T2;
     * </pre>
     *
     * T1, T2, T3, T4, and Integer all denote the same type.
     * To create distinct types, repeat the word type in the declaration. For example:
     *
     * <pre>
     *     type TMyInteger = type Integer;
     * </pre>
     *
     * @param name имя типа
     * @param genericParams параметры типа
     */
    record NewTypeId(
        ImList<String,?> name,
        ImList<TypeDecl,?> genericParams
    ) implements TypeDecl {
    }

    record TypeAlias(ImList<String,?> name, ImList<TypeDecl,?> genericParams)
    implements TypeDecl
    {}

    /////////////////////////////////

    /**
     * Текстовый тип
     */
    sealed interface StringType extends TypeDecl {
        record StrIng(Optional<Expression> expression) implements StringType {}
        record AnsiString( Optional<String> codePageNum, boolean typeFlag ) implements StringType {}

        static StringType of(DelphiParser.StringTypeContext ctx){
            if( ctx.STRING()!=null && ctx.STRING().getText()!=null ){
                if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                    return new StrIng(
                        Optional.of(Expression.of(ctx.expression()))
                    );
                }

                return new StrIng(Optional.empty());
            }

            if( ctx.ANSISTRING()!=null && ctx.ANSISTRING().getText()!=null ){
                var typeFlag = ctx.TYPE()!=null && ctx.TYPE().getText()!=null;
                var codePage =
                    ctx.codePageNumber()!=null &&
                    ctx.codePageNumber().intNum()!=null &&
                    ctx.codePageNumber().intNum().getText()!=null ?
                        Optional.of(ctx.codePageNumber().intNum().getText()) :
                        Optional.<String>empty();
                return new AnsiString(codePage, typeFlag);
            }

            throw AstParseError.unExpected(ctx);
        }
    }
}
