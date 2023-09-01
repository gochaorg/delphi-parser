package xyz.cofe.coll.im;

public interface Tuple2<A,B,NEXT extends Tuple2> extends Tuple1<A,NEXT> {
    B _2();

    @Override
    <C> NEXT append(C b);

    static <A,B> Tuple2<A,B,?> of(A a, B b){
        return new Tuple2<A, B, Tuple3<A,B,?>>() {
            @Override
            public B _2() {
                return b;
            }

            @Override
            public A _1() {
                return a;
            }

            @Override
            public <C> Tuple3<A, B, C> append(C b) {
                return null;
            }
        };
    }
}
