package xyz.cofe.coll.im;

import java.io.Serializable;

public interface Fn3<RES,A,B,C> extends Serializable {
    RES apply(A a, B b, C c);
}
