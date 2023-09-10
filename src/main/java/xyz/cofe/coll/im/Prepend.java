package xyz.cofe.coll.im;

/**
 * Добавление элемента в конец списка
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface Prepend<SELF,A> {
    SELF prepend(A a);
    SELF prepend(PositionalRead<A> values);
}
