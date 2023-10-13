package xyz.cofe.tsys;

import xyz.cofe.coll.im.ImList;

/**
 * Унаследованный тип
 */
public interface ImplementableType extends Type {
    /**
     * От каких типов наследуется
     * @return типы
     */
    ImList<Type> implemented();
}
