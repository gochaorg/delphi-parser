package xyz.cofe.coll.im;

import java.util.function.Consumer;

/**
 * Обход элементов списка
 * @param <A> элемент списка/коллекции
 */
public interface ForEach<A> {
    void forEach(Consumer<A> consumer);
}
