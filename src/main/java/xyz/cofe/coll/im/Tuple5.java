package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Tuple5<A,B,C,D,E> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();
    E _5();

    default <RES> RES map( Fn5<RES,A,B,C,D,E> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4(), _5());
    }

    static <A,B,C,D,E> Tuple5<A,B,C,D,E> of(A a, B b, C c, D d,E e) {
        return new Tuple5<A, B, C, D, E>() {
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
