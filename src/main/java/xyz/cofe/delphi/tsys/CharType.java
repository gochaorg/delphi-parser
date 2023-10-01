package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Символ строки
 */
public sealed interface CharType extends NamedType, SimpleType {
    public static final Char charType = new Char();
    public static final AnsiChar ansiCharType = new AnsiChar();
    public static final WideChar wideCharType = new WideChar();
    public static final UCS2Char ucs2CharType = new UCS2Char();
    public static final UCS4Char ucs4CharType = new UCS4Char();

    public static ImList<CharType,?> values = ImListLinked.of(
        charType,
        ansiCharType,
        wideCharType,
        ucs2CharType,
        ucs4CharType
    );

    public static final class Char implements CharType {
        private Char() {}
        private static TypeName name = new TypeName("Char");

        @Override
        public TypeName name() {
            return name;
        }
    }
    public static final class AnsiChar implements CharType {
        private AnsiChar() {}
        private static TypeName name = new TypeName("AnsiChar");
        @Override
        public TypeName name() { return name; }
    }
    public static final class WideChar implements CharType {
        private WideChar() {}
        private static TypeName name = new TypeName("WideChar");
        @Override public TypeName name() { return name; }
    }
    public static final class UCS2Char implements CharType {
        private UCS2Char() {}
        private static TypeName name = new TypeName("UCS2Char");
        @Override public TypeName name() { return name; }
    }
    public static final class UCS4Char implements CharType {
        private UCS4Char() {}
        private static TypeName name = new TypeName("UCS4Char");
        @Override public TypeName name() { return name; }
    }
}
