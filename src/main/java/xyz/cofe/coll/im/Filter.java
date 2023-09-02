package xyz.cofe.coll.im;

public interface Filter<SELF extends Append<SELF,A>,A> extends FoldLeft<A>, Emptable<SELF> {
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
