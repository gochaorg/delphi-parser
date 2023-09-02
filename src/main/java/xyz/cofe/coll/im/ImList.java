package xyz.cofe.coll.im;

import java.util.function.Function;

public interface ImList<A,SELF extends ImList<A,SELF>>
extends OrderedRead<A>, Countable, One<SELF,A>
{
    @SafeVarargs
    public static <A> ImListArray<A> of(A ... values) {
        return ImListArray.of(values);
    }

    public static <A> ImListArray<A> of(Iterable<A> values) {
        return ImListArray.of(values);
    }

    <B> ImList<B,?> map(Function<A,B> mapper);
    <B> ImList<B,?> fmap(Function<A,OrderedRead<B>> fmapper);

    SELF append(A a);
}
