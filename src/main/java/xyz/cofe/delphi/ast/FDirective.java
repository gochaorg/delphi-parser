package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Директивы метода/функции
 */
public sealed interface FDirective extends AstNode {
    sealed interface FunctionDirective extends AstNode {
    }

    sealed interface MethodDirective extends AstNode {
    }

    sealed interface CallConvention {
    }

    sealed interface Binding {
    }

    sealed interface Abstr {
    }

    sealed interface Hinting {
    }

    record Message(ExpressionAst expression, SourcePosition position) implements FDirective,
                                                                                 MethodDirective,
                                                                                 Binding,
                                                                                 SrcPos {
    }

    record Static(SourcePosition position) implements FDirective,
                                                      MethodDirective,
                                                      Binding,
                                                      SrcPos {
    }

    record Dynamic(SourcePosition position) implements FDirective,
                                                       MethodDirective,
                                                       Binding,
                                                       SrcPos {
    }

    record Override(SourcePosition position) implements FDirective,
                                                        MethodDirective,
                                                        Binding,
                                                        SrcPos {
    }

    record Virtual(SourcePosition position) implements FDirective,
                                                       MethodDirective,
                                                       Binding,
                                                       SrcPos {
    }

    record Abstract(SourcePosition position) implements FDirective,
                                                        MethodDirective,
                                                        Abstr,
                                                        SrcPos {
    }

    record Final(SourcePosition position) implements FDirective,
                                                     MethodDirective,
                                                     Abstr,
                                                     SrcPos {
    }

    record Reintroduce(SourcePosition position) implements FDirective,
                                                           MethodDirective,
                                                           SrcPos {
    }

    record Overload(SourcePosition position) implements FDirective,
                                                        FunctionDirective,
                                                        MethodDirective,
                                                        SrcPos {
    }

    record Inline(SourcePosition position) implements FDirective,
                                                      FunctionDirective,
                                                      MethodDirective,
                                                      SrcPos {
    }

    record Assembler(SourcePosition position) implements FDirective,
                                                         FunctionDirective,
                                                         MethodDirective,
                                                         SrcPos {
    }

    record Cdecl(SourcePosition position) implements FDirective,
                                                     FunctionDirective,
                                                     CallConvention,
                                                     MethodDirective,
                                                     SrcPos {
    }

    record Library(SourcePosition position) implements FDirective,
                                                       FunctionDirective,
                                                       SrcPos,
                                                       Hinting,
                                                       MethodDirective {
    }

    record Platform(SourcePosition position) implements FDirective,
                                                        FunctionDirective,
                                                        SrcPos,
                                                        Hinting,
                                                        MethodDirective {
    }

    record Experimental(SourcePosition position) implements FDirective,
                                                            FunctionDirective,
                                                            SrcPos,
                                                            Hinting,
                                                            MethodDirective {
    }

    record Unsafe(SourcePosition position) implements FDirective,
                                                      FunctionDirective,
                                                      SrcPos {
    }

    record VarArgs(SourcePosition position) implements FDirective,
                                                       FunctionDirective,
                                                       SrcPos {
    }

    record Near(SourcePosition position) implements FDirective,
                                                    FunctionDirective,
                                                    CallConvention,
                                                    MethodDirective,
                                                    SrcPos {
    }

    record Local(SourcePosition position) implements FDirective,
                                                     FunctionDirective,
                                                     CallConvention,
                                                     MethodDirective,
                                                     SrcPos {
    }

    record Far(SourcePosition position) implements FDirective,
                                                   FunctionDirective,
                                                   CallConvention,
                                                   MethodDirective,
                                                   SrcPos {
    }

    record Export(SourcePosition position) implements FDirective,
                                                      FunctionDirective,
                                                      CallConvention,
                                                      MethodDirective,
                                                      SrcPos {
    }

    record StdCall(SourcePosition position) implements FDirective,
                                                       FunctionDirective,
                                                       CallConvention,
                                                       MethodDirective,
                                                       SrcPos {
    }

    record SafeCall(SourcePosition position) implements FDirective,
                                                        FunctionDirective,
                                                        CallConvention,
                                                        MethodDirective,
                                                        SrcPos {
    }

    record Register(SourcePosition position) implements FDirective,
                                                        FunctionDirective,
                                                        CallConvention,
                                                        MethodDirective,
                                                        SrcPos {
    }

    record Pascal(SourcePosition position) implements FDirective,
                                                      FunctionDirective,
                                                      CallConvention,
                                                      MethodDirective,
                                                      SrcPos {
    }

    record Depricated(Optional<String> message, SourcePosition position) implements FDirective,
                                                                                    FunctionDirective,
                                                                                    SrcPos,
                                                                                    Hinting,
                                                                                    MethodDirective {
    }

    record External(
        Optional<ConstSectionAst.ConstExpression> constExpression,
        ImList<Specifier> specifiers,
        SourcePosition position
    ) implements FDirective,
                 FunctionDirective,
                 SrcPos {
        sealed interface Specifier extends AstNode {
            record Index(ConstSectionAst.ConstExpression index) implements Specifier {
                @java.lang.Override
                public ImList<? extends AstNode> nestedAstNodes() {
                    return ImList.of(index);
                }
            }

            record Name(ConstSectionAst.ConstExpression index) implements Specifier {
                @java.lang.Override
                public ImList<? extends AstNode> nestedAstNodes() {
                    return ImList.of(index);
                }
            }
        }

        @java.lang.Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return AstNode.upcast(constExpression).append(AstNode.upcast(specifiers));
        }
    }

    record DispID(ExpressionAst expression, SourcePosition position) implements FDirective,
                                                                                MethodDirective {
    }

    static Hinting of(DelphiParser.HintingDirectiveContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.DEPRECATED() != null && !ctx.DEPRECATED().getText().isEmpty()) {
            var str = ctx.stringFactor() != null && !ctx.stringFactor().isEmpty()
                ? Optional.of(ctx.stringFactor().getText())
                : Optional.<String>empty();
            return new Depricated(str, SourcePosition.of(ctx));
        }
        if (ctx.EXPERIMENTAL() != null && !ctx.EXPERIMENTAL().getText().isEmpty())
            return new Experimental(SourcePosition.of(ctx));
        if (ctx.PLATFORM() != null && !ctx.PLATFORM().getText().isEmpty())
            return new Platform(SourcePosition.of(ctx));
        if (ctx.LIBRARY() != null && !ctx.LIBRARY().getText().isEmpty())
            return new Library(SourcePosition.of(ctx));
        throw AstParseError.unExpected(ctx);
    }

    static FunctionDirective of(DelphiParser.FuncDirectiveContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if (ctx.UNSAFE() != null) return new Unsafe(SourcePosition.of(ctx));
        if (ctx.OVERLOAD() != null) return new Overload(SourcePosition.of(ctx));
        if (ctx.INLINE() != null) return new Inline(SourcePosition.of(ctx));
        if (ctx.ASSEMBLER() != null) return new Assembler(SourcePosition.of(ctx));
        if (ctx.CDECL() != null) return new Cdecl(SourcePosition.of(ctx));
        if (ctx.PASCAL() != null) return new Pascal(SourcePosition.of(ctx));
        if (ctx.REGISTER() != null) return new Register(SourcePosition.of(ctx));
        if (ctx.SAFECALL() != null) return new SafeCall(SourcePosition.of(ctx));
        if (ctx.STDCALL() != null) return new StdCall(SourcePosition.of(ctx));
        if (ctx.EXPORT() != null) return new Export(SourcePosition.of(ctx));
        if (ctx.FAR() != null) return new Far(SourcePosition.of(ctx));
        if (ctx.LOCAL() != null) return new Local(SourcePosition.of(ctx));
        if (ctx.NEAR() != null) return new Near(SourcePosition.of(ctx));
        if (ctx.DEPRECATED() != null) {
            if (ctx.stringFactor() != null) {
                return new Depricated(Optional.of(ctx.stringFactor().getText()), SourcePosition.of(ctx));
            }
            return new Depricated(Optional.empty(), SourcePosition.of(ctx));
        }
        if (ctx.EXPERIMENTAL() != null) return new Experimental(SourcePosition.of(ctx));
        if (ctx.PLATFORM() != null) return new Platform(SourcePosition.of(ctx));
        if (ctx.LIBRARY() != null) return new Library(SourcePosition.of(ctx));
        if (ctx.VARARGS() != null) return new VarArgs(SourcePosition.of(ctx));

        if (ctx.EXTERNAL() != null) {
            ImList<External.Specifier> specifiers = ImList.of();

            if (ctx.constExpression() != null && !ctx.constExpression().isEmpty()
            ) {
                var constExp = ConstSectionAst.ConstExpression.of(ctx.constExpression());

                if (ctx.externalSpecifier() != null && !ctx.externalSpecifier().isEmpty()) {
                    specifiers = ImList.of(ctx.externalSpecifier()).map(spec -> {
                        if (spec.NAME() != null && !spec.NAME().getText().isEmpty() && spec.constExpression() != null) {
                            return new External.Specifier.Name(
                                ConstSectionAst.ConstExpression.of(spec.constExpression())
                            );
                        } else if (spec.INDEX() != null && !spec.INDEX().getText().isEmpty() && spec.constExpression() != null) {
                            return new External.Specifier.Index(
                                ConstSectionAst.ConstExpression.of(spec.constExpression())
                            );
                        }

                        throw AstParseError.unExpected(spec);
                    });
                }

                return new External(Optional.of(constExp), specifiers, SourcePosition.of(ctx));
            }

            return new External(Optional.empty(), specifiers, SourcePosition.of(ctx));
        }

        throw AstParseError.unExpected(ctx);
    }

    static MethodDirective of(DelphiParser.MethodDirectiveContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if (ctx.reintroduceDirective() != null && !ctx.reintroduceDirective().isEmpty())
            return new Reintroduce(SourcePosition.of(ctx));

        if (ctx.overloadDirective() != null && !ctx.overloadDirective().isEmpty())
            return new Overload(SourcePosition.of(ctx));

        if (ctx.bindingDirective() != null && !ctx.bindingDirective().isEmpty()) {
            var txt = ctx.bindingDirective().getText().toLowerCase();
            if (txt.startsWith("message"))
                return new Message(
                    ExpressionAst.of(ctx.bindingDirective().expression()),
                    SourcePosition.of(ctx)
                );
            if (txt.startsWith("static")) return new Static(SourcePosition.of(ctx));
            if (txt.startsWith("dynam")) return new Dynamic(SourcePosition.of(ctx));
            if (txt.startsWith("override")) return new Override(SourcePosition.of(ctx));
            if (txt.startsWith("virt")) return new Virtual(SourcePosition.of(ctx));
            throw AstParseError.unExpected();
        }

        if (ctx.abstractDirective() != null && !ctx.abstractDirective().isEmpty()) {
            var txt = ctx.abstractDirective().getText().toLowerCase();
            if (txt.startsWith("abstr")) return new Abstract(SourcePosition.of(ctx));
            if (txt.startsWith("final")) return new Final(SourcePosition.of(ctx));
            throw AstParseError.unExpected();
        }

        if (ctx.inlineDirective() != null && !ctx.inlineDirective().isEmpty()) {
            var txt = ctx.inlineDirective().getText().toLowerCase();
            if (txt.startsWith("inline")) return new Inline(SourcePosition.of(ctx));
            if (txt.startsWith("assem")) return new Assembler(SourcePosition.of(ctx));
            throw AstParseError.unExpected();
        }
        if (ctx.callConvention() != null && !ctx.callConvention().isEmpty()) {
            var txt = ctx.callConvention().getText().toLowerCase();
            if (txt.startsWith("cdecl")) return new Cdecl(SourcePosition.of(ctx));
            if (txt.startsWith("pascal")) return new Pascal(SourcePosition.of(ctx));
            if (txt.startsWith("register")) return new Register(SourcePosition.of(ctx));
            if (txt.startsWith("safecall")) return new SafeCall(SourcePosition.of(ctx));
            if (txt.startsWith("stdcall")) return new StdCall(SourcePosition.of(ctx));
            if (txt.startsWith("export")) return new Export(SourcePosition.of(ctx));
            throw AstParseError.unExpected();
        }
        if (ctx.oldCallConventionDirective() != null && !ctx.oldCallConventionDirective().isEmpty()) {
            var txt = ctx.callConvention().getText().toLowerCase();
            if (txt.startsWith("far")) return new Far(SourcePosition.of(ctx));
            if (txt.startsWith("local")) return new Local(SourcePosition.of(ctx));
            if (txt.startsWith("near")) return new Near(SourcePosition.of(ctx));
            throw AstParseError.unExpected();
        }
        if (ctx.dispIDDirective() != null && !ctx.dispIDDirective().isEmpty()) {
            return new DispID(ExpressionAst.of(ctx.dispIDDirective().expression()), SourcePosition.of(ctx));
        }
        if (ctx.hintingDirective() != null) {
            if (ctx.hintingDirective().DEPRECATED() != null && !ctx.hintingDirective().DEPRECATED().getText().isEmpty()) {
                var str =
                    ctx.hintingDirective().stringFactor() != null &&
                        !ctx.hintingDirective().stringFactor().isEmpty()
                        ? Optional.of(ctx.hintingDirective().stringFactor().getText())
                        : Optional.<String>empty();
                return new Depricated(str, SourcePosition.of(ctx));
            }

            if (ctx.hintingDirective().EXPERIMENTAL() != null && !ctx.hintingDirective().EXPERIMENTAL().getText().isEmpty())
                return new Experimental(SourcePosition.of(ctx));

            if (ctx.hintingDirective().PLATFORM() != null && !ctx.hintingDirective().PLATFORM().getText().isEmpty())
                return new Platform(SourcePosition.of(ctx));

            if (ctx.hintingDirective().LIBRARY() != null && !ctx.hintingDirective().LIBRARY().getText().isEmpty())
                return new Library(SourcePosition.of(ctx));
        }
        throw AstParseError.unExpected(ctx);
    }
}
