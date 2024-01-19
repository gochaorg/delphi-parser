package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;
import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Методы класса/интерфейса/...
 */
public sealed interface ClassMethodAst extends InterfaceItemAst, ClassItemAst, SrcPos, Commented<ClassMethodAst> {
    /**
     * аргументы метода
     * @return аргументы
     */
    ImList<ArgumentAst> arguments();

    @Override
    ClassMethodAst astUpdate(AstUpdate.UpdateContext ctx);

    /**
     * Процедура
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param directives директивы компилятора
     */
    record Procedure(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ClassMethodAst, SrcPos {
        @Override
        public Procedure astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Procedure) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            //noinspection unchecked,rawtypes
            res = new Procedure(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives), //directives.orElse(this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Procedure withComments(ImList<Comment> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new Procedure(name,genericParams,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(upcast(directives));
        }
    }

    /**
     * Конструктор
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param directives директивы компилятора
     */
    record Constructor(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ClassMethodAst, SrcPos {
        @Override
        public Constructor withComments(ImList<Comment> comments) {
            return new Constructor(name,genericParams,arguments,directives,position,comments);
        }

        public Constructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Constructor) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            //noinspection unchecked,rawtypes
            res = new Constructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(upcast(directives));
        }
    }

    /**
     * Деструктор
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param directives директивы компилятора
     */
    record Destructor(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ClassMethodAst, SrcPos {
        public Destructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Destructor) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            //noinspection unchecked,rawtypes
            res = new Destructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Destructor withComments(ImList<Comment> comments) {
            return new Destructor(name,genericParams,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(upcast(directives));
        }
    }

    /**
     * Функция
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param result результат вызова
     * @param directives директивы компилятора
     */
    record Function(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        ImList<MethodDirectiveAst> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ClassMethodAst, SrcPos {
        @Override
        public Function astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.updateUnsafe(this.directives);
            var result = this.result.astUpdate(ctx);

            var res = this;

            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Function) cc.commenting(res);
            }

            if( directives.isEmpty()
            && genericParams.isEmpty()
            && arguments.isEmpty()
            && result==this.result
            && res.comments==this.comments
            )return this;

            //noinspection unchecked,rawtypes
            res = new Function(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                result,
                (ImList) directives.orElse((ImList) this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Function withComments(ImList<Comment> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new Function(name,genericParams,arguments,result,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(upcast(directives)).append(result);
        }
    }

    /**
     * Оператор
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param result результат вызова
     */
    record Operator(
        String name,
        ImList<GenericAst.Param> genericParams,
        ImList<ArgumentAst> arguments,
        TypeDeclAst result,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ClassMethodAst, SrcPos {
        public Operator astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var result = this.result.astUpdate(ctx);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Operator) cc.commenting(res);
            }

            if( genericParams.isEmpty()
            && arguments.isEmpty()
            && res.comments==this.comments
            && result==this.result
            )return this;

            res = new Operator(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                result,
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Operator withComments(ImList<Comment> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(result);
        }
    }

    static ClassMethodAst of(DelphiParser.ClassMethodContext ctx){
        if( ctx.ident().isEmpty() )throw AstParseError.unExpected();
        var name = ctx.ident().getText();

        ImListLinked<GenericAst.Param> generic_params =
            ctx.genericDefinition()!=null && !ctx.genericDefinition().isEmpty()
            ? GenericAst.of(ctx.genericDefinition())
            : ImListLinked.of();

        ImList<ArgumentAst> params =
            ctx.formalParameterSection()!=null && !ctx.formalParameterSection().isEmpty() && ctx.formalParameterSection().formalParameterList()!=null ?
            ArgumentAst.of( ctx.formalParameterSection().formalParameterList() ):
            ImListLinked.of() ;

        ImList<MethodDirectiveAst> meth_dir =
            ctx.methodDirective()!=null ?
            ImListLinked.of(ctx.methodDirective()).map(MethodDirectiveAst::of) :
            ImListLinked.of();

        Optional<TypeDeclAst> result =
            (ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()) ?
                Optional.of( TypeDeclAst.of(ctx.typeDecl()) ) :
                Optional.empty();

        var methKey =
            ctx.methodKey()==null ||
            ctx.methodKey().isEmpty() ||
            ctx.methodKey().getText()==null ?
                "" :
                ctx.methodKey().getText();

        //noinspection ConstantConditions
        if( ctx.OPERATOR()!=null
            && ctx.OPERATOR().getText().toLowerCase().startsWith("opera")
            && result.isPresent()
        ){
            return new Operator(name,generic_params,params,result.get(),SourcePosition.of(ctx),ImListLinked.of());
        }

        if( methKey.startsWith("pro") ){
            return new Procedure(name,generic_params,params,meth_dir,SourcePosition.of(ctx),ImListLinked.of());
        }

        if( methKey.startsWith("cons") ){
            return new Constructor(name,generic_params,params,meth_dir,SourcePosition.of(ctx),ImListLinked.of());
        }

        if( methKey.startsWith("des") ){
            return new Destructor(name,generic_params,params,meth_dir,SourcePosition.of(ctx),ImListLinked.of());
        }

        if( ctx.FUNCTION()!=null
        && ctx.FUNCTION().getText()!=null
        && ctx.FUNCTION().getText().length()>0
        && result!=null
        && result.isPresent() ){
            return new Function(name,generic_params,params,result.get(),meth_dir,SourcePosition.of(ctx),ImListLinked.of());
        }

        throw AstParseError.notImplemented(ctx);
    }
}
