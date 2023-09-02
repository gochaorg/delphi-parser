package xyz.cofe.coll.im;

import java.io.Serializable;
import java.util.function.Function;

public interface Fn1<RES,A> extends Serializable, Function<A,RES> {
    RES apply(A a);
}
