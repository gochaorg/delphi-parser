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
    <B> ImList<B> fmap(Function<A, PositionalRead<B>> fmapper);

    /**
     * Фильтрует коллекцию и возвращает коллекцию содержащую элементы указанного класса
     * @param cls интересующий класс элементов
     * @return коллекция элементов
     * @param <B> тип интересующих элементов
     */
    @SuppressWarnings("unchecked")
    default <B> ImList<B> fmap(Class<B> cls) {
        if( cls==null ) throw new IllegalArgumentException("cls==null");
        return fmap( item -> item!=null && cls.isAssignableFrom(item.getClass()) ?
            ImListLinked.of((B)item) : ImListLinked.of()
        );
    }
}
