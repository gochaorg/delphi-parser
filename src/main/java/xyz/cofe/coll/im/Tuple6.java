package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Tuple6<A,B,C,D,E,F> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();
    E _5();
    F _6();

    default <RES> RES map( Fn6<RES,A,B,C,D,E,F> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5(), _6());
    }

    static <A,B,C,D,E,F> Tuple6<A,B,C,D,E,F> of(A a, B b, C c, D d, E e, F f) {
        return new Tuple6<A, B, C, D, E,F>() {
            @Override
            public F _6() {
                return f;
            }

            @Override
            public E _5() {
                return e;
            }

            @Override
            public D _4() {
                return d;
            }

            @Override
            public C _3() {
                return c;
            }

            @Override
            public B _2() {
                return b;
            }

            @Override
            public A _1() {
                return a;
            }
        };
    }
}
