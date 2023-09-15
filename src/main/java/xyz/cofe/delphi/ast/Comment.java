package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.Token;

public record Comment(
    String text,
    SourcePosition position
) {
    static Comment of(Token token){
        if( token==null ) throw new IllegalArgumentException("token==null");
        return new Comment(token.getText(), SourcePosition.of(token));
    }
}
