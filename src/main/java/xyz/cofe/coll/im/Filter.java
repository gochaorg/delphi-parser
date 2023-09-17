package xyz.cofe.coll.im;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * Фильтрация коллекции
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface Filter<SELF extends Append<SELF,A>,A> extends FoldLeft<A>, Emptable<SELF> {
    /**
     * Фильтрует список
     * @param pred условие оставления элемента в списке
     * @return отфильтрованный список
     */
    default SELF filter(Function<A,Boolean> pred) {
        if( pred==null )throw new IllegalArgumentException("pred == null");
        return foldLeft(empty(), (res,it) -> {
            if(pred.apply(it)){
                return res.append(it);
            }
            return res;
        });
    }
}
