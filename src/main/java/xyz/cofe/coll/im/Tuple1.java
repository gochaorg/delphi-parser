package xyz.cofe.coll.im;

import java.io.Serializable;

/**
 * Кортеж из одного элемента
 * @param <A> Тип элемента
 */
public interface Tuple1<A> extends Serializable {
    /**
     * Возвращает элемент кортежа
     * @return элемент
     */
    A _1();

    default <RES> RES map( Fn1<RES,A> f ) {
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1());
    }

    default <B> Tuple2<A,B> append(B b) {
        return Tuple2.of(_1(),b);
    }

    static <A> Tuple1<A> of( A a ) {
        return new Tuple1<>() {
            @Override
            public A _1() {
                return a;
            }
        };
    }
}
