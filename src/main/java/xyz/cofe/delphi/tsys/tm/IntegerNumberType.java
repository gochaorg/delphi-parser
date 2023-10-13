package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;

/**
 * Цело-численные типы
 */
@SuppressWarnings("UnnecessaryModifier")
public sealed interface IntegerNumberType extends NamesOfType, SimpleType {
    public static final ShortIntType shortIntType = new ShortIntType();
    public static final SmallIntType smallIntType = new SmallIntType();
    public static final FixedIntType fixedIntType = new FixedIntType();
    public static final IntegerType integerType = new IntegerType();
    public static final Int64Type int64Type = new Int64Type();
    public static final ByteType byteType = new ByteType();
    public static final WordType wordType = new WordType();
    public static final FixedUintType fixedUintType = new FixedUintType();
    public static final CardinalType cardinalType = new CardinalType();
    public static final UInt64Type uInt64Type = new UInt64Type();

    public static ImList<IntegerNumberType> values = ImListLinked.of(
        cardinalType,
        shortIntType,
        smallIntType,
        integerType,
        fixedIntType,
        int64Type,
        byteType,
        wordType,
        fixedUintType,
        uInt64Type
    );

    public static final class ShortIntType implements IntegerNumberType {
        private ShortIntType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("ShortInt"), TypeName.of("Int8") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public static final class SmallIntType implements IntegerNumberType {
        private SmallIntType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("SmallInt"), TypeName.of("Int16") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    /**
     * https://docwiki.embarcadero.com/Libraries/Alexandria/en/System.FixedInt
     *
     * <ul>
     * <li>On Windows platforms (32-bit and 64-bit Windows), FixedInt is equivalent to the LongInt type.
     * <li>On OSX32, 32-bit iOS, 64-bit iOS, and Android platforms, FixedInt is equivalent to the Integer type.
     * </ul>
     */
    public static final class FixedIntType implements IntegerNumberType {
        private FixedIntType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("FixedInt"), TypeName.of("Int32") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("Integer")
    public final static class IntegerType implements IntegerNumberType {
        private IntegerType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("Integer"), TypeName.of("Int32") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class Int64Type implements IntegerNumberType {
        private Int64Type() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("Int64") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class ByteType implements IntegerNumberType {
        private ByteType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("Byte"), TypeName.of("UInt8") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class WordType implements IntegerNumberType {
        private WordType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("Word"), TypeName.of("UInt16") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class FixedUintType implements IntegerNumberType {
        private FixedUintType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("FixedUint"), TypeName.of("UInt32") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class CardinalType implements IntegerNumberType {
        private CardinalType() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("Cardinal"), TypeName.of("UInt32") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }

    public final static class UInt64Type implements IntegerNumberType {
        private UInt64Type() {}
        private static final TypeName[] names = new TypeName[]{ TypeName.of("UInt64") };
        @Override
        public TypeName[] names() {
            return names;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
}
