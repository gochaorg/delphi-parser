package xyz.cofe.coll.im;

import java.util.function.Function;

/**
 * Отображение коллекции
 * @param <A> элемент списка/коллекции
 */
public interface FMap<A> {
    /**
     * Отображение коллекции
     * @param fmapper функция отображения
     * @return отображенная коллекция
     * @param <B> тип элементов в коллекции
     */
    <B> ImList<B,?> fmap(Function<A, PositionalRead<B>> fmapper);
}
