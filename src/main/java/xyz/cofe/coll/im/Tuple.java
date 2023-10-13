package xyz.cofe.coll.im;

public interface Tuple {
    static Tuple of(){
        return new Tuple() {
        };
    }

    default <A> Tuple1<A> append( A a ) {
        return of(a);
    }

    static <A> Tuple1<A> of(A a){
        return Tuple1.of(a);
    }

    static <A,B> Tuple2<A,B> of(A a, B b){
        return Tuple2.of(a,b);
    }

    static <A,B,C> Tuple3<A,B,C> of(A a,B b,C c){
        return Tuple3.of(a,b,c);
    }

    static <A,B,C,D> Tuple4<A,B,C,D> of(A a,B b,C c,D d){
        return Tuple4.of(a,b,c,d);
    }

    static <A,B,C,D,E> Tuple5<A,B,C,D,E> of(A a,B b,C c,D d, E e){
        return Tuple5.of(a,b,c,d,e);
    }

    static <A,B,C,D,E,F> Tuple6<A,B,C,D,E,F> of(A a,B b,C c,D d, E e, F f){
        return Tuple6.of(a,b,c,d,e,f);
    }
}
