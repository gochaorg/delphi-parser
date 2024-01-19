package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.Token;
import xyz.cofe.coll.im.ImList;

/**
 * Структуры на базе параметров компиляции
 */
public sealed interface LexAst {
    /**
     * Возвращает токены определенные этой структурой включая вложенные
     * @return токены
     */
    default ImList<Token> nestedTokens() { return ImList.of(); }

    /**
     * Условная компиляция
     * @param condition условие
     * @param positive Позитивное решение
     * @param negative Негативное решение
     * @param begin Начало структуры
     * @param end Конец структуры
     */
    record ConditionalCompile(Condition condition, LexAst positive, LexAst negative, Token begin, Token end) implements LexAst {
        @Override
        public ImList<Token> nestedTokens() {
            return ImList.of(begin).append(positive.nestedTokens()).append(negative.nestedTokens()).append(end);
        }
    }

    /**
     * Определение символа <code>$DEFINE</code> <i>name</i>
     * @param name символ
     */
    record Define(String name) implements LexAst {}

    /**
     * Определение символа <code>$DEFINE</code> <i>name</i>
     * @param name символ
     */
    record UnDef(String name) implements LexAst {}

    sealed interface Condition {
        /**
         * Условие наличия определения <code>$IFDEF</code> <i>name</i>
         * @param name символ
         */
        record IfDef(String name) implements Condition {}

        /**
         * Условие отсутствия определения <code>$IFNDEF</code> <i>name</i>
         * @param name <i>name</i>
         */
        record IfNotDef(String name) implements Condition {}

        /**
         * Наличие переключателя <code>$IFOPT</code> <i>name</i>
         * <pre>
         *  {$IFOPT R+}
         *    Writeln('Compiled with range-checking');
         *  {$ENDIF}
         * </pre>
         * @param name переключатель
         */
        record IfOpt(String name) implements Condition {}

        /**
         * Условие компиляции <code>$IF</code> <i>code</i>
         * @param code условие
         */
        record If(String code) implements Condition {}
    }

    record Lexs(ImList<LexAst> nodes) implements LexAst {
        @Override
        public ImList<Token> nestedTokens() {
            return nodes.fmap(LexAst::nestedTokens);
        }
    }

    record Lex(Token token) implements LexAst {
        @Override
        public ImList<Token> nestedTokens() {
            return ImList.of(token);
        }
    }
}
