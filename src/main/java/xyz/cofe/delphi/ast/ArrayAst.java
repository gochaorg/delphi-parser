package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Массив
 *
 * @param indexes тип индекса
 * @param subType тип элемента массива
 * @param packed  флаг packed // TODO разобраться что за packed
 */
public record ArrayAst(
    ImList<ArrayIndexAst> indexes,
    ArraySubType subType,
    boolean packed,
    SourcePosition position
) implements StructuredTypeAst, TypeDeclAst, SrcPos {
    @Override
    public ArrayAst astUpdate(AstUpdate.UpdateContext updateCtx) {
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(indexes).append(subType);
    }
}
