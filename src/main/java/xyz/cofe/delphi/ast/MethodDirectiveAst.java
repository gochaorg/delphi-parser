package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

sealed public interface MethodDirectiveAst extends AstNode,
                                                   AstUpdate<Object> permits FunctionDirectiveAst.Assembler, FunctionDirectiveAst.Cdecl, FunctionDirectiveAst.Export, FunctionDirectiveAst.Far, FunctionDirectiveAst.Inline, FunctionDirectiveAst.Local, FunctionDirectiveAst.Near, FunctionDirectiveAst.Overload, FunctionDirectiveAst.Pascal, FunctionDirectiveAst.Register, FunctionDirectiveAst.SafeCall, FunctionDirectiveAst.StdCall, MethodDirectiveAst.Abstract, MethodDirectiveAst.Binding, MethodDirectiveAst.DispID, MethodDirectiveAst.Final, MethodDirectiveAst.Reintroduce {
    @Override
    default MethodDirectiveAst astUpdate(AstUpdate.UpdateContext updateCtx) {
        return this;
    }

    record Reintroduce() implements MethodDirectiveAst {
    }

    sealed interface Binding extends MethodDirectiveAst {
        record Message(ExpressionAst expression) implements Binding {
        }

        record Static() implements Binding {
        }

        record Dynamic() implements Binding {
        }

        record Override() implements Binding {
        }

        record Virtual() implements Binding {
        }
    }

    record Abstract() implements MethodDirectiveAst {
    }

    record Final() implements MethodDirectiveAst {
    }

    record DispID(ExpressionAst expression) implements MethodDirectiveAst {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return ImListLinked.of(expression);
        }

        @Override
        public DispID astUpdate(AstUpdate.UpdateContext ctx) {
            var expression = this.expression.astUpdate(ctx);
            if (expression == this.expression) return this;

            return new DispID(expression);
        }
    }

    static MethodDirectiveAst of(DelphiParser.MethodDirectiveContext ctx) {
        if (ctx.reintroduceDirective() != null && !ctx.reintroduceDirective().isEmpty()) return new Reintroduce();
        if (ctx.overloadDirective() != null && !ctx.overloadDirective().isEmpty()) return new FunctionDirectiveAst.Overload();
        if (ctx.bindingDirective() != null && !ctx.bindingDirective().isEmpty()) {
            var txt = ctx.bindingDirective().getText().toLowerCase();
            if (txt.startsWith("message"))
                return new Binding.Message(
                    ExpressionAst.of(ctx.bindingDirective().expression())
                );
            if (txt.startsWith("static")) return new Binding.Static();
            if (txt.startsWith("dynam")) return new Binding.Dynamic();
            if (txt.startsWith("override")) return new Binding.Override();
            if (txt.startsWith("virt")) return new Binding.Virtual();
            throw AstParseError.unExpected();
        }
        if (ctx.abstractDirective() != null && !ctx.abstractDirective().isEmpty()) {
            var txt = ctx.abstractDirective().getText().toLowerCase();
            if (txt.startsWith("abstr")) return new Abstract();
            if (txt.startsWith("final")) return new Final();
            throw AstParseError.unExpected();
        }
        if (ctx.inlineDirective() != null && !ctx.inlineDirective().isEmpty()) {
            var txt = ctx.inlineDirective().getText().toLowerCase();
            if (txt.startsWith("inline")) return new FunctionDirectiveAst.Inline();
            if (txt.startsWith("assem")) return new FunctionDirectiveAst.Assembler();
            throw AstParseError.unExpected();
        }
        if (ctx.callConvention() != null && !ctx.callConvention().isEmpty()) {
            var txt = ctx.callConvention().getText().toLowerCase();
            if (txt.startsWith("cdecl")) return new FunctionDirectiveAst.Cdecl();
            if (txt.startsWith("pascal")) return new FunctionDirectiveAst.Pascal();
            if (txt.startsWith("register")) return new FunctionDirectiveAst.Register();
            if (txt.startsWith("safecall")) return new FunctionDirectiveAst.SafeCall();
            if (txt.startsWith("stdcall")) return new FunctionDirectiveAst.StdCall();
            if (txt.startsWith("export")) return new FunctionDirectiveAst.Export();
            throw AstParseError.unExpected();
        }
        if (ctx.oldCallConventionDirective() != null && !ctx.oldCallConventionDirective().isEmpty()) {
            var txt = ctx.callConvention().getText().toLowerCase();
            if (txt.startsWith("far")) return new FunctionDirectiveAst.Far();
            if (txt.startsWith("local")) return new FunctionDirectiveAst.Local();
            if (txt.startsWith("near")) return new FunctionDirectiveAst.Near();
            throw AstParseError.unExpected();
        }
        if (ctx.dispIDDirective() != null && !ctx.dispIDDirective().isEmpty()) {
            return new DispID(ExpressionAst.of(ctx.dispIDDirective().expression()));
        }
        throw AstParseError.unExpected();
    }
}
