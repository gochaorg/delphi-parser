package xyz.cofe.coll.im;

public interface Tuple3<A,B,C> extends Tuple2<A,B,Tuple3<A,B,C>> {
    C _3();
    static <A,B,C,R extends Tuple3<A,B,C> & NextTuple> Tuple3<A,B,C> of(A a, B b, C c) {
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

            @Override
            public <D> Tuple3<A, B, C> append(D d) {
                return this;
            }
        };
    }
}
