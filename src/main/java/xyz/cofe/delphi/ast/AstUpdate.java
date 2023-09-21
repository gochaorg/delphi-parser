package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Map;
import java.util.Optional;

public interface AstUpdate<SELF> {
    public static <A extends AstUpdate<A>> Optional<ImList<A,?>> astUpdates(ImList<A, ?> list, UpdateContext ctx) {
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

    interface UpdateContext {
        default  <A extends AstUpdate<A>> Optional<ImList<A,?>> update(ImList<A,?> list) {
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
    }

    interface CommentingContext extends UpdateContext {
        <A extends Commented<A>> A commenting(A a);
    }

    static class Commenting implements CommentingContext {
        @SuppressWarnings("rawtypes")
        private final Map<Commented, ImList<Comment,?>> comments;

        @SuppressWarnings("rawtypes")
        public Commenting(Map<Commented, ImList<Comment,?>> comments){
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
