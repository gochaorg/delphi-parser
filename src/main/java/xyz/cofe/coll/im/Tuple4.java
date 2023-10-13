package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Tuple4<A,B,C,D> extends Serializable {
    A _1();
    B _2();
    C _3();
    D _4();

    default <RES> RES map( Fn4<RES,A,B,C,D> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2(), _3(), _4());
    }

    default <E> Tuple5<A,B,C,D,E> append(E e){
        return Tuple5.of(_1(), _2(), _3(), _4(), e);
    }

    static <A,B,C,D> Tuple4<A,B,C,D> of(A a, B b, C c, D d) {
        return new Tuple4<A, B, C, D>() {
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
