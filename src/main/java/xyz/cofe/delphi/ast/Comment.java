package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.Token;

/**
 * Комментарий в исходном коде
 * @param text текст коментария
 * @param position позиция в исходном коде
 */
public record Comment(
    String text,
    SourcePosition position
) implements SrcPos {
    static Comment of(Token token){
        if( token==null ) throw new IllegalArgumentException("token==null");
        return new Comment(token.getText(), SourcePosition.of(token));
    }
}
