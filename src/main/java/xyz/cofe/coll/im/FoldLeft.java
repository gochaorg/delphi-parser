package xyz.cofe.coll.im;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface FoldLeft<A> {
    public <B> B foldLeft(B init, BiFunction<B,A,B> fold );
}
