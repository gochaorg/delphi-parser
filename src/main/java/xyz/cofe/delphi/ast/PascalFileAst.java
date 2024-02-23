package xyz.cofe.delphi.ast;

import java.util.BitSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.lexer.PreProcState;
import xyz.cofe.delphi.lexer.PreProcessor;
import xyz.cofe.delphi.lexer.TokenizedFile;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Некий pascal файл
 */
public sealed interface PascalFileAst {
    record Program() implements PascalFileAst {
    }

    record Library() implements PascalFileAst {
    }

    /**
     * Единица компиляции
     *
     * @param name     имя модуля
     * @param api      Экспортируемое api
     * @param position Позиция в исходниках
     * @param comments Комментарии
     */
    record Unit(
        ImList<String> name,
        UnitInterface api,
        SourcePosition position,
        ImList<Comment> comments
    ) implements PascalFileAst, Commented<Unit> {
        @Override
        public Unit withComments(ImList<Comment> comments) {
            return new Unit(name, api, position, comments);
        }

        @Override
        public String toString() {
            return "unit " + name + "\n" +
                "interface:\n" +
                indent("  ", api.toString());
        }
    }

    /**
     * Экспортируемое api
     *
     * @param uses         какие используются модули
     * @param declarations Объявляемое API
     */
    record UnitInterface(
        ImList<NamespaceAst> uses,
        ImList<InterfaceDecl> declarations
    ) {
        static UnitInterface of(DelphiParser.UnitInterfaceContext unt) {
            if (unt == null) throw new IllegalArgumentException("unt==null");

            ImList<NamespaceAst> uses =
                unt.usesClause() == null
                    ? ImList.of()
                    : unt.usesClause().namespaceNameList() == null
                    ? ImList.of()
                    : unt.usesClause().namespaceNameList().namespaceName() == null
                    ? ImList.of()
                    : ImList.of(unt.usesClause().namespaceNameList().namespaceName().stream().map(NamespaceAst::of).collect(Collectors.toList()));

            return new UnitInterface(
                uses,
                InterfaceDecl.of(unt.interfaceDecl())
            );
        }

        @Override
        public String toString() {
            var sb = new StringBuilder();

            sb.append("uses:\n");
            uses.each(n -> sb.append("  ").append(n).append("\n"));

            sb.append("declarations:\n");
            declarations.each(d -> sb.append(indent("  ", d.toString())).append("\n"));

            return sb.toString();
        }
    }

    record Package() implements PascalFileAst {
    }

    static PascalFileAst parse(String source, String sourceName) throws AstParseError {
        return parse(source, sourceName, false);
    }

    static PascalFileAst parse(String source, String sourceName, boolean injectComments) throws AstParseError {
        if (source == null) throw new IllegalArgumentException("source==null");
        if (sourceName == null) throw new IllegalArgumentException("sourceName==null");

        return parse(TokenizedFile.parse(source, sourceName), injectComments);
    }

    static PascalFileAst parse(TokenizedFile sourceFile) throws AstParseError {
        return parse(sourceFile, false);
    }

    static PascalFileAst parse(TokenizedFile sourceFile, boolean injectComments) throws AstParseError {
        return parse(sourceFile, injectComments, new PreProcState());
    }

    public sealed interface ParseErrors {
        record Syntax(RecognitionException err, int line, int charPositionInLine, String message) implements ParseErrors {}
        record Ambiguity(int startIndex,int stopIndex) implements ParseErrors {}
        record AttemptingFullContext(int startIndex,int stopIndex) implements ParseErrors {}
        record ContextSensitivity(int startIndex,int stopIndex) implements ParseErrors {}
    }

    static PascalFileAst parse(TokenizedFile sourceFile, boolean injectComments, PreProcState initialState) throws AstParseError {
        return parse(sourceFile,injectComments,initialState,null);
    }

    static PascalFileAst parse(TokenizedFile sourceFile, boolean injectComments, PreProcState initialState, Consumer<ParseErrors> errors) throws AstParseError {
        if (sourceFile == null) throw new IllegalArgumentException("sourceFile==null");
        if (initialState == null) throw new IllegalArgumentException("initialState==null");

        var preProc = new PreProcessor(initialState);
        var preProcTokFiles = preProc.process(sourceFile);

        var parser = new DelphiParser(preProcTokFiles.toTokenStream());

        if( errors!=null) {
            parser.addErrorListener(new ANTLRErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                    errors.accept(new ParseErrors.Syntax(e,line,charPositionInLine,msg));
                }

                @Override
                public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.Ambiguity(startIndex,stopIndex));
                }

                @Override
                public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.AttemptingFullContext(startIndex,stopIndex));
                }

                @Override
                public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.ContextSensitivity(startIndex,stopIndex));
                }
            });
        }

        var file = parser.file();

        var prg = file.program();
        if (prg != null && !prg.isEmpty()) return new Program();

        var lib = file.library();
        if (lib != null && !lib.isEmpty()) return new Library();

        var unt = file.unit();

        var comments = sourceFile.tokens().filter(t -> t.getType() == DelphiLexer.COMMENT).map(t -> new Comment(t.getText(), SourcePosition.of(t)));

        if (unt != null && !unt.isEmpty()) {
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

            if (injectComments) {
                unit = new CommentInjector().inject(unit);
            }

            return unit;
        }

        var pkg = file.packageE();
        if (pkg != null && !pkg.isEmpty()) {
            return new Package();
        }

        throw new AstParseError("!!");
    }
}
