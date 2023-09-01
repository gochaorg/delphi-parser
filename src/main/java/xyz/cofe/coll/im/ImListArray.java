package xyz.cofe.coll.im;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ImListArray<A> implements ImList<A,ImListArray<A>> {
    private final List<A> list = new ArrayList<>();

    public static <A> ImListArray<A> of(Iterable<A> values){
        if( values==null )throw new IllegalArgumentException("values == null");
        var res = new ImListArray<A>();
        for( var a : values ){
            res.list.add(a);
        }
        return res;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void forEach(Consumer<A> consumer) {
        if( consumer==null )throw new IllegalArgumentException("consumer == null");
        list.forEach(consumer);
    }

    @Override
    public Optional<A> get(int index) {
        if( index<0 )return Optional.empty();
        if( index>=size() )return Optional.empty();
        return Optional.of(list.get(index));
    }
}
