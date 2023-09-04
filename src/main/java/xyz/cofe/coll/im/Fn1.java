package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.Function;

/**
 * Функция с одним элементом
 * @param <RES> результат функции
 * @param <A> аргумент функции
 */
public interface Fn1<RES,A> extends Serializable, Function<A,RES> {
    RES apply(A a);
}
