package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Tuple2<A,B> extends Serializable {
    A _1();
    B _2();

    default <RES> RES map( Fn2<RES,A,B> f ){
        if( f==null )throw new IllegalArgumentException("f==null");
        return f.apply(_1(), _2());
    }

    default <C> Tuple3<A,B,C> append(C c){
        return Tuple3.of(_1(),_2(),c);
    }

    static <A,B> Tuple2<A,B> of(A a, B b){
        return new Tuple2<A, B>() {
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
