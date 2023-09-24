package xyz.cofe.tsys;

public sealed interface Argument {
    public static record NamedArgument( String name, Type type ) implements Argument {}
    public static record PositionalArgument( Type type ) implements Argument {}
}
