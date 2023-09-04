package xyz.cofe.coll.im;

import java.util.function.Function;

/**
 * Отображение элементов коллекции
 * @param <A> элемент списка/коллекции
 */
public interface MAP<A> {
    <B> ImList<B,?> map(Function<A,B> mapper);
}
