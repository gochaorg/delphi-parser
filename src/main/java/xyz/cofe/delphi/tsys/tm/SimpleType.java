package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.impl.SimpleTypeImpl;

import java.util.Map;
import java.util.Optional;

/**
 * Простые типы, ну типа простые
 */
public sealed interface SimpleType extends Type permits BoolType, CharType, EnumType, FloatNumberType, IntegerNumberType, RangeType, StringType, VariantType {
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

    /**
     * Возвращает простые типы и их наименование
     * @return карта типов
     */
    public static Map<TypeName,SimpleType> typeMap(){ return SimpleTypeImpl.typeMap; }
}
