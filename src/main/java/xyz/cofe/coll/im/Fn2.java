package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.BiFunction;

/**
 * Функция с двумя элементами
 * @param <RES> результат функции
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 */
public interface Fn2<RES,A,B> extends Serializable, BiFunction<A,B,RES> {
    RES apply(A a, B b);
}
