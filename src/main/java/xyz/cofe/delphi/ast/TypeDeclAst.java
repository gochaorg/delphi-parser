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
public sealed interface TypeDeclAst
    extends AstNode
    permits
    ArrayAst,
        TypeDeclAst.Clazz,
        TypeDeclAst.Interface,
        TypeDeclAst.MetaClass,
        TypeDeclAst.NewTypeId,
        TypeDeclAst.Simple,
        TypeDeclAst.StringType,
    StructuredTypeAst,
        TypeDeclAst.TypeAlias,
    VariantTypeAst,
        TypeIdentAst,
        ProcedureTypeAst
{
    @Override
    TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx);

    static TypeDeclAst of(DelphiParser.TypeDeclContext ctx) {
        if( ctx.strucType()!=null
        &&  !ctx.strucType().isEmpty()
        ) return StructuredTypeAst.of(ctx.strucType());

        if( ctx.typeId()!=null && !ctx.typeId().isEmpty() ){
            boolean typeFlag = ctx.TYPE()!=null
                && ctx.TYPE().getText()!=null
                && !ctx.TYPE().getText().isEmpty();

            ImList<String> namespace = ImListLinked.of();
            ImList<String> ident = ImListLinked.of();

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

            ImList<TypeDeclAst> genericValues = ImListLinked.of();
            if( ctx.genericPostfix()!=null
            && !ctx.genericPostfix().isEmpty()
            ) {
                genericValues = ImListLinked
                    .of(ctx.genericPostfix().typeDecl())
                    .map(TypeDeclAst::of);
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
                    return new TypeIdentAst(
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
            return new VariantTypeAst();
        }

        if( ctx.stringType()!=null && !ctx.stringType().isEmpty() ){
            return StringType.of(ctx.stringType());
        }

        if( ctx.procedureType()!=null && !ctx.procedureType().isEmpty() ){
            return ProcedureTypeAst.of(ctx.procedureType());
        }

        throw AstParseError.notImplemented(ctx);
    }

    ///////////////////////////////

    record MetaClass(ImList<String> typeId) implements TypeDeclAst, StructuredTypeAst {
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
        ImList<TypeIdentAst> parents,
        ImList<ClassItemAst> body,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StructuredTypeAst, SrcPos, Commented<Clazz>, TypeDeclAst {
        @Override
        public Clazz astUpdate(AstUpdate.UpdateContext ctx) {
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");

            var parents = ctx.update(this.parents);
            //noinspection unchecked
            Optional<ImList<ClassItemAst>> body = ctx.update(this.body);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ) res = cc.commenting(res);

            if( parents.isEmpty() && body.isEmpty() && res.comments==this.comments )return this;

            ImList<ClassItemAst> b2 = body.orElse(this.body);

            return new Clazz(
                state, parents.orElse(this.parents),
                b2,
                position,
                res.comments);
        }

        @Override
        public Clazz withComments(ImList<Comment> comments) {
            return new Clazz(state,parents,body,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(parents).append(upcast(body));
        }

        public String toString(){
            var sb = new StringBuilder();
            sb.append("Class\n");

            state.ifPresent(s -> sb.append("  state: ").append(s).append("\n"));

            if(parents.size()>0) {
                sb.append("  parent:\n");
                parents.each(p -> {
                    sb.append(indent("    ", p.toString()));
                });
            }

            if(body.size()>0){
                sb.append("  body:\n");
                body.each(itm -> {
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

            ImList<TypeIdentAst> parents = ImListLinked.of();
            if( ctx.classParent()!=null && !ctx.classParent().isEmpty() ){
                parents = ImListLinked.of(ctx.classParent().genericTypeIdent())
                    .map(TypeIdentAst::of);
            }

            ImList<ClassItemAst> body =
                ctx.classItem()!=null && !ctx.classItem().isEmpty() ?
                    ImListLinked.of(ctx.classItem()).fmap(
                        ClassItemAst::of
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
        ImList<TypeIdentAst> parents,
        InterfaceType itfType,
        Optional<String> guid,
        ImList<InterfaceItemAst> body,
        SourcePosition position,
        ImList<Comment> comments
    ) implements StructuredTypeAst, TypeDeclAst, SrcPos, Commented<Interface> {
        @Override
        public Interface astUpdate(AstUpdate.UpdateContext ctx) {
            var parents = ctx.update(this.parents);
            Optional<ImList<InterfaceItemAst>> body = ctx.update(this.body);

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
        public Interface withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(parents).append(upcast(body));
        }

        static Interface of(DelphiParser.InterfaceTypeDeclContext ctx ){
            ImList<TypeIdentAst> parents = ctx.classParent()!=null && !ctx.classParent().isEmpty() ?
                ImListLinked.of(ctx.classParent().genericTypeIdent()).map(TypeIdentAst::of)
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
                ImListLinked.of(ctx.interfaceItem()).map(InterfaceItemAst::of),
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
                parents.each(p -> {
                    sb.append(indent("    ", p.toString()));
                });
            }

            if(body.size()>0){
                body.each(itm -> {
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
    record Simple(String name) implements TypeDeclAst {
        @Override
        public TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
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
        ImList<String> name,
        ImList<TypeDeclAst> genericParams,
        SourcePosition position,
        ImList<Comment> comments
    ) implements TypeDeclAst, SrcPos, Commented<NewTypeId> {
        @Override
        public NewTypeId astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public NewTypeId withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return genericParams;
        }
    }

    record TypeAlias(ImList<String> name, ImList<TypeDeclAst> genericParams, SourcePosition position, ImList<Comment> comments )
    implements TypeDeclAst, SrcPos, Commented<TypeAlias>
    {
        @Override
        public TypeAlias astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public TypeAlias withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return genericParams;
        }
    }

    /////////////////////////////////

    /**
     * Текстовый тип
     */
    sealed interface StringType extends TypeDeclAst, SrcPos {
        record StrIng(Optional<ExpressionAst> expression, SourcePosition position, ImList<Comment> comments ) implements StringType, Commented<StrIng> {
            @Override
            public StrIng astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public StrIng withComments(ImList<Comment> comments) {
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
        record AnsiString( Optional<String> codePageNum, boolean typeFlag, SourcePosition position, ImList<Comment> comments ) implements StringType, Commented<AnsiString> {
            @Override
            public AnsiString astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public AnsiString withComments(ImList<Comment> comments) {
                return new AnsiString(codePageNum,typeFlag,position,comments);
            }
        }

        static StringType of(DelphiParser.StringTypeContext ctx){
            if( ctx.STRING()!=null && ctx.STRING().getText()!=null ){
                if( ctx.expression()!=null && !ctx.expression().isEmpty() ){
                    return new StrIng(
                        Optional.of(ExpressionAst.of(ctx.expression())),
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
