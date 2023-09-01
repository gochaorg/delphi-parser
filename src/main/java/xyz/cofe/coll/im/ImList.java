package xyz.cofe.coll.im;

public interface ImList<A,SELF extends ImList<A,SELF>>
extends OrderedRead<A,SELF>, Countable
{
}
