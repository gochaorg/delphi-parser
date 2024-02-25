package xyz.cofe.delphi.ast;

import java.util.BitSet;
import java.util.Optional;
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
public sealed interface PascalFileAst permits LibraryAst,
                                              PackageAst,
                                              ProgramAst,
                                              UnitAst {

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
        record Syntax(RecognitionException err, int line, int charPositionInLine,
                      String message) implements ParseErrors {
        }

        record Ambiguity(int startIndex, int stopIndex) implements ParseErrors {
        }

        record AttemptingFullContext(int startIndex, int stopIndex) implements ParseErrors {
        }

        record ContextSensitivity(int startIndex, int stopIndex) implements ParseErrors {
        }
    }

    static PascalFileAst parse(TokenizedFile sourceFile, boolean injectComments, PreProcState initialState) throws AstParseError {
        return parse(sourceFile, injectComments, initialState, null);
    }

    static PascalFileAst parse(TokenizedFile sourceFile, boolean injectComments, PreProcState initialState, Consumer<ParseErrors> errors) throws AstParseError {
        if (sourceFile == null) throw new IllegalArgumentException("sourceFile==null");
        if (initialState == null) throw new IllegalArgumentException("initialState==null");

        var preProc = new PreProcessor(initialState);
        var preProcTokFiles = preProc.process(sourceFile);

        var parser = new DelphiParser(preProcTokFiles.toTokenStream());

        if (errors != null) {
            parser.addErrorListener(new ANTLRErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                    errors.accept(new ParseErrors.Syntax(e, line, charPositionInLine, msg));
                }

                @Override
                public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.Ambiguity(startIndex, stopIndex));
                }

                @Override
                public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.AttemptingFullContext(startIndex, stopIndex));
                }

                @Override
                public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
                    errors.accept(new ParseErrors.ContextSensitivity(startIndex, stopIndex));
                }
            });
        }

        var file = parser.file();

        var prg = file.program();
        if (prg != null && !prg.isEmpty()) return new ProgramAst();

        var lib = file.library();
        if (lib != null && !lib.isEmpty()) return new LibraryAst();

        var unt = file.unit();

        var comments = sourceFile.tokens().filter(t -> t.getType() == DelphiLexer.COMMENT).map(t -> new Comment(t.getText(), SourcePosition.of(t)));

        if (unt != null && !unt.isEmpty()) {
            var id = ImListLinked.of(
                unt.unitHead().namespaceName().ident().stream()
                    .map(RuleContext::getText)
                    .collect(Collectors.toList()));

            var unit = new UnitAst(
                id,
                unt.unitHead().hintingDirective()!=null
                    ? ImList.of(unt.unitHead().hintingDirective()).map(FDirective::of)
                    : ImList.of(),
                UnitInterfaceAst.of(unt.unitInterface()),
                unt.unitImplementation()!=null
                    ? Optional.of(UnitImplementationAst.of(unt.unitImplementation()))
                    : Optional.empty(),
                UnitBlock.of(unt.unitBlock()),
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
            return new PackageAst();
        }

        throw new AstParseError("!!");
    }
}
