package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;

/**
 * Тип Varaint, он же Any
 */
@JsonSerialize(using = FlatStrSer.class)
@FlatStr("Variant")
public final class VariantType implements SimpleType, NamedType {
    public static final VariantType variant = new VariantType();
    private VariantType() {}

    private static final TypeName name = TypeName.of("Variant");
    @Override
    public TypeName name() {
        return name;
    }

    @Override
    public String toString() {
        return name().toString();
    }
}
