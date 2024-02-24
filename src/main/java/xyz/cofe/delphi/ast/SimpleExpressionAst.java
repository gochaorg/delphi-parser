package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public sealed interface SimpleExpressionAst extends ExpressionAst {
    /**
     * Имя оператора
     */
    public enum OpName {
        /**
         * '@'
         */
        RefOf,

        /**
         * '^'
         */
        Deref,

        /**
         * 'not'
         */
        Not,

        /**
         * '+'
         */
        Plus,

        /**
         * '-'
         */
        Minus,

        /**
         * '*'
         */
        Mul,

        /**
         * '/'
         */
        FloatDiv,

        /**
         * 'div'
         */
        IntegerDiv,

        /**
         * 'mod'
         */
        Mod,

        /**
         * 'and'
         */
        And,

        /**
         * 'shl'
         */
        SHL,

        /**
         * 'shr'
         */
        SHR,

        /**
         * 'as'
         */
        As,

        /**
         * 'or'
         */
        Or,

        /**
         * 'xor'
         */
        Xor,

        /**
         * '='
         */
        Equals,

        /**
         * '&lt;&gt;'
         */
        NotEquals,

        /**
         * '&lt;'
         */
        Less,

        /**
         * '&gt;'
         */
        More,

        /**
         * '&lt;='
         */
        LessOrEquals,

        /**
         * '&gt;='
         */
        MoreOrEquals,

        /**
         * 'in'
         */
        In,

        /**
         * 'is'
         */
        Is;

        public static Optional<OpName> of(String src) {
            if (src == null) throw new IllegalArgumentException("src==null");
            return Optional.ofNullable(switch (src.toLowerCase()) {
                case "@" -> OpName.RefOf;
                case "^" -> OpName.Deref;
                case "not" -> OpName.Not;
                case "+" -> OpName.Plus;
                case "-" -> OpName.Minus;
                case "*" -> OpName.Mul;
                case "/" -> OpName.FloatDiv;
                case "div" -> OpName.IntegerDiv;
                case "mod" -> OpName.Mod;
                case "and" -> OpName.And;
                case "shl" -> OpName.SHL;
                case "shr" -> OpName.SHR;
                case "as" -> OpName.As;
                case "or" -> OpName.Or;
                case "xor" -> OpName.Xor;
                case "=" -> OpName.Equals;
                case "<>" -> OpName.NotEquals;
                case "<" -> OpName.Less;
                case ">" -> OpName.More;
                case "<=" -> OpName.LessOrEquals;
                case ">=" -> OpName.MoreOrEquals;
                case "in" -> OpName.In;
                case "is" -> OpName.Is;
                default -> null;
            });
        }
    }

    record Operator(OpName opName, SourcePosition position)
        implements SrcPos,
                   AstNode {
    }

    record Binary(Operator op, ExpressionAst left, ExpressionAst right, SourcePosition position)
        implements SimpleExpressionAst,
                   SrcPos,
                   AstNode {
        public static ExpressionAst of(DelphiParser.RelOpContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            if (ctx.op != null && ctx.left != null && ctx.right != null) {
                var opNameOpt = OpName.of(ctx.op.getText());
                if (opNameOpt.isPresent()) {
                    return new Binary(
                        new Operator(opNameOpt.get(), SourcePosition.of(ctx)),
                        of(ctx.left),
                        ExpressionAst.of(ctx.right),
                        SourcePosition.of(ctx)
                    );
                }
            }

            if (ctx.left != null) {
                return of(ctx.left);
            }

            throw AstParseError.unExpected(ctx);
        }

        public static ExpressionAst of(DelphiParser.SumOpContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            if (ctx.op != null && ctx.left != null && ctx.right != null) {
                var opNameOpt = OpName.of(ctx.op.getText());
                if (opNameOpt.isPresent()) {
                    return new Binary(
                        new Operator(opNameOpt.get(), SourcePosition.of(ctx)),
                        of(ctx.left),
                        ExpressionAst.of(ctx.right),
                        SourcePosition.of(ctx)
                    );
                }
            }

            if (ctx.left != null) {
                return of(ctx.left);
            }

            throw AstParseError.unExpected(ctx);
        }

        public static ExpressionAst of(DelphiParser.MulOpContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            if (ctx.op != null && ctx.left != null && ctx.right != null) {
                var opNameOpt = OpName.of(ctx.op.getText());
                if (opNameOpt.isPresent()) {
                    return new Binary(
                        new Operator(opNameOpt.get(), SourcePosition.of(ctx)),
                        Unary.of(ctx.left),
                        ExpressionAst.of(ctx.right),
                        SourcePosition.of(ctx)
                    );
                }
            }

            if (ctx.left != null) {
                return Unary.of(ctx.left);
            }

            throw AstParseError.unExpected(ctx);
        }
    }

    record Unary(Operator op, ExpressionAst expression, SourcePosition position)
        implements SimpleExpressionAst,
                   SrcPos {
        public static ExpressionAst of(DelphiParser.UnaryOpContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.op != null && !ctx.op.getText().isEmpty()
                && ctx.expression() != null && !ctx.expression().isEmpty()
            ) {
                var opNameOpt = OpName.of(ctx.op.getText());
                if (opNameOpt.isPresent()) {
                    return new Unary(
                        new Operator(opNameOpt.get(), SourcePosition.of(ctx)),
                        ExpressionAst.of(ctx.expression()),
                        SourcePosition.of(ctx)
                    );
                }
            }

            if (ctx.atom() != null && !ctx.atom().isEmpty()) {
                return AtomAst.of(ctx.atom());
            }

            throw AstParseError.unExpected(ctx);
        }
    }

    public static ExpressionAst of(DelphiParser.SimpleExpressionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.relOp() != null) {
            return Binary.of(ctx.relOp());
        }
        throw AstParseError.unExpected(ctx);
    }
}
