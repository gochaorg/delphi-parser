package xyz.cofe.coll.im;

import java.util.function.Function;

public interface FMap<A> {
    <B> ImList<B,?> fmap(Function<A,OrderedRead<B>> fmapper);
}
