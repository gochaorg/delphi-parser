package xyz.cofe.coll.im;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ImListArray<A> implements ImList<A,ImListArray<A>> {
    private final List<A> list;

    protected ImListArray(List<A> list){
        this.list = list;
    }

    public static <A> ImListArray<A> of(Iterable<A> values){
        if( values==null )throw new IllegalArgumentException("values == null");
        var list = new ArrayList<A>();
        var res = new ImListArray<A>(list);
        for( var a : values ){
            list.add(a);
        }
        return res;
    }

    @SafeVarargs
    public static <A> ImListArray<A> of(A ... values){
        if( values==null )throw new IllegalArgumentException("values == null");
        var list = new ArrayList<A>();
        var res = new ImListArray<A>(list);
        list.addAll(Arrays.asList(values));
        return res;
    }

    public static <A> ImListArray<A> of(PositionalRead<A> values){
        if( values==null )throw new IllegalArgumentException("values == null");
        var list = new ArrayList<A>();
        var res = new ImListArray<A>(list);
        values.each(list::add);
        return res;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void each(Consumer<A> consumer) {
        if( consumer==null )throw new IllegalArgumentException("consumer == null");
        list.forEach(consumer);
    }

    @Override
    public Optional<A> get(int index) {
        if( index<0 )return Optional.empty();
        if( index>=size() )return Optional.empty();
        return Optional.of(list.get(index));
    }

    @Override
    public <B> ImListArray<B> map(Function<A, B> mapper) {
        if( mapper==null )throw new IllegalArgumentException("mapper == null");
        var list = this.list.stream().map(mapper).collect(Collectors.toList());
        return new ImListArray<>(list);
    }

    @Override
    public <B> ImList<B, ?> fmap(Function<A, PositionalRead<B>> fmapper) {
        if( fmapper==null )throw new IllegalArgumentException("fmapper == null");
        var list = new ArrayList<B>();
        for( var a : this.list ){
            var lst = fmapper.apply(a);
            lst.each(list::add);
        }
        return new ImListArray<>(list);
    }

    @Override
    public ImListArray<A> one(A a) {
        return new ImListArray<A>(List.of(a));
    }

    @Override
    public ImListArray<A> append(A a) {
        var lst = new ArrayList<A>(this.list);
        lst.add(a);
        return new ImListArray<>(lst);
    }

    @Override
    public ImListArray<A> prepend(A a) {
        var lst = new ArrayList<A>(this.list);
        lst.add(0,a);
        return new ImListArray<>(lst);
    }

    @Override
    public ImListArray<A> empty() {
        return new ImListArray<>(List.of());
    }

    @Override
    public String toString(){
        var sb = new StringBuilder();
        each(a -> {
            if(sb.length()>0) sb.append(", ");
            sb.append(a!=null ? a.toString() : "null");
        });
        sb.insert(0,"[ ");
        sb.append(" ]");
        return sb.toString();
    }

    @Override
    public ImListArray<A> append(PositionalRead<A> values) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ArrayList<A>(this.list);
        values.each(lst::add);
        return new ImListArray<>(lst);
    }

    @Override
    public ImListArray<A> prepend(PositionalRead<A> values) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ArrayList<A>(this.list);
        lst.addAll(0, values.toList());
        return new ImListArray<>(lst);
    }

    @Override
    public Iterator<A> iterator() {
        return this.list.iterator();
    }
}
