package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;

/**
 * Символ строки
 */
@SuppressWarnings("UnnecessaryModifier")
public sealed interface CharType extends NamedType, SimpleType {
    /** Символ, возможно unicode */
    public static final Char charType = new Char();

    /** Символ 1 байт */
    public static final AnsiChar ansiCharType = new AnsiChar();

    /**
     * Символ 2 байта, наверно unicode
     */
    public static final WideChar wideCharType = new WideChar();

    /**
     * Символ 2 байта
     */
    public static final UCS2Char ucs2CharType = new UCS2Char();

    /**
     * Символ 4 байта
     */
    public static final UCS4Char ucs4CharType = new UCS4Char();

    /**
     * Какие могут быть символы (типы)
     */
    public static ImList<CharType> values = ImListLinked.of(
        charType,
        ansiCharType,
        wideCharType,
        ucs2CharType,
        ucs4CharType
    );

    //region Char
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("Char")
    public static final class Char implements CharType {
        private Char() {}
        private static final TypeName name = TypeName.of("Char");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region AnsiChar
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("AnsiChar")
    public static final class AnsiChar implements CharType {
        private AnsiChar() {}
        private static final TypeName name = TypeName.of("AnsiChar");
        @Override
        public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region WideChar
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("WideChar")
    public static final class WideChar implements CharType {
        private WideChar() {}
        private static final TypeName name = TypeName.of("WideChar");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region UCS2Char
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("UCS2Char")
    public static final class UCS2Char implements CharType {
        private UCS2Char() {}
        private static final TypeName name = TypeName.of("UCS2Char");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region UCS4Char
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("UCS4Char")
    public static final class UCS4Char implements CharType {
        private UCS4Char() {}
        private static final TypeName name = TypeName.of("UCS4Char");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
}
