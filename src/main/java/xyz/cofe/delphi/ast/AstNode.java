package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Абстрактный узел, нужен для доступа к дочерним элементам
 */
public interface AstNode
{
    interface UpdateContext {}

    /**
     * Обновление поддерева узлов
     * @param ctx контекст обновления
     * @return новое поддерево, должно быть того же типа что реализует AstNode
     * @see AstUpdate
     */
    default Object astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ImList<AstNode> upcast(ImList<? extends AstNode> itms ) {
        if( itms==null ) throw new IllegalArgumentException("itms==null");
        List lst = itms.toList();
        return ImListLinked.of(lst);
    }

    @SuppressWarnings({"OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType"})
    public static ImList<AstNode> upcast(Optional<? extends AstNode> item){
        if( item==null ) throw new IllegalArgumentException("item==null");
        if( item.isEmpty() )return ImListLinked.of();
        var ast = item.get();
        return ImListLinked.of(ast);
    }

    public static ImList<AstNode> upcast(AstNode node){
        if( node==null ) throw new IllegalArgumentException("node==null");
        return ImListLinked.of(node);
    }

    /**
     * Должен быть реализован в дочерних классах, для доступа к дочерним узлам
     * @return дочерние узлы
     */
    default ImList<? extends AstNode> nestedAstNodes() {
        return ImListLinked.of();
    }

    /**
     * Итератор по дочерним узлам, включая вложенные
     * @return итератор, где элемент - путь к узлу, в пути последовательность элементов обычная (0 - корень)
     */
    default Iterable<ImList<AstNode>> tree(){
        return () -> new AstTreeIterator(this);
    }
}
