package xyz.cofe.coll.im;

import java.util.function.BiFunction;

/**
 * Свертка списка от конца к началу
 * @param <A> элемент списка/коллекции
 */
public interface FoldRight<A> {
    /**
     * Свертка списка от конца к началу
     * @param init начальное значение
     * @param fold функция свертки
     * @return результат свертки
     * @param <B> тип результата
     */
    public <B> B foldRight(B init, BiFunction<B,A,B> fold);
}
