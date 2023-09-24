package xyz.cofe.coll.im;

/**
 * Добавление элемента в конец списка
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface Prepend<SELF,A> {
    /**
     * Добавление элемента в начало списка
     * @param a элемент
     * @return список с добавленным элементом
     */
    SELF prepend(A a);

    /**
     * Добавление элементов в начало списка
     * @param values элементы
     * @return список с добавленными элементами
     */
    SELF prepend(PositionalRead<A> values);
}
