package xyz.cofe.coll.im;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Свертка списка от начала к концу
 * @param <A> элемент списка/коллекции
 */
public interface FoldLeft<A> {
    public <B> B foldLeft(B init, BiFunction<B,A,B> fold );
}
