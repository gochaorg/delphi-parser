package xyz.cofe.delphi.tsys;

/**
 * Элемент интерфейса
 */
public sealed interface InterfaceItem permits Function, InterfaceItem.BrokenMethod, Procedure, Property {
    public static record BrokenMethod(String message) implements InterfaceItem {}
}
