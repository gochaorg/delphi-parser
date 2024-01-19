package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Iterator;

/**
 * Итератор по дереву AST
 */
public class AstTreeIterator implements Iterator<ImList<AstNode>> {
    private ImList<ImList<AstNode>> workSet;

    public AstTreeIterator(ImList<ImList<AstNode>> workSet){
        if( workSet==null ) throw new IllegalArgumentException("workSet==null");
        this.workSet = workSet;
    }

    public AstTreeIterator(AstNode ... roots){
        if( roots==null ) throw new IllegalArgumentException("roots==null");
        this.workSet = ImListLinked.of(roots).map(ImListLinked::of);
    }

    @Override
    public boolean hasNext() {
        return workSet.size()!=0;
    }

    @Override
    public ImList<AstNode> next() {
        if(workSet.size()==0)return null;
        var nextSet = workSet.tail();
        var res = workSet.head().get();
        if( res.last().isPresent() ){
            var last = res.head().get();
            ImList<ImList<AstNode>> inst = last.nestedAstNodes().map( n -> res.prepend(n) );
            nextSet = nextSet.prepend(inst.reverse());
        }

        workSet = nextSet;
        return res.reverse();
    }
}
