package xyz.cofe.coll.im;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Свертка списка от начала к концу
 * @param <A> элемент списка/коллекции
 */
public interface FoldLeft<A> {
    /**
     * Свертка от начала к концу списка
     * @param init начальное значение
     * @param fold функция свертки
     * @return результат свертки
     * @param <B> тип результата
     */
    public <B> B foldLeft(B init, BiFunction<B,A,B> fold );
}
