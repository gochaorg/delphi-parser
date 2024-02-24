package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Выражение
 */
public interface ExpressionAst extends AstNode,
                                       SrcPos {
    /**
     * Заглушка
     *
     * @param text     исходный текст
     * @param position позиция в исходниках
     */
    record Stub(String text, SourcePosition position) implements ExpressionAst,
                                                                 SrcPos {
    }

    static ExpressionAst of(DelphiParser.ExpressionContext exp) {
        if (exp == null) throw new IllegalArgumentException("exp==null");
        return new Stub(exp.getText(), SourcePosition.of(exp));
    }
}
