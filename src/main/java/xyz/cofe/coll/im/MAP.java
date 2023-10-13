package xyz.cofe.coll.im;

import java.util.function.Function;

/**
 * Отображение элементов коллекции
 * @param <A> элемент списка/коллекции
 */
public interface MAP<A> {
    /**
     * Отображение элементов коллекции
     * @param mapper функция отображения
     * @return коллекция с отображением
     * @param <B> тип элемента коллекции
     */
    <B> ImList<B> map(Function<A,B> mapper);
}
