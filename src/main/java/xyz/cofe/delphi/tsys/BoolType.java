package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Булево тип
 */
public sealed interface BoolType extends NamesOfType, SimpleType {
    public static final Bool boolType = new Bool();
    public static final ByteBool byteBoolType = new ByteBool();
    public static final WordBool wordBoolType = new WordBool();
    public static final LongBool longBoolType = new LongBool();

    public static ImList<BoolType,?> values = ImListLinked.of(
        boolType,
        byteBoolType,
        wordBoolType,
        longBoolType
    );

    public static final class Bool implements BoolType {
        private Bool() {}
        private static String[] names = new String[]{ "Boolean" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class ByteBool implements BoolType {
        private ByteBool() {}
        private static String[] names = new String[]{ "ByteBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class WordBool implements BoolType {
        private WordBool() {}
        private static String[] names = new String[]{ "WordBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
    public static final class LongBool implements BoolType {
        private LongBool() {}
        private static String[] names = new String[]{ "LongBool" };
        @Override
        public String[] names() {
            return names;
        }
    }
}
