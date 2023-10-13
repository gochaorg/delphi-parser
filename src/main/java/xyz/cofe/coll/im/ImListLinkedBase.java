package xyz.cofe.coll.im;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class ImListLinkedBase<A>
    implements
    Prepend<ImList<A>, A>,
    Countable,
    ForEach<A>,
    Emptable<ImList<A>>,
    One<ImList<A>, A>,
    PositionalRead<A>,
    Append<ImList<A>, A>,
    Tail<ImList<A>, A>,
    Reverse<ImList<A>>,
    Filter<ImList<A>, A>,
    MAP<A>,
    FMap<A>,
    ImList<A> {
    private final A value;
    private final ImListLinkedBase<A> next;
    private final int size;

    protected ImListLinkedBase(A value, ImListLinkedBase<A> next) {
        this.value = value;
        this.next = next;
        if (next != null) {
            this.size = next.size + 1;
        } else {
            this.size = value != null ? 1 : 0;
        }
    }

    /**
     * Проверка, что текущий элемент - это конец списка
     *
     * @return true - конец и не содержит значения
     */
    protected boolean isNil() {
        return value == null && next == null;
    }

    /**
     * Данный метод должен быть переопределен в дочерних классах
     *
     * @param value значение
     * @param next  ссылка на следующий элемент
     * @return список
     */
    protected abstract <B> ImListLinkedBase<B> selfConstructor(B value, ImListLinkedBase<B> next);
//    {
//        return new ImListLinkedBase<>(value,next);
//    }

    @Override
    public ImListLinkedBase<A> empty() {
        return selfConstructor(null, null);
    }

    @Override
    public ImListLinkedBase<A> one(A a) {
        return empty().prepend(a);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImListLinkedBase<A> prepend(A a) {
        return selfConstructor(a, this);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void each(Consumer<A> consumer) {
        if (consumer == null) throw new IllegalArgumentException("consumer==null");
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            consumer.accept(cur.value);
            cur = cur.next;
        }
    }

    @Override
    public Optional<A> get(int index) {
        if (index < 0) return Optional.empty();
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            if (index == 0) return Optional.ofNullable(cur.value);
            cur = cur.next;
            index -= 1;
        }
        return Optional.empty();
    }

    @Override
    public Optional<A> head() {
        if (isNil()) return Optional.empty();
        return Optional.ofNullable(value);
    }

    @SuppressWarnings("ConstantValue")
    @Override
    public Optional<A> last() {
        if (isNil()) return Optional.empty();
        var cur = this;
        while (cur != null) {
            if( cur.next==null ) {
                if( cur.isNil() )return Optional.empty();
                return Optional.ofNullable(cur.value);
            }
            cur = cur.next;
        }
        return Optional.empty();
    }

    public ImListLinkedBase<A> reverse() {
        var lst = empty();
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            lst = lst.prepend(cur.value);
            cur = cur.next;
        }
        return lst;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public ImListLinkedBase<A> append(A a) {
        if (size() == 0) return one(a);
        if (size() == 1) return one(a).prepend(get(0).get());
        return foldRight(one(a), ImListLinkedBase::prepend);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImListLinkedBase<A> tail() {
        if (next == null) return empty();
        return next;
    }

    @Override
    public <B> B foldLeft(B init, BiFunction<B, A, B> fold) {
        if (fold == null) throw new IllegalArgumentException("fold==null");
        var res = init;
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            res = fold.apply(res, cur.value);
            cur = cur.next;
        }
        return res;
    }

    @Override
    public <B> B foldRight(B init, BiFunction<B, A, B> fold) {
        if (fold == null) throw new IllegalArgumentException("fold==null");
        return reverse().foldLeft(init, fold);
    }

    @Override
    public <B> ImList<B> map(Function<A, B> mapper) {
        if (mapper == null) throw new IllegalArgumentException("mapper==null");
        var res = selfConstructor((B) null, null);
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            var v = mapper.apply(cur.value);
            res = res.prepend(v);
            cur = cur.next;
        }
        return res.reverse();
    }

    @Override
    public <B> ImList<B> fmap(Function<A, PositionalRead<B>> fmapper) {
        if (fmapper == null) throw new IllegalArgumentException("fmapper==null");
        var res = selfConstructor((B) null, null);
        var cur = this;
        while (cur != null) {
            if (cur.isNil()) break;
            var v = fmapper.apply(cur.value);
            res = v.foldLeft(res, ImListLinkedBase::prepend);
            cur = cur.next;
        }
        return res.reverse();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImListLinkedBase<A> append(PositionalRead<A> values) {
        if (values == null) throw new IllegalArgumentException("values==null");
        // todo не эффективная реализация
        Object[] res = new Object[]{this};
        values.each(a -> {
            var r = (ImListLinkedBase<A>) res[0];
            res[0] = r.append(a);
        });
        return (ImListLinkedBase<A>) res[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public ImListLinkedBase<A> prepend(PositionalRead<A> values) {
        if (values == null) throw new IllegalArgumentException("values==null");
        Object[] res = new Object[]{this};
        values.each(a -> {
            var r = (ImListLinkedBase<A>) res[0];
            res[0] = r.prepend(a);
        });
        return (ImListLinkedBase<A>) res[0];
    }

    public static class ImListLinkedIterator<A> implements Iterator<A> {
        private ImListLinkedBase<A> imList;

        public ImListLinkedIterator(
            ImListLinkedBase<A> imList
        ) {
            this.imList = imList;
        }

        @Override
        public boolean hasNext() {
            if (imList == null) return false;
            return !imList.isNil();
        }

        @Override
        public A next() {
            if (imList == null) throw new NoSuchElementException();
            if (imList.isNil()) throw new NoSuchElementException();
            var value = imList.value;
            imList = imList.next;
            return value;
        }
    }

    @Override
    public Iterator<A> iterator() {
        return new ImListLinkedIterator<>(this);
    }
}
