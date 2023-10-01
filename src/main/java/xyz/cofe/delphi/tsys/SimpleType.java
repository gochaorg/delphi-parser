package xyz.cofe.delphi.tsys;

import xyz.cofe.delphi.tsys.impl.SimpleTypeImpl;

import java.util.Optional;

/**
 * Простые типы, ну типа простые
 */
public sealed interface SimpleType permits BoolType, CharType, EnumType, FloatNumberType, IntegerNumberType, RangeType, StringType, Variant {
    /**
     * Поиск типа по его имени среди предопределенных
     * @param name имя типа
     * @return тип
     */
    public static Optional<SimpleType> findByName(TypeName name) {
        if (name == null) throw new IllegalArgumentException("name==null");

        var st = SimpleTypeImpl.typeMap.get(name);
        if( st!=null )return Optional.of(st);

        return Optional.empty();
    }
}
