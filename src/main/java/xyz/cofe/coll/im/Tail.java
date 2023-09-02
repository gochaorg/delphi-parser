package xyz.cofe.coll.im;

public interface Tail<SELF extends Append<SELF,A>,A> extends OrderedRead<A>, Emptable<SELF> {
    default SELF tail() {
        return foldLeft(Tuple2.of(empty(),0), (acc,it) -> {
            var cur = acc._1();
            var idx = acc._2();
            if(idx > 0){
                cur = cur.append(it);
            }
            return Tuple2.of(cur,idx+1);
        }).map( (acc,it) -> acc );
    }
}
