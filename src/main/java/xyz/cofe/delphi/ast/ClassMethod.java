package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Методы класса/интерфейса/...
 */
public sealed interface ClassMethod extends InterfaceItem, ClassItem, SrcPos, Commented<ClassMethod> {
    @Override
    ClassMethod astUpdate(AstUpdate.UpdateContext ctx);

    /**
     * Процедура
     * @param name имя
     * @param genericParams generic параметры
     * @param arguments аргументы
     * @param directives директивы компилятора
     */
    record Procedure(
        String name,
        ImList<Generic.Param,?> genericParams,
        ImList<Argument,?> arguments,
        ImList<MethodDirective,?> directives,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements ClassMethod, SrcPos {
        @Override
        public Procedure astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.update(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Procedure) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            res = new Procedure(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                directives.orElse(this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Procedure withComments(ImList<Comment, ?> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new Procedure(name,genericParams,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
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
        ImList<Generic.Param,?> genericParams,
        ImList<Argument,?> arguments,
        ImList<MethodDirective,?> directives,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements ClassMethod, SrcPos {
        @Override
        public Constructor withComments(ImList<Comment, ?> comments) {
            return new Constructor(name,genericParams,arguments,directives,position,comments);
        }

        public Constructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.update(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Constructor) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            res = new Constructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                directives.orElse(this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
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
        ImList<Generic.Param,?> genericParams,
        ImList<Argument,?> arguments,
        ImList<MethodDirective,?> directives,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements ClassMethod, SrcPos {
        public Destructor astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.update(this.directives);

            var res = this;
            if( ctx instanceof AstUpdate.CommentingContext cc ){
                res = (Destructor) cc.commenting(res);
            }

            if( directives.isEmpty() && genericParams.isEmpty() && arguments.isEmpty() && res.comments==this.comments )return this;

            res = new Destructor(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                directives.orElse(this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Destructor withComments(ImList<Comment, ?> comments) {
            return new Destructor(name,genericParams,arguments,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
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
        ImList<Generic.Param,?> genericParams,
        ImList<Argument,?> arguments,
        TypeDecl result,
        ImList<MethodDirective,?> directives,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements ClassMethod, SrcPos {
        @Override
        public Function astUpdate(AstUpdate.UpdateContext ctx) {
            var genericParams = ctx.update(this.genericParams); // AstUpdate.astUpdates(this.genericParams, ctx);
            var arguments = ctx.update(this.arguments);
            var directives = ctx.update(this.directives);
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

            res = new Function(
                name,
                genericParams.orElse(this.genericParams),
                arguments.orElse(this.arguments),
                result,
                directives.orElse(this.directives),
                position,
                res.comments
            );

            return res;
        }

        @Override
        public Function withComments(ImList<Comment, ?> comments) {
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            return new Function(name,genericParams,arguments,result,directives,position,comments);
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
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
        ImList<Generic.Param,?> genericParams,
        ImList<Argument,?> arguments,
        TypeDecl result,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements ClassMethod, SrcPos {
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
        public Operator withComments(ImList<Comment, ?> comments) {
            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return upcast(genericParams).append(upcast(arguments)).append(result);
        }
    }

    sealed interface MethodDirective extends AstNode, AstUpdate<MethodDirective> {
        @Override
        default MethodDirective astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        record Reintroduce() implements MethodDirective {}
        record Overload() implements MethodDirective {}

        sealed interface Binding extends MethodDirective {
            record Message(Expression expression) implements Binding {}
            record Static() implements Binding {}
            record Dynamic() implements Binding {}
            record Override() implements Binding {}
            record Virtual() implements Binding {}
        }

        record Abstract() implements MethodDirective {}
        record Final() implements MethodDirective {}

        record Inline() implements MethodDirective {}
        record Assembler() implements MethodDirective {}

        sealed interface CallConvention extends MethodDirective {
            record Cdecl() implements CallConvention {}
            record Pascal() implements CallConvention {}
            record Register() implements CallConvention {}
            record SafeCall() implements CallConvention {}
            record StdCall() implements CallConvention {}
            record Export() implements CallConvention {}

            record Far() implements CallConvention {}
            record Local() implements CallConvention {}
            record Near() implements CallConvention {}
        }

        record DispID(Expression expression) implements MethodDirective {
            @Override
            public ImList<? extends AstNode, ?> nestedAstNodes() {
                return ImListLinked.of(expression);
            }

            @Override
            public DispID astUpdate(AstUpdate.UpdateContext ctx) {
                var expression = this.expression.astUpdate(ctx);
                if( expression==this.expression )return this;

                return new DispID(expression);
            }
        }

        static MethodDirective of(DelphiParser.MethodDirectiveContext ctx){
            if(ctx.reintroduceDirective()!=null && !ctx.reintroduceDirective().isEmpty())return new Reintroduce();
            if(ctx.overloadDirective()!=null && !ctx.overloadDirective().isEmpty())return new Overload();
            if(ctx.bindingDirective()!=null && !ctx.bindingDirective().isEmpty()){
                var txt = ctx.bindingDirective().getText().toLowerCase();
                if(txt.startsWith("message"))
                    return new Binding.Message(
                        Expression.of(ctx.bindingDirective().expression())
                    );
                if(txt.startsWith("static")) return new Binding.Static();
                if(txt.startsWith("dynam")) return new Binding.Dynamic();
                if(txt.startsWith("override")) return new Binding.Override();
                if(txt.startsWith("virt")) return new Binding.Virtual();
                throw AstParseError.unExpected();
            }
            if(ctx.abstractDirective()!=null && !ctx.abstractDirective().isEmpty()){
                var txt = ctx.abstractDirective().getText().toLowerCase();
                if(txt.startsWith("abstr")) return new Abstract();
                if(txt.startsWith("final")) return new Final();
                throw AstParseError.unExpected();
            }
            if(ctx.inlineDirective()!=null && !ctx.inlineDirective().isEmpty()){
                var txt = ctx.inlineDirective().getText().toLowerCase();
                if(txt.startsWith("inline")) return new Inline();
                if(txt.startsWith("assem")) return new Assembler();
                throw AstParseError.unExpected();
            }
            if(ctx.callConvention()!=null && !ctx.callConvention().isEmpty()){
                var txt = ctx.callConvention().getText().toLowerCase();
                if( txt.startsWith("cdecl") )return new CallConvention.Cdecl();
                if( txt.startsWith("pascal") )return new CallConvention.Pascal();
                if( txt.startsWith("register") )return new CallConvention.Register();
                if( txt.startsWith("safecall") )return new CallConvention.SafeCall();
                if( txt.startsWith("stdcall") )return new CallConvention.StdCall();
                if( txt.startsWith("export") )return new CallConvention.Export();
                throw AstParseError.unExpected();
            }
            if(ctx.oldCallConventionDirective()!=null && !ctx.oldCallConventionDirective().isEmpty()){
                var txt = ctx.callConvention().getText().toLowerCase();
                if( txt.startsWith("far") )return new CallConvention.Far();
                if( txt.startsWith("local") )return new CallConvention.Local();
                if( txt.startsWith("near") )return new CallConvention.Near();
                throw AstParseError.unExpected();
            }
            if(ctx.dispIDDirective()!=null && !ctx.dispIDDirective().isEmpty()){
                return new DispID(Expression.of(ctx.dispIDDirective().expression()));
            }
            throw AstParseError.unExpected();
        }
    }

    static ClassMethod of(DelphiParser.ClassMethodContext ctx){
        if( ctx.ident().isEmpty() )throw AstParseError.unExpected();
        var name = ctx.ident().getText();

        ImListLinked<Generic.Param> generic_params =
            ctx.genericDefinition()!=null && !ctx.genericDefinition().isEmpty()
            ? Generic.of(ctx.genericDefinition())
            : ImListLinked.of();

        ImList<Argument,?> params =
            ctx.formalParameterSection()!=null && !ctx.formalParameterSection().isEmpty() && ctx.formalParameterSection().formalParameterList()!=null ?
            Argument.of( ctx.formalParameterSection().formalParameterList() ):
            ImListLinked.of() ;

        ImList<MethodDirective,?> meth_dir =
            ctx.methodDirective()!=null ?
            ImListLinked.of(ctx.methodDirective()).map(MethodDirective::of) :
            ImListLinked.of();

        Optional<TypeDecl> result =
            (ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()) ?
                Optional.of( TypeDecl.of(ctx.typeDecl()) ) :
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
