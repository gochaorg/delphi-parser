package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public interface Expression {
    static Expression of(DelphiParser.ExpressionContext exp){
        throw AstParseError.notImplemented();
    }
}
