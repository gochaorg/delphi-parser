package xyz.cofe.delphi.tsys;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static xyz.cofe.delphi.tsys.json.jakson.JsonAttr.JSON_TYPE_FIELD;

/**
 * Элемент интерфейса
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = JSON_TYPE_FIELD
)
public sealed interface InterfaceItem permits Function, InterfaceItem.Broken, Procedure, Property {
    record Broken(String message) implements InterfaceItem {}
}
