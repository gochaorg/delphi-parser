package xyz.cofe.tsys;

import xyz.cofe.coll.im.ImList;

/**
 * Параметр типа
 */
public sealed interface GenericParam {
    /**
     * Инвариантный тип
     * @param name имя параметра
     */
    record InVariant( String name ) implements GenericParam {}

    /**
     * Ковариантная позиция
     *
     * Можно использовать только в out / return позициях
     * @param name имя параметра
     * @param requiredImpl ограни
     */
    record CoVariant(String name, ImList<Type> requiredImpl ) implements GenericParam {}

    /**
     * Контр вариантная позиция
     * @param name имя параметра
     */
    record ContraVariant( String name, ImList<Type> requiredImpl ) implements GenericParam {}
}
