package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Числа с плавающей десятичной точкой
 */
public sealed interface FloatNumberType extends NamesOfType, SimpleType {
    public static final Real48 real48Type = new Real48();
    public static final Single singleType = new Single();
    public static final Double doubleType = new Double();
    public static final Real realType = new Real();
    public static final Extended extendedType = new Extended();
    public static Comp compType = new Comp();
    public static Currency currencyType = new Currency();

    public static ImList<FloatNumberType,?> values = ImListLinked.of(
        real48Type,
        singleType,
        doubleType,
        realType,
        extendedType,
        compType,
        currencyType
    );

    public static final class Real48 implements FloatNumberType {
        private Real48(){}
        private static String[] names = new String[]{ "Real48" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Single implements FloatNumberType {
        private Single() {}
        private static String[] names = new String[]{ "Single" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Double implements FloatNumberType {
        private Double() {}
        private static String[] names = new String[]{ "Double" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Real implements FloatNumberType {
        private Real() {}
        private static String[] names = new String[]{ "Real" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Extended implements FloatNumberType {
        private Extended() {}
        private static String[] names = new String[]{ "Extended" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Comp implements FloatNumberType {
        private Comp() {}
        private static String[] names = new String[]{ "Comp" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class Currency implements FloatNumberType {
        private Currency() {}
        private static String[] names = new String[]{ "Currency" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
