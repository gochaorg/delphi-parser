package xyz.cofe.delphi.tsys;

import java.util.Optional;

/**
 * https://docwiki.embarcadero.com/RADStudio/Alexandria/en/String_Types_(Delphi)
 */
public sealed interface StringType extends SimpleType {
    public static record ShortString(Optional<Long> maxLength) implements StringType {}
    public static record AnsiString(Optional<String> codePage) implements StringType {}
    public static record UnicodeString(Optional<Long> maxLength) implements StringType {}
    public static record WideString(Optional<Long> maxLength) implements StringType {}
    public static record STRING(Optional<Long> maxLength) implements StringType {}
    public static record PChar() implements StringType {}
    public static record PWideChar() implements StringType {}
}
