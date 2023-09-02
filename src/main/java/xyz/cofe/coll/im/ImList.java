package xyz.cofe.coll.im;

import java.util.function.Function;

public interface ImList<A,SELF extends ImList<A,SELF>>
extends
    OrderedRead<A>,
    Countable,
    One<SELF,A>,
    Append<SELF,A>,
    Prepend<SELF,A>,
    Tail<SELF,A>,
    Filter<SELF,A>,
    MAP<A>,
    FMap<A>
{
}
