package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;

/**
 * Булево тип
 */
@SuppressWarnings("UnnecessaryModifier")
public sealed interface BoolType extends NamedType, SimpleType {
    public static final Bool boolType = new Bool();
    public static final ByteBool byteBoolType = new ByteBool();
    public static final WordBool wordBoolType = new WordBool();
    public static final LongBool longBoolType = new LongBool();

    public static ImList<BoolType> values = ImListLinked.of(
        boolType,
        byteBoolType,
        wordBoolType,
        longBoolType
    );

    //region Boolean
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("Boolean")
    public static final class Bool implements BoolType {
        private Bool() {}

        private static final TypeName name = TypeName.of("Boolean");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region ByteBool
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("ByteBool")
    public static final class ByteBool implements BoolType {
        private ByteBool() {}
        private static final TypeName name = TypeName.of("ByteBool");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region WordBool
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("WordBool")
    public static final class WordBool implements BoolType {
        private WordBool() {}
        private static final TypeName name =TypeName.of("WordBool");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
    //region LongBool
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("LongBool")
    public static final class LongBool implements BoolType {
        private LongBool() {}
        private static final TypeName name = TypeName.of("LongBool");

        @Override
        public TypeName name() {
            return name;
        }

        @Override
        public String toString() {
            return name().toString();
        }
    }
    //endregion
}
