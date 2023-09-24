package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

public sealed interface FloatNumberType extends NamesOfType, SimpleType {
    public static ImList<FloatNumberType,?> values = ImListLinked.of(
        new Real48(),
        new Single(),
        new Double(),
        new Real(),
        new Extended(),
        new Comp(),
        new Currency()
    );

    public record Real48() implements FloatNumberType {
        private static String[] names = new String[]{ "Real48" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Single() implements FloatNumberType {
        private static String[] names = new String[]{ "Single" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Double() implements FloatNumberType {
        private static String[] names = new String[]{ "Double" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Real() implements FloatNumberType {
        private static String[] names = new String[]{ "Real" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Extended() implements FloatNumberType {
        private static String[] names = new String[]{ "Extended" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Comp() implements FloatNumberType {
        private static String[] names = new String[]{ "Comp" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public record Currency() implements FloatNumberType {
        private static String[] names = new String[]{ "Currency" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
