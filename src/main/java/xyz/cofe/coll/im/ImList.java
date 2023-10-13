package xyz.cofe.coll.im;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.coll.im.json.jakson.ImListSerializer;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@JsonSerialize(using = ImListSerializer.class)
public interface ImList<A>
extends
    PositionalRead<A>,
    Countable,
    One<ImList<A>,A>,
    Append<ImList<A>,A>,
    Prepend<ImList<A>,A>,
    Tail<ImList<A>,A>,
    Filter<ImList<A>,A>,
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
        items.each(itm -> {
            foldLeft(0, (acc,it) -> {
                if( equals.apply(it,itm) ){
                    cnt[0]++;
                }
                return acc;
            });
        });
        return cnt[0];
    }

    /**
     * Проверка наличия указанных элементов
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    default boolean containsAll( PositionalRead<A> items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var cnt = containsCount( Objects::equals, items );
        return items.size() == cnt;
    }

    /**
     * Проверка наличия указанных элементов
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    @SuppressWarnings("unchecked")
    default boolean containsAll(A ... items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var cnt = containsCount( Objects::equals, ImListLinked.of(items) );
        return items.length == cnt;
    }

    /**
     * Проверка наличия указанных элементов
     * @param items искомые элементы
     * @return true - указанные элементы есть в списке / false - указанные элементы отсутствуют или присутствуют в не полном объеме
     */
    default boolean containsAll( Iterable<A> items ){
        if( items==null )throw new IllegalArgumentException("items==null");
        var itms = ImListLinked.of(items);
        var cnt = containsCount( Objects::equals, itms );
        return itms.size() == cnt;
    }

    /**
     * Поиск элемента
     * @param predicate искомый элемент
     * @return искомый элемент или None
     */
    default Optional<A> find(Function<A,Boolean> predicate) {
        if( predicate==null ) throw new IllegalArgumentException("predicate==null");
        return filter(predicate).head();
    }

    /**
     * Разворот списка
     * @return список в обратном порядке
     */
    default ImList<A> reverse(){
        return foldLeft(empty(), Prepend::prepend);
    }

    default Stream<A> stream() { return toList().stream(); }
}
