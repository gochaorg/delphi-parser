package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public interface Expression extends AstNode {
    static Expression of(DelphiParser.ExpressionContext exp){
        throw AstParseError.notImplemented();
    }
}
