package xyz.cofe.tsys;

import com.google.common.collect.ImmutableList;

/**
 * Унаследованный тип
 */
public interface ImplementableType extends Type {
    /**
     * От каких типов наследуется
     * @return типы
     */
    ImmutableList<Type> getImplements();
}
