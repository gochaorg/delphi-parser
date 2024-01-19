package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static xyz.cofe.delphi.tsys.tm.json.jakson.JsonAttr.JSON_TYPE_FIELD;

/**
 * Элемент интерфейса
 */
@SuppressWarnings("DefaultAnnotationParam")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = JSON_TYPE_FIELD
)
public sealed interface InterfaceItem permits MethodFunction, InterfaceItem.Broken, MethodProcedure, Property {
    record Broken(String message) implements InterfaceItem {}
}
