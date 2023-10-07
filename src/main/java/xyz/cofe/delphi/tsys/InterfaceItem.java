package xyz.cofe.delphi.tsys;

/**
 * Элемент интерфейса
 */
public sealed interface InterfaceItem permits Function, InterfaceItem.Broken, Procedure, Property {
    public static record Broken(String message) implements InterfaceItem {}
}
