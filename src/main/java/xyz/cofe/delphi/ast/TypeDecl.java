package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;
import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Объявление типа
 */
public sealed interface TypeDecl
    extends AstNode
    permits TypeDecl.Array, TypeDecl.Interface, TypeDecl.MetaClass, TypeDecl.NewTypeId, TypeDecl.Simple, TypeDecl.StringType, TypeDecl.Structured, TypeDecl.TypeAlias, TypeDecl.Variant, TypeIdent
{
    @Override
    TypeDecl astUpdate(AstUpdate.UpdateContext ctx);

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
                    genericValues,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
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
                    genericValues,
                    SourcePosition.of(ctx),
                    ImListLinked.of()
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
    record Variant() implements TypeDecl {
        @Override
        public Variant astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    /**
     * Структурный тип
     */
    sealed interface Structured extends TypeDecl {
        @Override
        Structured astUpdate(AstUpdate.UpdateContext ctx);

        static Structured of(DelphiParser.StrucTypeContext ctx ){
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
        boolean packed,
        SourcePosition position
    ) implements Structured, TypeDecl, SrcPos {
        @Override
        public Array astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return upcast(indexes).append(subType);
        }
    }

    sealed interface ArrayIndex extends AstNode {
        @Override
        ArrayIndex astUpdate(AstUpdate.UpdateContext ctx);
    }
    record ArrayIndexType(ImList<String,?> typeId) implements ArrayIndex {
        @Override
        public ArrayIndexType astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }
    record ArrayIndexRange(Expression from, Expression to) implements ArrayIndex {
        @Override
        public ArrayIndexRange astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return ImListLinked.of(from, to);
        }
    }

    sealed interface ArraySubType extends AstNode {
        @Override
        ArraySubType astUpdate(AstUpdate.UpdateContext ctx);

        record Const() implements ArraySubType {
            @Override
            public Const astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }
        }

        record TypeRef( TypeDecl decl ) implements ArraySubType {
            @Override
            public TypeRef astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public ImList<? extends AstNode, ?> nestedAstNodes() {
                return ImListLinked.of(decl);
            }
        }
    }

    // ................

    record MetaClass(ImList<String,?> typeId) implements TypeDecl, Structured {
        @Override
        public MetaClass astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

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
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements Structured, SrcPos, Commented<Clazz> {
        @Override
        public Clazz astUpdate(AstUpdate.UpdateContext ctx) {
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");

            var parents = ctx.update(this.parents);
            //noinspection unchecked
            Optional<ImList<ClassItem,?>> body = ctx.update(this.body);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ) res = cc.commenting(res);

            if( parents.isEmpty() && body.isEmpty() && res.comments==this.comments )return this;

            ImList<ClassItem,?> b2 = body.orElse(this.body);

            return new Clazz(
                state, parents.orElse(this.parents),
                b2,
                position,
                res.comments);
        }

        @Override
        public Clazz withComments(ImList<Comment, ?> comments) {
            return new Clazz(state,parents,body,position,comments);
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return upcast(parents).append(upcast(body));
        }

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
            return new Clazz(state, parents, body, SourcePosition.of(ctx), ImListLinked.of());
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
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements Structured, TypeDecl, SrcPos, Commented<Interface> {
        @Override
        public Interface astUpdate(AstUpdate.UpdateContext ctx) {
            var parents = ctx.update(this.parents);
            Optional<ImList<InterfaceItem,?>> body = ctx.update(this.body);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = cc.commenting(res);
            }

            if( parents.isEmpty()
            && body.isEmpty()
            && res.comments==this.comments
            ) return this;

            return new Interface(
                parents.orElse(this.parents),
                itfType,
                guid,
                body.orElse(this.body),
                position,
                res.comments
            );
        }

        @Override
        public Interface withComments(ImList<Comment, ?> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return upcast(parents).append(upcast(body));
        }

        static Interface of(DelphiParser.InterfaceTypeDeclContext ctx ){
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
                SourcePosition.of(ctx),
                ImListLinked.of()
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
    record Simple(String name) implements TypeDecl {
        @Override
        public TypeDecl astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

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
        ImList<TypeDecl,?> genericParams,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements TypeDecl, SrcPos, Commented<NewTypeId> {
        @Override
        public NewTypeId astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public NewTypeId withComments(ImList<Comment, ?> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return genericParams;
        }
    }

    record TypeAlias( ImList<String,?> name, ImList<TypeDecl,?> genericParams, SourcePosition position, ImList<Comment,?> comments )
    implements TypeDecl, SrcPos, Commented<TypeAlias>
    {
        @Override
        public TypeAlias astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public TypeAlias withComments(ImList<Comment, ?> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return genericParams;
        }
    }

    /////////////////////////////////

    /**
     * Текстовый тип
     */
    sealed interface StringType extends TypeDecl, SrcPos {
        record StrIng( Optional<Expression> expression, SourcePosition position, ImList<Comment,?> comments ) implements StringType, Commented<StrIng> {
            @Override
            public StrIng astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public StrIng withComments(ImList<Comment, ?> comments) {
                return new StrIng(expression,position,comments);
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                StrIng strIng = (StrIng) o;
                return Objects.equals(expression, strIng.expression);
            }

            @Override
            public int hashCode() {
                return Objects.hash(expression);
            }
        }
        record AnsiString( Optional<String> codePageNum, boolean typeFlag, SourcePosition position, ImList<Comment,?> comments ) implements StringType, Commented<AnsiString> {
            @Override
            public AnsiString astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public AnsiString withComments(ImList<Comment, ?> comments) {
                return new AnsiString(codePageNum,typeFlag,position,comments);
            }
        }

        static StringType of(DelphiParser.StringTypeContext ctx){
            if( ctx.STRING()!=null && ctx.STRING().getText()!=null ){
                if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                    return new StrIng(
                        Optional.of(Expression.of(ctx.expression())),
                        SourcePosition.of(ctx),
                        ImListLinked.of()
                    );
                }

                return new StrIng(Optional.empty(), SourcePosition.of(ctx), ImListLinked.of());
            }

            if( ctx.ANSISTRING()!=null && ctx.ANSISTRING().getText()!=null ){
                var typeFlag = ctx.TYPE()!=null && ctx.TYPE().getText()!=null;
                var codePage =
                    ctx.codePageNumber()!=null &&
                    ctx.codePageNumber().intNum()!=null &&
                    ctx.codePageNumber().intNum().getText()!=null ?
                        Optional.of(ctx.codePageNumber().intNum().getText()) :
                        Optional.<String>empty();
                return new AnsiString(codePage, typeFlag, SourcePosition.of(ctx), ImListLinked.of());
            }

            throw AstParseError.unExpected(ctx);
        }
    }
}
