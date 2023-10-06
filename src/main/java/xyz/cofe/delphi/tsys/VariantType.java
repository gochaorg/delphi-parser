package xyz.cofe.delphi.tsys;

/**
 * Тип Varaint, он же Any
 */
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
