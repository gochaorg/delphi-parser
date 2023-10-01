package xyz.cofe.delphi.ast;

import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.*;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Некий pascal файл
 */
public sealed interface PascalFileAst extends AstNode {
    record Program() implements PascalFileAst {}
    record Library() implements PascalFileAst {}

    record Unit(
        ImList<String,?> name,
        UnitInterface api,
        SourcePosition position,
        ImList<Comment,?> comments
    ) implements PascalFileAst, AstUpdate, Commented<Unit> {
        @Override
        public Unit withComments(ImList<Comment, ?> comments) {
            return new Unit(name,api,position,comments);
        }

        @Override
        public String toString() {
            return "unit "+name+"\n"+
                "interface:\n"+
                indent("  ",api.toString());
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return ImListLinked.of(api);
        }

        @Override
        public Unit astUpdate(AstUpdate.UpdateContext updateCtx) {
            var api = this.api.astUpdate(updateCtx);
            if( api==this.api )return this;

            return new Unit(name, api, position, comments);
        }
    }

    record UnitInterface(
        ImList<NamespaceAst,?> uses,
        ImList<InterfaceDecl,?> declarations
    ) implements AstNode
    {
        @Override
        public UnitInterface astUpdate(AstUpdate.UpdateContext ctx) {
            var changedNs = new boolean[]{ false };
            var ns = uses.foldRight(ImListLinked.<NamespaceAst>of(), (acc, it) -> {
                var it1 = it.astUpdate(ctx);
                //noinspection ConstantConditions
                if(it!=it1){
                    changedNs[0] = true;
                }
                return acc.prepend(it1);
            });

            var changedDecl = new boolean[]{ false };
            var decl = declarations.foldRight(ImListLinked.<InterfaceDecl>of(), (acc,it) -> {
                var it1 = it.astUpdate(ctx);
                if(it!=it1){
                    changedDecl[0] = true;
                }
                return acc.prepend(it1);
            });

            if( changedDecl[0] && changedNs[0] )return new UnitInterface(ns, decl);
            if( changedNs[0] )return new UnitInterface(ns, declarations);
            if( changedDecl[0] )return new UnitInterface(uses, decl);

            return this;
        }

        @Override
        public ImList<? extends AstNode, ?> nestedAstNodes() {
            return upcast(uses).append(upcast(declarations));
        }

        static UnitInterface of(DelphiParser.UnitInterfaceContext unt){
            var uses = ImListLinked.of(
                unt.usesClause().namespaceNameList().namespaceName().stream()
                    .map(NamespaceAst::of)
                    .collect(Collectors.toList())
            );

            return new UnitInterface(
                uses,
                InterfaceDecl.of(unt.interfaceDecl())
            );
        }

        @Override
        public String toString(){
            var sb = new StringBuilder();

            sb.append("uses:\n");
            uses.forEach(n -> sb.append("  ").append(n).append("\n"));

            sb.append("declarations:\n");
            declarations.forEach(d -> sb.append(indent("  ",d.toString())).append("\n") );

            return sb.toString();
        }
    }

    record Package() implements PascalFileAst {}

    static PascalFileAst parse(String source, String sourceName ) throws AstParseError {
        return parse(source,sourceName,false);
    }

    static PascalFileAst parse(String source, String sourceName, boolean injectComments ) throws AstParseError {
        if( source==null )throw new IllegalArgumentException("source==null");
        if( sourceName==null ) throw new IllegalArgumentException("sourceName==null");

        var charStream = CharStreams.fromString(source,sourceName);
        var lexer = new DelphiLexer(charStream);

        var tokenStream = new CommonTokenStream(lexer);

        var parser = new DelphiParser(tokenStream);
        var file = parser.file();

        var prg = file.program();
        if( prg!=null && !prg.isEmpty() )return new Program();

        var lib = file.library();
        if( lib!=null && !lib.isEmpty() )return new Library();

        var unt = file.unit();

        var comments = ImListLinked.of(
            (tokenStream.getTokens()!=null ? tokenStream.getTokens() : List.<Token>of())
                .stream()
                .filter(t -> t.getChannel()==1)
                .map(Comment::of)
                .toList());

        if( unt!=null && !unt.isEmpty() ) {
            var id = ImListLinked.of(
                unt.unitHead().namespaceName().ident().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList()));

            var unit = new Unit(
                id,
                UnitInterface.of(unt.unitInterface()),
                SourcePosition.of(unt),
                comments
            );

            if( injectComments ){
                unit = new CommentInjector().inject(unit);
            }

            return unit;
        }

        var pkg = file.packageE();
        if( pkg!=null && !pkg.isEmpty() ){
            return new Package();
        }

        throw new AstParseError("!!");
    }
}
