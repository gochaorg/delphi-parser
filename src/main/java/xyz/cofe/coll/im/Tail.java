package xyz.cofe.coll.im;

/**
 * Чтение хвоста списка - все, кроме первого элемента
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface Tail<SELF extends Append<SELF,A>,A> extends PositionalRead<A>, Emptable<SELF> {
    /**
     * Возвращение списка без первого элемента
     * @return хвост списка - список без первого элемента
     */
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
