package xyz.cofe.coll.im;

/**
 * Разворот списка
 * @param <SELF> Собственный тип
 */
public interface Reverse<SELF> {
    /**
     * Разворот списка
     * @return список с противоположным ходом элементов
     */
    SELF reverse();
}
