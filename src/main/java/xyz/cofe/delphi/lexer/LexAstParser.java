package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.Token;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiLexer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Препроцессор параметров компиляции
 */
public class LexAstParser {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ImList<LexAst> parse(Iterable<Token> tokens) {
        if (tokens == null) throw new IllegalArgumentException("nodes==null");

        var result = new ImList[]{ImList.of()};
        TokenVisitor tokenVisitor = new DefaultVisitor(node -> {
            result[0] = (ImList) result[0].prepend(node);
        });

        for (var token : tokens) {
            tokenVisitor = tokenVisitor.visit(token);
        }

        return result[0].reverse();
    }

    public ImList<LexAst> parse(TokenizedFile tokenizedFile) {
        if (tokenizedFile == null) throw new IllegalArgumentException("tokenizedFile==null");
        return parse(tokenizedFile.tokens());
    }

    //region Conditional patterns
    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern DEFINE_Pattern = Pattern.compile("(?is)\\{\\$DEFINE\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> DEFINE(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = DEFINE_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern UNDEF_Pattern = Pattern.compile("(?is)\\{\\$UNDEF\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> UNDEF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = UNDEF_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern IFDEF_Pattern = Pattern.compile("(?is)\\{\\$IFDEF\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> IFDEF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = IFDEF_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern IFNDEF_Pattern = Pattern.compile("(?is)\\{\\$IFNDEF\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> IFNDEF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = IFNDEF_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern IF_Pattern = Pattern.compile("(?is)\\{\\$IF\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> IF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = IF_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern IFOPT_Pattern = Pattern.compile("(?is)\\{\\$IFOPT\\s+(?<exp>[^\\s]+)\\s*\\}.*");

    private static Optional<String> IFOPT(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = IFOPT_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern ELSEIF_Pattern = Pattern.compile("(?is)\\{\\$ELSEIF\\s+(?<exp>[^\\s]+)\\s*\\}");

    private static Optional<String> ELSEIF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return Optional.empty();

        var m = ELSEIF_Pattern.matcher(token.getText());
        if (!m.matches()) return Optional.empty();

        return Optional.of(m.group("exp"));
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern ELSE_Pattern = Pattern.compile("(?is)\\{\\$ELSE\\s*\\}.*");

    private static boolean ELSE(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return false;

        var m = ELSE_Pattern.matcher(token.getText());
        return m.matches();
    }

    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern ENDIF_Pattern = Pattern.compile("(?is)\\{\\$(ENDIF|IFEND)\\s*\\}.*");

    private static boolean ENDIF(Token token) {
        if (token == null) throw new IllegalArgumentException("token==null");
        if (token.getType() != DelphiLexer.COMMENT) return false;

        var m = ENDIF_Pattern.matcher(token.getText());
        return m.matches();
    }
    //endregion

    public interface TokenVisitor {
        TokenVisitor visit(Token token);
    }

    private static boolean singleTokenStatement(Consumer<LexAst> result, Token token) {
        var def = DEFINE(token);
        if (def.isPresent()) {
            result.accept(new LexAst.Define(def.get()));
            return true;
        }

        var undef = UNDEF(token);
        if (undef.isPresent()) {
            result.accept(new LexAst.UnDef(undef.get()));
            return true;
        }

        return false;
    }

    public static class DefaultVisitor implements TokenVisitor {
        private final Consumer<LexAst> result;

        public DefaultVisitor(Consumer<LexAst> result) {
            if (result == null) throw new IllegalArgumentException("result==null");
            this.result = result;
        }

        @Override
        public TokenVisitor visit(Token token) {
            if (token == null) throw new IllegalArgumentException("token==null");

            if (singleTokenStatement(result, token)) return this;

            Optional<TokenVisitor> ifopt_vis = IFOPT(token).map(exp -> new ConditionVisitor(this, result, new LexAst.Condition.IfOpt(exp), token));
            Optional<TokenVisitor> ifdef_vis = IFDEF(token).map(exp -> new ConditionVisitor(this, result, new LexAst.Condition.IfDef(exp), token));
            Optional<TokenVisitor> ifndef_vis = IFNDEF(token).map(exp -> new ConditionVisitor(this, result, new LexAst.Condition.IfNotDef(exp), token));
            Optional<TokenVisitor> if_vis = IF(token).map(exp -> new ConditionVisitor(this, result, new LexAst.Condition.If(exp), token));

            return ifdef_vis.or(() -> if_vis).or(() -> ifndef_vis).or(() -> ifopt_vis).orElse(this);
        }
    }

    public static class ConditionVisitor implements TokenVisitor {
        private final TokenVisitor parent;
        private final LexAst.Condition condition;

        private final List<LexAst> positive = new ArrayList<>();
        private final List<LexAst> negative = new ArrayList<>();
        private final Token begin;
        private List<LexAst> consumerList = positive;
        private final Consumer<LexAst> result;

        public ConditionVisitor(TokenVisitor parent, Consumer<LexAst> result, LexAst.Condition condition, Token begin) {
            if (parent == null) throw new IllegalArgumentException("parent==null");
            if (condition == null) throw new IllegalArgumentException("condition==null");
            if (begin == null) throw new IllegalArgumentException("begin==null");
            if (result == null) throw new IllegalArgumentException("result==null");
            this.parent = parent;
            this.condition = condition;
            this.begin = begin;
            this.result = result;
        }

        @Override
        public TokenVisitor visit(Token token) {
            if (token == null) throw new IllegalArgumentException("token==null");

            if (singleTokenStatement(result, token)) return this;

            TokenVisitor ret = this;

            if (ENDIF(token)) {
                result.accept(new LexAst.ConditionalCompile(condition, new LexAst.Lexs(ImList.of(positive)), new LexAst.Lexs(ImList.of(negative)), begin, token));
                ret = parent;
            } else if (ELSE(token)) {
                consumerList = negative;
            } else {
                var elseIf = ELSEIF(token);

                var ifdef = IFDEF(token);
                var ifndef = IFNDEF(token);
                var If = IF(token);
                var IfOpt = IFOPT(token);

                if (elseIf.isPresent()) {
                    consumerList = negative;
                    ret = new ConditionVisitor(
                        parent,
                        lexAst -> {
                            consumerList.add(lexAst);
                            result.accept(new LexAst.ConditionalCompile(condition, new LexAst.Lexs(ImList.of(positive)), new LexAst.Lexs(ImList.of(negative)), begin, token));
                        },
                        new LexAst.Condition.If(elseIf.get()),
                        token
                    );
                } else if (ifdef.isPresent()) {
                    ret = new ConditionVisitor(this, lexAst -> consumerList.add(lexAst), new LexAst.Condition.IfDef(ifdef.get()), token);
                } else if (ifndef.isPresent()) {
                    ret = new ConditionVisitor(this, lexAst -> consumerList.add(lexAst), new LexAst.Condition.IfNotDef(ifndef.get()), token);
                } else if (If.isPresent()) {
                    ret = new ConditionVisitor(this, lexAst -> consumerList.add(lexAst), new LexAst.Condition.If(If.get()), token);
                } else if (IfOpt.isPresent()) {
                    ret = new ConditionVisitor(this, lexAst -> consumerList.add(lexAst), new LexAst.Condition.IfOpt(IfOpt.get()), token);
                } else {
                    consumerList.add(new LexAst.Lex(token));
                }
            }

            return ret;
        }
    }
}
