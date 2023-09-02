package xyz.cofe.coll.im;

public class ImListLinked<A> extends ImListLinkedBase<A,ImListLinked<A>> {
    public ImListLinked(A value, ImListLinkedBase<A, ImListLinked<A>> next) {
        super(value, next);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected <A1> ImListLinkedBase<A1, ?> selfConstructor(A1 value, ImListLinkedBase<A1, ?> next) {
        return new ImListLinked(value,next);
    }

    @SafeVarargs
    public static <A> ImListLinked<A> of(A ... values ){
        var lst = new ImListLinked<A>( null, null );
        for( var a : values ){
            lst = lst.prepend(a);
        }
        return lst.reverse();
    }

    public static <A> ImListLinked<A> of( Iterable<A> values ) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ImListLinked<A>( null, null );
        for( var a : values ){
            lst = lst.prepend(a);
        }
        return lst.reverse();
    }

    public static <A> ImListLinked<A> of( OrderedRead<A> values ) {
        if( values==null )throw new IllegalArgumentException("values==null");
        var lst = new ImListLinked<A>( null, null );
        lst = values.foldLeft(lst, ImListLinkedBase::prepend);
        return lst.reverse();
    }
}
