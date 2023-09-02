package xyz.cofe.coll.im;

public interface Tuple3<A,B,C> {
    A _1();
    B _2();
    C _3();

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
