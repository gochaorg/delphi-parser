package xyz.cofe.delphi.tsys;

public sealed interface InterfaceItem permits InterfaceItem.BrokenMethod, Method, Property {
    public static record BrokenMethod(String message) implements InterfaceItem {}
}
