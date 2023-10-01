package xyz.cofe.delphi.tsys;

/**
 * Тип Varaint, он же Any
 */
public final class Variant implements SimpleType, NamedType {
    public static final Variant variant = new Variant();
    private Variant() {}

    @Override
    public String name() {
        return "Variant";
    }
}
