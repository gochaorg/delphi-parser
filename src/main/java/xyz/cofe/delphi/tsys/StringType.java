package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Optional;

/**
 * https://docwiki.embarcadero.com/RADStudio/Alexandria/en/String_Types_(Delphi)
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalAssignedToNull", "FieldCanBeLocal"})
public sealed interface StringType extends SimpleType {
    public sealed interface NamedStringType extends StringType, NamedType {}

    public sealed interface ShortString {}
    public static final class ShortStringUnsized implements StringType, ShortString, NamedType, NamedStringType {
        private ShortStringUnsized() {}

        @Override
        public String name() {
            return "ShortString";
        }
    }
    public static final class ShortStringSized implements StringType, ShortString {
        public final long maxLength;

        private ShortStringSized(long maxLength) {
            this.maxLength = maxLength;
        }
    }
    public static final ShortStringUnsized shortStringWithOutLengthType = new ShortStringUnsized();
    public static ShortString shortString(Optional<Long> len){
        if( len==null ) throw new IllegalArgumentException("len==null");
        return len.isEmpty() ? StringType.shortStringWithOutLengthType : new ShortStringSized(len.get());
    }

    public sealed interface AnsiString {}
    public static final class AnsiStringWOCodePage implements StringType, AnsiString, NamedType, NamedStringType {
        private AnsiStringWOCodePage() {}

        @Override
        public String name() {
            return "AnsiString";
        }
    }
    public static final class AnsiStringWithCodePage implements StringType, AnsiString {
        public final String codePage;
        private AnsiStringWithCodePage(String codePage) {
            if (codePage == null) throw new IllegalArgumentException("codePage==null");
            this.codePage = codePage;
        }
    }
    public static final AnsiStringWOCodePage ansiStringWithOutCodePageType = new AnsiStringWOCodePage();
    public static AnsiString ansiString(Optional<String> codePage){
        if( codePage==null ) throw new IllegalArgumentException("codePage==null");
        return codePage.isEmpty() ? StringType.ansiStringWithOutCodePageType : new AnsiStringWithCodePage(codePage.get());
    }

    public sealed interface UnicodeString {}
    public static final class UnicodeStringUnsized implements StringType, UnicodeString, NamedType, NamedStringType {
        private UnicodeStringUnsized() {}

        @Override
        public String name() {
            return "UnicodeString";
        }
    }
    public static final class UnicodeStringSized implements StringType, UnicodeString {
        public final Long maxLength;

        private UnicodeStringSized(long maxLength) {
            this.maxLength = maxLength;
        }
    }
    public static final UnicodeStringUnsized unicodeStringWithOutLengthType = new UnicodeStringUnsized();
    public static UnicodeString unicodeString(Optional<Long> len){
        if( len==null ) throw new IllegalArgumentException("len==null");
        return len.isEmpty() ? StringType.unicodeStringWithOutLengthType : new UnicodeStringSized(len.get());
    }

    public sealed interface WideString {}
    public static final class WideStringUnsized implements StringType, WideString, NamedType, NamedStringType {
        private WideStringUnsized() {}

        @Override
        public String name() {
            return "WideString";
        }
    }
    public static final class WideStringSized implements StringType, WideString {
        public final long maxLength;

        private WideStringSized(long maxLength) {
            this.maxLength = maxLength;
        }
    }
    public static final WideStringUnsized wideStringWithOutLengthType = new WideStringUnsized();
    public static WideString wideString(Optional<Long> len){
        if( len==null ) throw new IllegalArgumentException("len==null");
        return len.isEmpty() ? StringType.wideStringWithOutLengthType : new WideStringSized(len.get());
    }

    public sealed interface STRING {}
    public static final class StringSized implements StringType, STRING {
        public final long maxLength;

        public StringSized(long maxLength) {
            this.maxLength = maxLength;
        }
    }
    public static final class StringUnsized implements StringType, STRING, NamedType, NamedStringType {
        private StringUnsized() {}

        @Override
        public String name() {
            return "String";
        }
    }

    public static final StringUnsized stringWithOutLengthType = new StringUnsized();
    public static STRING string(Optional<Long> len){
        if( len==null ) throw new IllegalArgumentException("len==null");
        return len.isEmpty() ? StringType.stringWithOutLengthType : new StringSized(len.get());
    }

    public static final class PChar implements StringType, NamedType, NamedStringType {
        private PChar() {
        }

        @Override
        public String name() {
            return "PChar";
        }
    }
    public static final PChar pchar = new PChar();

    public static final class PWideChar implements StringType, NamedType, NamedStringType {
        private PWideChar() {
        }

        @Override
        public String name() {
            return "PWideChar";
        }
    }
    public static final PWideChar pwidechar = new PWideChar();

    public static ImList<NamedStringType,?> namedValues = ImListLinked.of(pchar, pwidechar, stringWithOutLengthType, wideStringWithOutLengthType, unicodeStringWithOutLengthType, ansiStringWithOutCodePageType, shortStringWithOutLengthType);
}
