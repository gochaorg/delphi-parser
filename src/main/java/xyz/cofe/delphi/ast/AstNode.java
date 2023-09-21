package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public interface AstNode
{
    interface UpdateContext {}

    default Object astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ImList<AstNode,?> upcast(ImList<? extends AstNode,?> itms ) {
        if( itms==null ) throw new IllegalArgumentException("itms==null");
        List lst = itms.toList();
        return ImListLinked.of(lst);
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public static ImList<AstNode,?> upcast(Optional<? extends AstNode> item){
        if( item==null ) throw new IllegalArgumentException("item==null");
        if( item.isEmpty() )return ImListLinked.of();
        var ast = item.get();
        return ImListLinked.of(ast);
    }

    public static ImList<AstNode,?> upcast(AstNode node){
        if( node==null ) throw new IllegalArgumentException("node==null");
        return ImListLinked.of(node);
    }

    default ImList<? extends AstNode,?> nestedAstNodes() {
        return ImListLinked.of();
    }

    default Iterable<ImList<AstNode,?>> tree(){
        return () -> new AstTreeIterator(this);
    }
}
