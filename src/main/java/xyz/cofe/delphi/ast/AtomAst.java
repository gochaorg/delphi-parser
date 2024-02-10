package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;
import xyz.cofe.text.Text;

import java.util.Optional;

/**
 * Атомарное значение
 */
sealed public interface AtomAst permits AtomAst.BoolValue,
                                        AtomAst.DelphiSet,
                                        AtomAst.FloatNumber,
                                        AtomAst.IdentRef,
                                        AtomAst.InheritedContext,
                                        AtomAst.InheritedIdent,
                                        AtomAst.IntNumber,
                                        AtomAst.NestedExpression,
                                        AtomAst.NilValue,
                                        AtomAst.StringValue,
                                        PostfixAst {
    public static AtomAst of(DelphiParser.AtomContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        AtomAst base = null;
        if (ctx.intNum() != null && !ctx.intNum().isEmpty()) {
            base = IntNumber.of(ctx.intNum());
        } else if (ctx.realNum() != null && !ctx.realNum().isEmpty()) {
            base = FloatNumber.of(ctx.realNum());
        }
        if (ctx.stringFactor() != null && !ctx.stringFactor().isEmpty()) {
            base = StringValue.of(ctx.stringFactor());
        }
        if (ctx.preDefinedValues() != null && !ctx.preDefinedValues().isEmpty()) {
            var txt = ctx.preDefinedValues().getText().toLowerCase();
            base = switch (txt) {
                case "nil" -> new NilValue(SourcePosition.of(ctx));
                case "true" -> new BoolValue(true, SourcePosition.of(ctx));
                case "false" -> new BoolValue(false, SourcePosition.of(ctx));
                default -> throw AstParseError.unExpected(ctx);
            };
        }
        if (ctx.INHERITED() != null && !ctx.INHERITED().getText().isEmpty()) {
            if (ctx.identInAtom() != null && !ctx.identInAtom().isEmpty()) {
                return new InheritedIdent(ctx.identInAtom().getText(), SourcePosition.of(ctx));
            }
            base = new InheritedContext(SourcePosition.of(ctx));
        }
        if (ctx.expression() != null) {
            base = new NestedExpression(
                ExpressionAst.of(ctx.expression()),
                SourcePosition.of(ctx)
            );
        }
        if (ctx.setExpression() != null && !ctx.setExpression().isEmpty()) {
            base = DelphiSet.of(ctx.setExpression());
        }

        if (base == null) throw AstParseError.notImplemented(ctx);
        if (ctx.postfix() == null || ctx.postfix().isEmpty()) return base;

        for (var postfix : ctx.postfix()) {
            base = PostfixAst.build(postfix, base);
        }

        return base;
    }

    record NestedExpression(ExpressionAst expression, SourcePosition position) implements AtomAst,
                                                                                          SrcPos,
                                                                                          AstNode {
        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return ImList.of(expression);
        }
    }

    /**
     * Целое число
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record IntNumber(
        long value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos,
                 AstNode {
        public static IntNumber of(DelphiParser.IntNumContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.TkIntNum() != null && !ctx.TkIntNum().getText().isBlank()) {
                return new IntNumber(Long.parseLong(ctx.TkIntNum().getText()), SourcePosition.of(ctx));
            }
            if (ctx.TkHexNum() != null && !ctx.TkHexNum().getText().isBlank()) {
                return new IntNumber(Long.parseLong(
                    Text.trimStart(ctx.getText(), "$"),
                    16
                ), SourcePosition.of(ctx));
            }
            throw AstParseError.unExpected(ctx);
        }
    }

    /**
     * Плавающее число
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record FloatNumber(
        double value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos,
                 AstNode {
        public static FloatNumber of(DelphiParser.RealNumContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            return new FloatNumber(
                Double.parseDouble(ctx.getText()),
                SourcePosition.of(ctx)
            );
        }
    }

    /**
     * Текстовая строка
     *
     * @param value    декодированное значение
     * @param position позиция в исходниках
     */
    record StringValue(
        String value,
        SourcePosition position
    ) implements AtomAst,
                 SrcPos,
                 AstNode {
        private static final String hex = "0123456789abcdefABCDEF";
        private static final String dec = "0123456789";

        private static boolean isDigit(String src, int off, int base) {
            if (off >= src.length()) return false;
            if (off < 0) return false;
            char c = src.charAt(off);
            if (base == 16) {
                return hex.indexOf(c) >= 0;
            }
            return dec.indexOf(c) >= 0;
        }

        private static boolean isQuote(String src, int off) {
            if (off >= src.length()) return false;
            if (off < 0) return false;
            char c = src.charAt(off);
            return c == '\'';
        }

        public static Optional<String> decode(String src) {
            if (src == null) throw new IllegalArgumentException("src==null");

            StringBuilder sb = new StringBuilder();
            StringBuilder digits = new StringBuilder();

            String state = "start";

            for (var i = 0; i < src.length(); i++) {
                char chr = src.charAt(i);
                switch (state) {
                    case "start" -> {
                        switch (chr) {
                            case '#' -> {
                                state = "#";
                                digits.setLength(0);
                            }
                            case '\'' -> {
                                state = "str";
                            }
                            case ' ', '\n', '\r', '\t' -> {
                            }
                            default -> state = "err";
                        }
                    }
                    case "" -> {
                        switch (chr) {
                            case '#' -> {
                                state = "#";
                                digits.setLength(0);
                            }
                            case '\'' -> {
                                state = "str";
                            }
                            case ' ', '\n', '\r', '\t' -> {
                            }
                            default -> state = "stop";
                        }
                    }
                    case "#" -> {
                        switch (chr) {
                            case '$' -> {
                                state = "#$";
                            }
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
                                state = "#d";
                                digits.append(chr);
                            }
                        }
                    }
                    case "#$" -> {
                        if (isDigit(src, i, 16)) {
                            digits.append(chr);
                            if (!isDigit(src, i + 1, 16)) {
                                state = "";
                                int num = Integer.parseInt(digits.toString(), 16);
                                sb.append((char) num);
                            }
                        } else {
                            state = "err";
                        }
                    }
                    case "#d" -> {
                        if (isDigit(src, i, 10)) {
                            digits.append(chr);
                            if (!isDigit(src, i + 1, 10)) {
                                state = "";
                                int num = Integer.parseInt(digits.toString(), 10);
                                sb.append((char) num);
                            }
                        } else {
                            state = "err";
                        }
                    }
                    case "str" -> {
                        switch (chr) {
                            case '\'' -> {
                                if (isQuote(src, i + 1)) {
                                    state = "qstr";
                                } else {
                                    state = "";
                                }
                            }
                            default -> {
                                sb.append(chr);
                            }
                        }
                    }
                    case "qstr" -> {
                        sb.append("'");
                        state = "str";
                    }
                    case "err" -> {
                    }
                }
            }

            return
                state == "err" ?
                    Optional.empty() :
                    Optional.of(sb.toString());
        }

        public static StringValue of(DelphiParser.StringFactorContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            var src = new StringBuilder();
            for (var i = 0; i < ctx.getChildCount(); i++) {
                src.append(ctx.getChild(i).getText());
            }

            var str = decode(src.toString());
            if (str.isEmpty()) throw AstParseError.notImplemented(ctx);

            return new StringValue(
                str.get(),
                SourcePosition.of(ctx)
            );
        }
    }

    /**
     * Значение унаследованное, определяется контекстом
     *
     * @param position позиция в исходниках
     */
    record InheritedContext(SourcePosition position) implements AtomAst,
                                                                SrcPos,
                                                                AstNode {
    }

    /**
     * @param id
     * @param position
     */
    record InheritedIdent(String id, SourcePosition position) implements AtomAst,
                                                                         SrcPos,
                                                                         AstNode {
    }

    /**
     * Ссылка на идентификатор/переменную/метод
     *
     * @param id
     * @param position
     */
    record IdentRef(String id, SourcePosition position) implements AtomAst,
                                                                   SrcPos,
                                                                   AstNode {
    }

    /**
     * NULL ссылка
     *
     * @param position позиция в исходниках
     */
    record NilValue(SourcePosition position) implements AtomAst,
                                                        SrcPos,
                                                        AstNode {
    }

    /**
     * NULL ссылка
     *
     * @param position позиция в исходниках
     */
    record BoolValue(boolean value, SourcePosition position) implements
                                                             AtomAst,
                                                             SrcPos,
                                                             AstNode {
    }

    /**
     * множество
     */
    sealed interface DelphiSet extends SrcPos,
                                       AtomAst,
                                       AstNode {
        record One(ExpressionAst expression, SourcePosition position) implements DelphiSet {
            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImList.of(expression);
            }
        }

        record FromTo(ExpressionAst from, ExpressionAst to, SourcePosition position) implements DelphiSet {
            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImList.of(from, to);
            }
        }

        record Sequence(ImList<DelphiSet> items, SourcePosition position) implements DelphiSet {
            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return items;
            }
        }

        public static DelphiSet of(DelphiParser.SetExpressionContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");

            var state = "";
            var itms = ImList.<DelphiSet>of();

            ExpressionAst expr = null;
            SourcePosition exprStart = null;

            for (var ci = 0; ci < ctx.getChildCount(); ci++) {
                var ch = ctx.getChild(ci);
                switch (state) {
                    case "" -> {
                        if (ch instanceof TerminalNode tok && tok.getText().equals("[")) {
                            state = "[";
                        } else {
                            throw AstParseError.unExpected(ctx);
                        }
                    }
                    case "[" -> {
                        if (ch instanceof DelphiParser.ExpressionContext e) {
                            state = "[e";
                            expr = ExpressionAst.of(e);
                            exprStart = SourcePosition.of(e);
                        } else if (ch instanceof TerminalNode tok && tok.getText().equals(",")) {
                            state = "[";
                        } else if (ch instanceof TerminalNode tok && tok.getText().equals("]")) {
                            break;
                        } else {
                            throw AstParseError.unExpected(ctx);
                        }
                    }
                    case "[e" -> {
                        if (ch instanceof TerminalNode tok && tok.getText().equals("..")) {
                            state = "[e..";
                        } else if (ch instanceof TerminalNode tok && tok.getText().equals(",")) {
                            state = "[";
                            itms = itms.prepend(
                                new DelphiSet.One(expr, exprStart)
                            );
                            expr = null;
                        } else if (ch instanceof TerminalNode tok && tok.getText().equals("]")) {
                            //noinspection ConstantValue
                            if (expr != null && exprStart != null) {
                                itms = itms.prepend(
                                    new DelphiSet.One(expr, exprStart)
                                );
                            }
                        } else {
                            throw AstParseError.unExpected(ctx);
                        }
                    }
                    case "[e.." -> {
                        if (ch instanceof DelphiParser.ExpressionContext e) {
                            itms = itms.prepend(
                                new FromTo(
                                    expr,
                                    ExpressionAst.of(e),
                                    SourcePosition.mergeExtend(
                                        exprStart,
                                        SourcePosition.of(e)
                                    )
                                )
                            );
                            state = "[";
                            expr = null;
                        } else {
                            throw AstParseError.unExpected(ctx);
                        }
                    }
                    default -> {
                    }
                }
            }

            return new Sequence(
                itms.reverse(),
                SourcePosition.of(ctx)
            );
        }
    }

}
