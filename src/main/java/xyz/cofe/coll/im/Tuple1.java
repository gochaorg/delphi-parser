package xyz.cofe.coll.im;

public interface Tuple1<A,NEXT extends Tuple1> {
    A _1();
    static <A> Tuple1<A,Tuple2<A,?,?>> of( A a ) {
        return new Tuple1<>() {
            @Override
            public A _1() {
                return null;
            }

            @Override
            public <B> Tuple2<A, B, ?> append(B b) {
                return null;
            }
        };
    }
    public <B> NEXT append(B b);
}
