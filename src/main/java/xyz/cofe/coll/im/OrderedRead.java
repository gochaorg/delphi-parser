package xyz.cofe.coll.im;

import java.util.Optional;

public interface OrderedRead<A,SELF extends OrderedRead<A,SELF>> extends Countable, ForEach<A> {
    Optional<A> get(int index);
    default Optional<A> head(){ return get(0); }
    default Optional<A> last(){ return get(size()-1); }
}
