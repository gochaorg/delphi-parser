package xyz.cofe.delphi.tsys;

/**
 * Элемент класса
 */
public sealed interface ClassItem permits ClassItem.Broken, Constructor, Destructor, Field, Function, Operator, Procedure, Property {
    record Broken(String message) implements ClassItem {}
}
