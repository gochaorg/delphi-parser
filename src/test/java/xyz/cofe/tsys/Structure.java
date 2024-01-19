package xyz.cofe.tsys;

import xyz.cofe.coll.im.ImList;

/**
 * Некая структура
 */
public record Structure(
    String name,
    ImList<Field> fields
) implements NamedType {
}
