package xyz.cofe.coll.im;

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
    default SELF filter(Fn1<Boolean,A> pred) {
        if( pred==null )throw new IllegalArgumentException("pred == null");
        return foldLeft(empty(), (res,it) -> {
            if(pred.apply(it)){
                return res.append(it);
            }
            return res;
        });
    }
}
