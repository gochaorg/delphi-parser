package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с тремя элементами
 * @param <RES> результат функции
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 * @param <D> аргумент функции
 * @param <E> аргумент функции
 * @param <F> аргумент функции
 */
public interface Fn6<RES,A,B,C,D,E,F> extends Serializable {
    RES apply(A a, B b, C c,D d,E e,F f);
}
