package xyz.cofe.coll.im;

import java.util.function.BiFunction;

public interface FoldRight<A> {
    public <B> B foldRight(B init, BiFunction<B,A,B> fold);
}
