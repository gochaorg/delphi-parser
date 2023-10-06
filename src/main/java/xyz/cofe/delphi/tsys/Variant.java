package xyz.cofe.delphi.tsys;

/**
 * Тип Varaint, он же Any
 */
public final class Variant implements SimpleType, NamedType {
    public static final Variant variant = new Variant();
    private Variant() {}

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
