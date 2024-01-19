package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;

import java.util.Optional;

/**
 * https://docwiki.embarcadero.com/RADStudio/Alexandria/en/String_Types_(Delphi)
 */
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "OptionalAssignedToNull", "FieldCanBeLocal", "UnnecessaryModifier"})
public sealed interface StringType extends SimpleType {
    public sealed interface NamedStringType extends StringType, NamedType {}

    //region ShortString
    public sealed interface ShortString {}

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("ShortString")
    public static final class ShortStringUnsized implements StringType, ShortString, NamedType, NamedStringType {
        private ShortStringUnsized() {}

        private static final TypeName name = TypeName.of("ShortString");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
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
    //endregion
    //region AnsiString
    public sealed interface AnsiString {}

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("AnsiString")
    public static final class AnsiStringWOCodePage implements StringType, AnsiString, NamedType, NamedStringType {
        private AnsiStringWOCodePage() {}

        private static final TypeName name = TypeName.of("AnsiString");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
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
    //endregion
    //region UnicodeString
    public sealed interface UnicodeString {}

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("UnicodeString")
    public static final class UnicodeStringUnsized implements StringType, UnicodeString, NamedType, NamedStringType {
        private UnicodeStringUnsized() {}

        private static final TypeName name = TypeName.of("UnicodeString");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
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
    //endregion
    //region WideString
    public sealed interface WideString {}
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("WideString")
    public static final class WideStringUnsized implements StringType, WideString, NamedType, NamedStringType {
        private WideStringUnsized() {}

        private static final TypeName name = TypeName.of("WideString");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
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
    //endregion
    //region String
    public sealed interface STRING {}
    public static final class StringSized implements StringType, STRING {
        public final long maxLength;

        public StringSized(long maxLength) {
            this.maxLength = maxLength;
        }
    }

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("String")
    public static final class StringUnsized implements StringType, STRING, NamedType, NamedStringType {
        private StringUnsized() {}

        private static final TypeName name = TypeName.of("String");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public static final StringUnsized stringWithOutLengthType = new StringUnsized();
    public static STRING string(Optional<Long> len){
        if( len==null ) throw new IllegalArgumentException("len==null");
        return len.isEmpty() ? StringType.stringWithOutLengthType : new StringSized(len.get());
    }
    //endregion

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("PChar")
    public static final class PChar implements StringType, NamedType, NamedStringType {
        private PChar() {
        }

        private static final TypeName name = TypeName.of("PChar");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final PChar pchar = new PChar();

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("PWideChar")
    public static final class PWideChar implements StringType, NamedType, NamedStringType {
        private PWideChar() {
        }

        private static final TypeName name = TypeName.of("PWideChar");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final PWideChar pwidechar = new PWideChar();

    public static ImList<NamedStringType> namedValues = ImListLinked.of(pchar, pwidechar, stringWithOutLengthType, wideStringWithOutLengthType, unicodeStringWithOutLengthType, ansiStringWithOutCodePageType, shortStringWithOutLengthType);
}
