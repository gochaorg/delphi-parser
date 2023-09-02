package xyz.cofe.coll.im;

import java.util.Optional;

public interface ImSetOrdered<A,SELF extends ImSetOrdered<A,SELF>>
extends
    ImSet<A,SELF>,
    OrderedRead<A>,
    Countable
{
}
