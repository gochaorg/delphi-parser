package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.StreamSupport;

/**
 * Добавляет комментарии в дерево
 */
@SuppressWarnings("rawtypes")
public class CommentInjector {
    private final Comparator<SourcePosition> sourcePositionComparator = new Comparator<SourcePosition>() {
        @Override
        public int compare(SourcePosition a, SourcePosition b) {
            if( a.equals(b) )return 0;
            if( a.before(b) )return -1;
            if( b.before(a) )return 1;
            return 0;
        }
    };

    private Map<Commented,List<Comment>> injectionsOf(PascalFileAst.Unit unit){
        var result = new HashMap<Commented,List<Comment>>();

        var breakPointsMap = new TreeMap<SourcePosition,Commented>(sourcePositionComparator);

        StreamSupport.stream(unit.tree().spliterator(),false).map(
            treePath -> treePath.last().flatMap(
                n -> n instanceof Commented s ?
                    Optional.of(Tuple2.of(s.position(), s)) : Optional.empty()
            )
        ).flatMap(Optional::stream).forEach(cPos -> breakPointsMap.put(cPos._1(), cPos._2()));

        if( breakPointsMap.isEmpty() )return result;

        var commentsMap = new TreeMap<SourcePosition,Comment>(sourcePositionComparator);
        unit.comments().each(cmnt -> {
            commentsMap.put(cmnt.position(),cmnt);
        });

        BiConsumer<Commented,List<Comment>> prepareInject = result::put;

        if(!breakPointsMap.isEmpty()) {
            var from = breakPointsMap.keySet().iterator().next();
            for (var cmntToPair : breakPointsMap.entrySet()) {
                var to = cmntToPair.getValue().position();
                var cmntTo = cmntToPair.getValue();

                if( from.equals(to) ){
                    var subCommentMap = commentsMap.headMap(cmntTo.position(),false);
                    if( !subCommentMap.isEmpty() ) prepareInject.accept( cmntTo, subCommentMap.values().stream().toList() );
                }else {
                    var prevKey = breakPointsMap.lowerKey(to);
                    var subCommentMap = prevKey != null ? commentsMap.subMap(prevKey, false, to, false) : commentsMap.headMap(to, false);
                    if( !subCommentMap.isEmpty() ) prepareInject.accept( cmntTo, subCommentMap.values().stream().toList() );
                }

                from = to;
            }
        }

        return result;
    }

    /**
     * Добавление комментариев в узлы дерева
     * @param unit AST дерево
     * @return обновленное AST дерево
     */
    public PascalFileAst.Unit inject(PascalFileAst.Unit unit){
        if( unit==null ) throw new IllegalArgumentException("unit==null");

        var injections = injectionsOf(unit);
        //noinspection rawtypes
        var injectionsMap = new HashMap<Commented, ImList<Comment>>();
        injections.forEach( (k,v) -> {
            injectionsMap.put(k, ImListLinked.of(v));
        });

        var comments = new AstUpdate.Commenting(injectionsMap);
        return unit.astUpdate(comments);
    }
}
