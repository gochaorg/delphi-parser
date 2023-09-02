package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.BiFunction;

public interface Fn2<RES,A,B> extends Serializable, BiFunction<A,B,RES> {
    RES apply(A a, B b);
}
