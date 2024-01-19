package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.*;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiLexer;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public record TokenizedFile(Source source, ImList<Token> tokens) {
    public static TokenizedFile parse(Source source){
        if( source==null )throw new IllegalArgumentException("source==null");

        var charStream = CharStreams.fromString(source.text(), source.sourceName());
        var lexer = new DelphiLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        tokenStream.fill();

        return new TokenizedFile(source, ImList.of(tokenStream.getTokens()));
    }

    public static TokenizedFile parse(String source, String sourceName) {
        if( source==null )throw new IllegalArgumentException("source==null");
        if( sourceName==null )throw new IllegalArgumentException("sourceName==null");
        return parse(new Source.Text(sourceName, source));
    }

    //TODO кодировка utf-8 bom портит все, надо вырезать первые BOM байты
    public static TokenizedFile parse(Path path, Charset charset) {
        if( path==null )throw new IllegalArgumentException("path==null");
        if( charset==null )throw new IllegalArgumentException("charset==null");
        return parse(new Source.File(path, charset));
    }

    public TokenStream toTokenStream() {
        var listTokenSource = new ListTokenSource(tokens.toList());
        return new CommonTokenStream(listTokenSource);
    }

    public sealed interface Source {
        String sourceName();
        String text();
        record File(Path path, Charset charset) implements Source {
            @Override
            public String text() {
                try {
                    return Files.readString(path, charset);
                } catch (IOException e) {
                    throw new IOError(e);
                }
            }

            @Override
            public String sourceName() {
                return path.getFileName().toString();
            }
        }
        record Text(String sourceName, String text) implements Source {}
    }
}
