package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Числа с плавающей десятичной точкой
 */
public sealed interface FloatNumberType extends NamedType, SimpleType {
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
        private static TypeName name = TypeName.of("Real48");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Single implements FloatNumberType {
        private Single() {}
        private static TypeName name = TypeName.of("Single");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Double implements FloatNumberType {
        private Double() {}
        private static TypeName name = TypeName.of("Double");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Real implements FloatNumberType {
        private Real() {}
        private static TypeName name = TypeName.of("Real");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Extended implements FloatNumberType {
        private Extended() {}
        private static TypeName name = TypeName.of("Extended");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Comp implements FloatNumberType {
        private Comp() {}
        private static TypeName name = TypeName.of("Comp");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    public static final class Currency implements FloatNumberType {
        private Currency() {}
        private static TypeName name = TypeName.of("Currency");
        @Override public TypeName name() { return name; }

        @Override
        public String toString() {
            return name().toString();
        }
    }
}
