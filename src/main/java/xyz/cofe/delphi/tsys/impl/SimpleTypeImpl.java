package xyz.cofe.delphi.tsys.impl;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.tsys.tm.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Перечень типов которые предопределены
 */
public class SimpleTypeImpl {
    private static final ImList<SimpleType> simpleNamedTypeList;

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ImList<SimpleType> upcast(ImList<? extends SimpleType> value) {
        return ImListLinked.of((List) value.toList());
    }

    static {
        ImList<SimpleType> types = ImListLinked.of();
        types = types
            .prepend(VariantType.variant)
            .prepend(upcast(IntegerNumberType.values))
            .prepend(upcast(FloatNumberType.values))
            .prepend(upcast(CharType.values))
            .prepend(upcast(BoolType.values))
            .prepend(upcast(StringType.namedValues))
        ;
        simpleNamedTypeList = types;
    }

    public static final Map<TypeName, SimpleType> typeMap;

    static {
        var map = new HashMap<TypeName, SimpleType>();
        simpleNamedTypeList.each(st -> {
            if (st instanceof NamesOfType names) {
                for (var name : names.names()) {
                    map.computeIfAbsent(name, n -> st);
                }
            } else if (st instanceof NamedType name) {
                map.computeIfAbsent(name.name(), n -> st);
            }
        });
        typeMap = Collections.unmodifiableMap(map);
    }
}
