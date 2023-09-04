package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.*;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.delphi.parser.DelphiParser;

import java.io.StringReader;
import java.util.stream.Collectors;

public sealed interface PascalFile {
    public static record Program() implements PascalFile {}
    public static record Library() implements PascalFile {}
    public static record Unit(ImList<String,?> name) implements PascalFile {}
    public static record Package() implements PascalFile {}

    public static PascalFile parse( String source ) throws AstParseError {
        if( source==null )throw new IllegalArgumentException("source==null");

        var charStream = CharStreams.fromString(source);
        var lexer = new DelphiLexer(charStream);
        var tokenStream = new CommonTokenStream(lexer);
        var parser = new DelphiParser(tokenStream);
        var file = parser.file();

        var prg = file.program();
        if( prg!=null && !prg.isEmpty() )return new Program();

        var lib = file.library();
        if( lib!=null && !lib.isEmpty() )return new Library();

        var unt = file.unit();
        if( unt!=null && !unt.isEmpty() ) {
            var id = ImListLinked.of(unt.unitHead().namespaceName().ident().stream().map(RuleContext::getText).collect(Collectors.toList()));
            return new Unit(id);
        }

        var pkg = file.packageE();
        if( pkg!=null && !pkg.isEmpty() ){
            return new Package();
        }

        throw new AstParseError("!!");
    }
}
