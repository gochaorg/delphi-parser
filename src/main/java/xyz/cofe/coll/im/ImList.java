package xyz.cofe.coll.im;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public interface ImList<A,SELF extends ImList<A,SELF>>
extends
    PositionalRead<A>,
    Countable,
    One<SELF,A>,
    Append<SELF,A>,
    Prepend<SELF,A>,
    Tail<SELF,A>,
    Filter<SELF,A>,
    MAP<A>,
    FMap<A>
{
    /**
     * Проверка наличия указанных элементов
     * @param equals функция проверки на равенство
     * @param items искомые элементы
     * @return кол-во совпавших
     */
    default int containsCount( Fn2<Boolean,A,A> equals, PositionalRead<A> items ){
        if( equals==null )throw new IllegalArgumentException("equals==null");
        if( items==null )throw new IllegalArgumentException("items==null");
        int[] cnt = new int[]{ 0 };
        items.forEach( itm -> {
            foldLeft(0, (acc,it) -> {
                if( equals.apply(it,itm) ){
                    cnt[0]++;
                }
                return acc;
            });
        });
        return cnt[0];
    }

    default boolean containsAll( PositionalRead<A> items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var cnt = containsCount( Objects::equals, items );
        return items.size() == cnt;
    }

    default boolean containsAll( A ... items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var cnt = containsCount( Objects::equals, ImListLinked.of(items) );
        return items.length == cnt;
    }

    default boolean containsAll( Iterable<A> items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var itms = ImListLinked.of(items);
        var cnt = containsCount( Objects::equals, itms );
        return itms.size() == cnt;
    }

    default Optional<A> find(Function<A,Boolean> predicate) {
        if( predicate==null ) throw new IllegalArgumentException("predicate==null");
        return filter(predicate).head();
    }

    default SELF reverse(){
        return foldLeft(empty(), Prepend::prepend);
    }
}
