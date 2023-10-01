package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Цело-численные типы
 */
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

    public static ImList<IntegerNumberType,?> values = ImListLinked.of(
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
        private static String[] names = new String[]{ "ShortInt", "Int8" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public static final class SmallIntType implements IntegerNumberType {
        private SmallIntType() {}
        private static String[] names = new String[]{ "SmallInt", "Int16" };
        @Override
        public String[] names() {
            return names;
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
        private static String[] names = new String[]{ "FixedInt", "Int32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class IntegerType implements IntegerNumberType {
        private IntegerType() {}
        private static String[] names = new String[]{ "Integer", "Int32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class Int64Type implements IntegerNumberType {
        private Int64Type() {}
        private static String[] names = new String[]{ "Int64" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class ByteType implements IntegerNumberType {
        private ByteType() {}
        private static String[] names = new String[]{ "Byte", "UInt8" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class WordType implements IntegerNumberType {
        private WordType() {}
        private static String[] names = new String[]{ "Word", "UInt16" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class FixedUintType implements IntegerNumberType {
        private FixedUintType() {}
        private static String[] names = new String[]{ "FixedUint", "UInt32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class CardinalType implements IntegerNumberType {
        private CardinalType() {}
        private static String[] names = new String[]{ "Cardinal", "UInt32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public final static class UInt64Type implements IntegerNumberType {
        private UInt64Type() {}
        private static String[] names = new String[]{ "UInt64" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
