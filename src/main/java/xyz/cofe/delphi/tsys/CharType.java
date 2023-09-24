package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Символ строки
 */
public sealed interface CharType extends NamesOfType {
    public static ImList<CharType,?> values = ImListLinked.of(
        new Char(),
        new AnsiChar(),
        new WideChar(),
        new UCS2Char(),
        new UCS4Char()
    );

    public record Char() implements CharType {
        private static String[] names = new String[]{ "Char" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record AnsiChar() implements CharType {
        private static String[] names = new String[]{ "AnsiChar" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record WideChar() implements CharType {
        private static String[] names = new String[]{ "WideChar" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record UCS2Char() implements CharType {
        private static String[] names = new String[]{ "UCS2Char" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record UCS4Char() implements CharType {
        private static String[] names = new String[]{ "UCS4Char" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
