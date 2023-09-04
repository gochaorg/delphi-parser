package xyz.cofe.coll.im;

import java.util.function.Function;

/**
 * Отображение коллекции
 * @param <A> элемент списка/коллекции
 */
public interface FMap<A> {
    <B> ImList<B,?> fmap(Function<A, PositionalRead<B>> fmapper);
}
