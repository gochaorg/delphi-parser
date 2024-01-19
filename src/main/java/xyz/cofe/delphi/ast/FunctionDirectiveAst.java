package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public sealed interface FunctionDirectiveAst extends AstNode, AstUpdate<Object>
    permits FunctionDirectiveAst.Assembler, FunctionDirectiveAst.Cdecl, FunctionDirectiveAst.Depricated, FunctionDirectiveAst.Experimental, FunctionDirectiveAst.Export, FunctionDirectiveAst.External, FunctionDirectiveAst.Far, FunctionDirectiveAst.Inline, FunctionDirectiveAst.Library, FunctionDirectiveAst.Local, FunctionDirectiveAst.Near, FunctionDirectiveAst.Overload, FunctionDirectiveAst.Pascal, FunctionDirectiveAst.Platform, FunctionDirectiveAst.Register, FunctionDirectiveAst.SafeCall, FunctionDirectiveAst.StdCall, FunctionDirectiveAst.Unsafe, FunctionDirectiveAst.VarArgs, HintingDirectiveAst {

    @Override
    default FunctionDirectiveAst astUpdate(AstUpdate.UpdateContext updateCtx) {
        return this;
    }

    record Overload() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Overload astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "overload";
        }
    }

    record Inline() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Inline astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "inline";
        }
    }

    record Assembler() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Assembler astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "assembler";
        }
    }

    record Cdecl() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Cdecl astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "cdecl";
        }
    }

    record Pascal() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Pascal astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "pascal";
        }
    }

    record Register() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Register astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "register";
        }
    }

    record SafeCall() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public SafeCall astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "safecall";
        }
    }

    record StdCall() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public StdCall astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "stdcall";
        }
    }

    record Export() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Export astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "export";
        }
    }

    record Far() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Far astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "far";
        }
    }

    record Local() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Local astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "local";
        }
    }

    record Near() implements FunctionDirectiveAst, MethodDirectiveAst {
        @Override
        public Near astUpdate(AstUpdate.UpdateContext updateCtx) {
            return this;
        }

        @Override
        public String toString() {
            return "near";
        }
    }

    record VarArgs() implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "varargs";
        }
    }

    record Unsafe() implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "unsafe";
        }
    }

    record Experimental() implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "experimental";
        }
    }

    record Platform() implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "platform";
        }
    }

    record Library() implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "library";
        }
    }

    record Depricated(Optional<String> message) implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "depricated"+message.map(m->" "+m).orElse("");
        }
    }

    record External(
        Optional<ConstSectionAst.ConstExpression> constExpression,
        ImList<ExternalSpecifier> specifiers
    ) implements FunctionDirectiveAst {
        @Override
        public String toString() {
            return "external"+constExpression.map(e -> " expr:"+e).orElse("")+(specifiers.size()==0 ? "" : specifiers.toString());
        }
    }

    sealed interface ExternalSpecifier {
    }

    record ExternalSpecName(ConstSectionAst.ConstExpression index) implements ExternalSpecifier {
    }

    record ExternalSpecIndex(ConstSectionAst.ConstExpression index) implements ExternalSpecifier {
    }

    static FunctionDirectiveAst of(DelphiParser.FuncDirectiveContext ctx) {
        if (ctx.UNSAFE() != null) return new Unsafe();
        if (ctx.OVERLOAD() != null) return new Overload();
        if (ctx.INLINE() != null) return new Inline();
        if (ctx.ASSEMBLER() != null) return new Assembler();
        if (ctx.CDECL() != null) return new Cdecl();
        if (ctx.PASCAL() != null) return new Pascal();
        if (ctx.REGISTER() != null) return new Register();
        if (ctx.SAFECALL() != null) return new SafeCall();
        if (ctx.STDCALL() != null) return new StdCall();
        if (ctx.EXPORT() != null) return new Export();
        if (ctx.FAR() != null) return new Far();
        if (ctx.LOCAL() != null) return new Local();
        if (ctx.NEAR() != null) return new Near();
        if (ctx.DEPRECATED() != null) {
            if (ctx.stringFactor() != null) {
                return new Depricated(Optional.of(ctx.stringFactor().getText()));
            }
            return new Depricated(Optional.empty());
        }
        if (ctx.EXPERIMENTAL() != null) return new Experimental();
        if (ctx.PLATFORM() != null) return new Platform();
        if (ctx.LIBRARY() != null) return new Library();
        if (ctx.VARARGS() != null) return new VarArgs();

        if (ctx.EXTERNAL() != null) {
            ImList<ExternalSpecifier> specifiers = ImListLinked.of();

            if (ctx.constExpression() != null && !ctx.constExpression().isEmpty()
            ) {
                var constExp = ConstSectionAst.ConstExpression.of(ctx.constExpression());

                if (ctx.externalSpecifier() != null && !ctx.externalSpecifier().isEmpty()) {
                    specifiers = ImList.of(ctx.externalSpecifier()).map(spec -> {
                        if (spec.NAME() != null && !spec.NAME().getText().isEmpty() && spec.constExpression() != null) {
                            return new ExternalSpecName(
                                ConstSectionAst.ConstExpression.of(spec.constExpression())
                            );
                        } else if (spec.INDEX() != null && !spec.INDEX().getText().isEmpty() && spec.constExpression() != null) {
                            return new ExternalSpecIndex(
                                ConstSectionAst.ConstExpression.of(spec.constExpression())
                            );
                        }

                        throw AstParseError.unExpected(spec);
                    });
                }

                return new External(Optional.of(constExp), specifiers);
            }

            return new External(Optional.empty(), specifiers);
        }

        throw AstParseError.unExpected(ctx);
    }
}
