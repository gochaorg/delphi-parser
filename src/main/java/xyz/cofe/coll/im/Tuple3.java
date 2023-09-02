package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Tuple3<A,B,C> extends Serializable {
    A _1();
    B _2();
    C _3();

    default <RES> RES map( Fn3<RES,A,B,C> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3());
    }

    static <A,B,C,R extends Tuple3<A,B,C>> Tuple3<A,B,C> of(A a, B b, C c) {
        return new Tuple3<A, B, C>() {
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
