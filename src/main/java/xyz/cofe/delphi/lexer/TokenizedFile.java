package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.*;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.delphi.parser.DelphiParser;

import java.io.IOError;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

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
        return parse(path,charset,false);
    }

    public static TokenizedFile parse(Path path, Charset charset, boolean checkUtf8BOM) {
        if( path==null )throw new IllegalArgumentException("path==null");
        if( charset==null )throw new IllegalArgumentException("charset==null");
        return parse(new Source.File(path, charset, checkUtf8BOM));
    }

    public TokenStream toTokenStream() {
        var listTokenSource = new ListTokenSource(tokens.toList());
        return new CommonTokenStream(listTokenSource);
    }

    public DelphiParser toDelphiParser(){
        return new DelphiParser(toTokenStream());
    }

    public sealed interface Source {
        String sourceName();
        String text();
        record File(Path path, Charset charset, boolean checkUtf8BOM) implements Source {
            @Override
            public String text() {
                try {
                    boolean hasUtf8bom = false;

                    if( checkUtf8BOM ) {
                        try (var strm = Files.newInputStream(path)) {
                            byte[] buff = new byte[10];
                            var reads = strm.read(buff);
                            if (reads > 2) {
                                int b0 = buff[0] & 0xFF;
                                int b1 = buff[1] & 0xFF;
                                int b2 = buff[2] & 0xFF;
                                hasUtf8bom = b0 == 0xEF && b1 == 0xBB && b2 == 0xBF;
                            }
                        }
                    }

                    if( checkUtf8BOM && hasUtf8bom ){
                        var bytes = Files.readAllBytes(path);
                        if( bytes.length>3 ){
                            bytes = Arrays.copyOfRange(bytes,3,bytes.length);
                        }

                        return new String(bytes, StandardCharsets.UTF_8);
                    }

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
