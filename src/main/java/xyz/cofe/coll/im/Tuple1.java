package xyz.cofe.coll.im;

public interface Tuple1<A> {
    A _1();

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
