package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Методы класса/интерфейса/...
 */
public sealed interface ClassMethod extends InterfaceItem, ClassItem {
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
        SourcePosition position
    ) implements ClassMethod {}

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
        SourcePosition position
    ) implements ClassMethod {}

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
        SourcePosition position
    ) implements ClassMethod {}

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
        SourcePosition position
    ) implements ClassMethod {}

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
        SourcePosition position
    ) implements ClassMethod {}

    sealed interface MethodDirective {
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

        record DispID(Expression expression) implements MethodDirective {}

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
                if(txt.startsWith("override")) return new Binding.Dynamic();
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
            return new Operator(name,generic_params,params,result.get(),SourcePosition.of(ctx));
        }

        if( methKey.startsWith("pro") ){
            return new Procedure(name,generic_params,params,meth_dir,SourcePosition.of(ctx));
        }

        if( methKey.startsWith("cons") ){
            return new Constructor(name,generic_params,params,meth_dir,SourcePosition.of(ctx));
        }

        if( methKey.startsWith("des") ){
            return new Destructor(name,generic_params,params,meth_dir,SourcePosition.of(ctx));
        }

        if( ctx.FUNCTION()!=null
        && ctx.FUNCTION().getText()!=null
        && ctx.FUNCTION().getText().length()>0
        && result!=null
        && result.isPresent() ){
            return new Function(name,generic_params,params,result.get(),meth_dir,SourcePosition.of(ctx));
        }

        throw AstParseError.notImplemented(ctx);
    }
}
