package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public interface ExpressionAst extends AstNode, SrcPos {
    public static record Stub(String text, SourcePosition position) implements ExpressionAst, SrcPos {
        @Override
        public ExpressionAst astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    @Override
    ExpressionAst astUpdate(AstUpdate.UpdateContext ctx);

    String text();

    static ExpressionAst of(DelphiParser.ExpressionContext exp){
        if( exp==null ) throw new IllegalArgumentException("exp==null");
        return new Stub(exp.getText(), SourcePosition.of(exp));
    }
}
