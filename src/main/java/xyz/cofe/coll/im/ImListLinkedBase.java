package xyz.cofe.coll.im;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ImListLinkedBase<A,SELF extends ImListLinkedBase<A,SELF>>
implements
    Prepend<SELF,A>,
    Countable,
    ForEach<A>,
    Emptable<SELF>,
    One<SELF,A>,
    OrderedRead<A>,
    Append<SELF,A>,
    Tail<SELF,A>,
    Reverse<SELF>,
    Filter<SELF,A>,
    MAP<A>,
    FMap<A>,
    ImList<A,SELF>
{
    private A value;
    private ImListLinkedBase<A,SELF> next;
    private int size;

    protected ImListLinkedBase(A value, ImListLinkedBase<A,SELF> next){
        this.value = value;
        this.next = next;
        if( next!=null ){
            this.size = next.size+1;
        }else{
            this.size = value!=null ? 1 : 0;
        }
    }

    /**
     * Проверка, что текущий элемент - это конец списка
     * @return true - конец и не содержит значения
     */
    protected boolean isNil(){
        return value==null && next==null;
    }

    /**
     * Данный метод должен быть переопределен в дочерних классах
     * @param value значение
     * @param next ссылка на следующий элемент
     * @return список
     */
    @SuppressWarnings("unchecked")
    protected <A> ImListLinkedBase<A,?> selfConstructor(A value, ImListLinkedBase<A,?> next){
        return new ImListLinkedBase<>(value,next);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF empty() {
        return (SELF) selfConstructor(null,null);
    }

    @Override
    public SELF one(A a) {
        return empty().prepend(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF prepend(A a) {
        return (SELF) selfConstructor(a, this);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void forEach(Consumer<A> consumer) {
        if(consumer==null)throw new IllegalArgumentException("consumer==null");
        var cur = this;
        while (cur!=null){
            if( cur.isNil() )break;
            consumer.accept(cur.value);
            cur = cur.next;
        }
    }

    @Override
    public Optional<A> get(int index) {
        if( index<0 ) return Optional.empty();
        var cur = this;
        while (cur!=null){
            if( cur.isNil() )break;
            if( index==0 )return Optional.ofNullable(cur.value);
            cur = cur.next;
            index -= 1;
        }
        return Optional.empty();
    }

    @Override
    public Optional<A> head() {
        if( isNil() )return Optional.empty();
        return Optional.ofNullable( value );
    }

    @Override
    public Optional<A> last() {
        if( isNil() ) return Optional.empty();
        var cur = this;
        while (cur!=null){
            var nxt = cur.next;
            if(nxt.isNil()){
                return Optional.ofNullable(cur.value);
            }
            cur = nxt;
        }
        return Optional.empty();
    }

    public SELF reverse(){
        var lst = empty();
        var cur = this;
        while (cur!=null){
            if(cur.isNil())break;
            lst = lst.prepend(cur.value);
            cur = cur.next;
        }
        return lst;
    }

    @Override
    public SELF append(A a) {
        return prepend(a).reverse();
    }

    @SuppressWarnings("unchecked")
    @Override
    public SELF tail() {
        if(next==null) return empty();
        return (SELF) next;
    }

    @Override
    public <B> B foldLeft(B init, BiFunction<B, A, B> fold) {
        if( fold==null )throw new IllegalArgumentException("fold==null");
        var res = init;
        var cur = this;
        while (cur!=null){
            if(cur.isNil())break;
            res = fold.apply(res, cur.value);
            cur = cur.next;
        }
        return res;
    }

    @Override
    public <B> B foldRight(B init, BiFunction<B, A, B> fold) {
        if( fold==null )throw new IllegalArgumentException("fold==null");
        return reverse().foldLeft(init, fold);
    }

    @Override
    public SELF filter(Fn1<Boolean, A> pred) {
        if( pred==null )throw new IllegalArgumentException("pred == null");
        return foldLeft(empty(), (res,it) -> {
            if(pred.apply(it)){
                return res.prepend(it);
            }
            return res;
        }).reverse();
    }

    @Override
    public <B> ImList<B, ?> map(Function<A, B> mapper) {
        if(mapper==null)throw new IllegalArgumentException("mapper==null");
        var res = selfConstructor((B) null, null );
        var cur = this;
        while (cur!=null){
            if(cur.isNil())break;
            var v = mapper.apply(cur.value);
            res = res.prepend(v);
            cur = cur.next;
        }
        return res.reverse();
    }

    @Override
    public <B> ImList<B, ?> fmap(Function<A, OrderedRead<B>> fmapper) {
        if(fmapper==null)throw new IllegalArgumentException("fmapper==null");
        var res = selfConstructor((B) null, null );
        var cur = this;
        while (cur!=null){
            if(cur.isNil())break;
            var v = fmapper.apply(cur.value);
            res = v.foldLeft(res, ImListLinkedBase::prepend);
            cur = cur.next;
        }
        return res.reverse();
    }
}
