package xyz.cofe.coll.mut;

import com.google.common.collect.ImmutableList;

import java.util.Iterator;
import java.util.List;

/**
 * Частично упорядоченное множество
 */
public interface PartOrderSet<A> extends Iterable<A> {
    /**
     * Перечисляет элементы множества
     * @return элементы
     */
    Iterator<A> iterator();


    boolean contains( A a );

    ImmutableList<A> childrenOf( A a );

    ImmutableList<A> parentsOf( A a );
}
