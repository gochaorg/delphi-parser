package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static xyz.cofe.delphi.tsys.json.jakson.JsonAttr.JSON_TYPE_FIELD;

/**
 * Элемент класса
 */
@SuppressWarnings("DefaultAnnotationParam")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = JSON_TYPE_FIELD
)
public sealed interface ClassItem permits ClassItem.Broken, Constructor, Destructor, Field, Function, Operator, Procedure, Property {
    record Broken(String message) implements ClassItem {}
}
