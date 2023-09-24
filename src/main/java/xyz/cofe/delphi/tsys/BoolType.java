package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Булево тип
 */
public sealed interface BoolType extends NamesOfType, SimpleType {
    public static ImList<BoolType,?> values = ImListLinked.of(
        new Bool(),
        new ByteBool(),
        new WordBool(),
        new LongBool()
    );

    public static record Bool() implements BoolType {
        private static String[] names = new String[]{ "Boolean" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static record ByteBool() implements BoolType {
        private static String[] names = new String[]{ "ByteBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static record WordBool() implements BoolType {
        private static String[] names = new String[]{ "WordBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static record LongBool() implements BoolType {
        private static String[] names = new String[]{ "LongBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
