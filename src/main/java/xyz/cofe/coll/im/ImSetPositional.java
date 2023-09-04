package xyz.cofe.coll.im;

public interface ImSetPositional<A,SELF extends ImSetPositional<A,SELF>>
extends
    ImSet<A,SELF>,
    PositionalRead<A>,
    Countable
{
}
