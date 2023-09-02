package xyz.cofe.coll.im;

public interface Tuple2<A,B> {
    A _1();
    B _2();

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
