package xyz.cofe.coll.im;

import java.util.function.Consumer;

/**
 * Обход элементов списка
 * @param <A> элемент списка/коллекции
 */
public interface ForEach<A> {
    /**
     * Обход списка
     * @param consumer функция принимающая элемент
     */
    void forEach(Consumer<A> consumer);
}
