package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Функция с тремя элементами
 * @param <RES> результат функции
 * @param <A> аргумент функции
 * @param <B> аргумент функции
 * @param <C> аргумент функции
 */
public interface Fn3<RES,A,B,C> extends Serializable {
    RES apply(A a, B b, C c);
}
