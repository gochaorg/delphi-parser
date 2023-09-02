package xyz.cofe.coll.im;

import java.util.Optional;
import java.util.function.BiFunction;

public interface OrderedRead<A>
    extends Countable, ForEach<A>, FoldLeft<A>, FoldRight<A>
{
    Optional<A> get(int index);
    default Optional<A> head(){ return get(0); }
    default Optional<A> last(){ return get(size()-1); }
    default <B> B foldLeft(B init, BiFunction<B,A,B> fold ) {
        var acc = new Object[]{ init };

        for( var i=0; i<size(); i++ ){
            get(i).ifPresent(a -> {
                //noinspection unchecked
                var cur = (B)acc[0];
                cur = fold.apply(cur,a);
                acc[0] = cur;
            });
        }

        //noinspection unchecked
        return (B)acc[0];
    }
    default <B> B foldRight(B init, BiFunction<B,A,B> fold) {
        var acc = new Object[]{ init };

        for( var i=size()-1; i>=0; i-- ){
            get(i).ifPresent(a -> {
                //noinspection unchecked
                var cur = (B)acc[0];
                cur = fold.apply(cur,a);
                acc[0] = cur;
            });
        }

        //noinspection unchecked
        return (B)acc[0];
    }
}
