package xyz.cofe.coll.im;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * Позиционное чтение элементов коллекции
 * @param <A> элемент списка/коллекции
 */
public interface PositionalRead<A>
    extends Countable, ForEach<A>, FoldLeft<A>, FoldRight<A>
{
    /**
     * Чтение элемента списка
     * @param index индекс элемента
     * @return элемент
     */
    Optional<A> get(int index);

    /**
     * Чтение "головы" / первого элемента
     * @return элемент
     */
    default Optional<A> head(){ return get(0); }

    /**
     * Чтение последнего элемента
     * @return элемент
     */
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

    /**
     * Возвращает мутабельный список
     * @return мутабельный список
     */
    default List<A> toList(){
        var lst = new ArrayList<A>();
        each(lst::add);
        return lst;
    }
}
