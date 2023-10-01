package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

/**
 * Булево тип
 */
public sealed interface BoolType extends NamedType, SimpleType {
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

        private static TypeName name = new TypeName("Boolean");

        @Override
        public TypeName name() {
            return name;
        }
    }
    public static final class ByteBool implements BoolType {
        private ByteBool() {}
        private static TypeName name = new TypeName("ByteBool");

        @Override
        public TypeName name() {
            return name;
        }
    }
    public static final class WordBool implements BoolType {
        private WordBool() {}
        private static TypeName name = new TypeName("WordBool");

        @Override
        public TypeName name() {
            return name;
        }
    }
    public static final class LongBool implements BoolType {
        private LongBool() {}
        private static TypeName name = new TypeName("LongBool");

        @Override
        public TypeName name() {
            return name;
        }
    }
}
