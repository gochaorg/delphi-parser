package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public interface Expression extends AstNode, SrcPos {
    public static record Stub(String text, SourcePosition position) implements Expression, SrcPos {
        @Override
        public Expression astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    @Override
    Expression astUpdate(AstUpdate.UpdateContext ctx);

    String text();

    static Expression of(DelphiParser.ExpressionContext exp){
        if( exp==null ) throw new IllegalArgumentException("exp==null");
        return new Stub(exp.getText(), SourcePosition.of(exp));
    }
}
