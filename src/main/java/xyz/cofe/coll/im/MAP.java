package xyz.cofe.coll.im;

import java.util.function.Function;

public interface MAP<A> {
    <B> ImList<B,?> map(Function<A,B> mapper);
}
