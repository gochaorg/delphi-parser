package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

public sealed interface IntegerNumberType extends NamesOfType, SimpleType {
    public static ImList<IntegerNumberType,?> values = ImListLinked.of(
        new ShortInt(),
        new SmallInt(),
        new FixedInt(),
        new Integer(),
        new Int64(),
        new Byte(),
        new Word(),
        new FixedUint(),
        new Cardinal(),
        new UInt64()
    );

    public record ShortInt() implements IntegerNumberType {
        private static String[] names = new String[]{ "ShortInt", "Int8" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record SmallInt() implements IntegerNumberType {
        private static String[] names = new String[]{ "SmallInt", "Int16" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record FixedInt() implements IntegerNumberType {
        private static String[] names = new String[]{ "FixedInt", "Int32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record Integer() implements IntegerNumberType {
        private static String[] names = new String[]{ "Integer", "Int32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record Int64() implements IntegerNumberType {
        private static String[] names = new String[]{ "Int64" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record Byte() implements IntegerNumberType {
        private static String[] names = new String[]{ "Byte", "UInt8" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record Word() implements IntegerNumberType {
        private static String[] names = new String[]{ "Word", "UInt16" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record FixedUint() implements IntegerNumberType {
        private static String[] names = new String[]{ "FixedUint", "UInt32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record Cardinal() implements IntegerNumberType {
        private static String[] names = new String[]{ "Cardinal", "UInt32" };
        @Override
        public String[] names() {
            return names;
        }
    }

    public record UInt64() implements IntegerNumberType {
        private static String[] names = new String[]{ "UInt64" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
