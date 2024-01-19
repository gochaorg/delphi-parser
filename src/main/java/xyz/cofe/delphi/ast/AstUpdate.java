package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Map;
import java.util.Optional;

/**
 * Обновление узла AST
 * @param <SELF> Тип узла AST
 */
public interface AstUpdate<SELF> {
    /**
     * Обновление списка узлов
     * @param list список узлов
     * @param ctx контекст обновления
     * @return обновленные узлы или none - если небыло обновленных узлов
     * @param <A> тип узла
     */
    public static <A extends AstUpdate<A>> Optional<ImList<A>> astUpdates(ImList<A> list, UpdateContext ctx) {
        if( list==null ) throw new IllegalArgumentException("list==null");

        var self = ctx;
        if( list.size()==0 )return Optional.empty();
        var changed = new boolean[]{ false };
        var newList = list.foldRight(ImListLinked.<A>of(), (acc,it) -> {
            var it1 = it.astUpdate(self);
            if( it1!=it ) changed[0] = true;
            return acc.prepend(it1);
        });
        if( changed[0] )return Optional.of(newList);
        return Optional.empty();
    }

    /**
     * Контекст обновления
     */
    interface UpdateContext {
        default  <A extends AstUpdate<A>> Optional<ImList<A>> update(ImList<A> list) {
            if( list==null ) throw new IllegalArgumentException("list==null");

            var self = this;
            if( list.size()==0 )return Optional.empty();
            var changed = new boolean[]{ false };
            var newList = list.foldRight(ImListLinked.<A>of(), (acc,it) -> {
                var it1 = it.astUpdate(self);
                if( it1!=it ) changed[0] = true;
                return acc.prepend(it1);
            });
            if( changed[0] )return Optional.of(newList);
            return Optional.empty();
        }

        default Optional<ImList<Object>> updateUnsafe(ImList<?> list){
            if( list==null ) throw new IllegalArgumentException("list==null");

            var self = this;
            if( list.size()==0 )return Optional.empty();

            var changed = new boolean[]{ false };
            var newList = list.foldRight(ImListLinked.<Object>of(), (acc,it) -> {
                //noinspection rawtypes
                if( it instanceof AstUpdate upd ){
                    var it1 = upd.astUpdate(self);
                    if( it1!=it )changed[0] = true;
                    return acc.prepend(it1);
                }
                return acc.prepend(it);
            });
            if( changed[0] )return Optional.of(newList);
            return Optional.empty();
        }
    }

    /**
     * Комментировние узлов
     */
    interface CommentingContext extends UpdateContext {
        /**
         * Установка комментария для узла
         * @param a узел
         * @return узел с комментарием
         * @param <A> тип узла
         */
        <A extends Commented<A>> A commenting(A a);
    }

    /**
     * Контекст комментариев
     */
    static class Commenting implements CommentingContext {
        @SuppressWarnings("rawtypes")
        private final Map<Commented, ImList<Comment>> comments;

        @SuppressWarnings("rawtypes")
        public Commenting(Map<Commented, ImList<Comment>> comments){
            if( comments==null ) throw new IllegalArgumentException("comments==null");
            this.comments = comments;
        }

        @Override
        public <A extends Commented<A>> A commenting(A a) {
            if( a==null ) throw new IllegalArgumentException("a==null");
            var newComments = comments.get(a);
            if( newComments==null )return a;

            return a.withComments(newComments);
        }
    }

    @SuppressWarnings("unchecked")
    default SELF astUpdate(UpdateContext updateCtx) {
        return (SELF) this;
    }
}
