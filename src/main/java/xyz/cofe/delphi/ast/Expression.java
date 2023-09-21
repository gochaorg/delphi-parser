package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public interface Expression extends AstNode {
    @Override
    Expression astUpdate(AstUpdate.UpdateContext ctx);

    static Expression of(DelphiParser.ExpressionContext exp){
        throw AstParseError.notImplemented();
    }
}
