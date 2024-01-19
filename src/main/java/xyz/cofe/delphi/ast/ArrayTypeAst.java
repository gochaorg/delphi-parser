package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Массив
 *
 * @param indexes тип индекса
 * @param subType тип элемента массива
 * @param packed  флаг packed // TODO разобраться что за packed
 */
public record ArrayTypeAst(
    ImList<ArrayIndexAst> indexes,
    ArraySubTypeAst subType,
    boolean packed,
    SourcePosition position
) implements StructuredTypeAst, TypeDeclAst, SrcPos {
    @Override
    public ArrayTypeAst astUpdate(AstUpdate.UpdateContext updateCtx) {
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(indexes).append(subType);
    }

    sealed public static interface ArraySubTypeAst extends AstNode {
        @Override
        ArraySubTypeAst astUpdate(AstUpdate.UpdateContext ctx);

        record Const() implements ArraySubTypeAst {
            @Override
            public Const astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }
        }

        record TypeRef(TypeDeclAst decl) implements ArraySubTypeAst {
            @Override
            public TypeRef astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImListLinked.of(decl);
            }
        }
    }

    sealed public static interface ArrayIndexAst extends AstNode {
        @Override
        ArrayIndexAst astUpdate(AstUpdate.UpdateContext ctx);

        record ArrayIndexRangeAst(ExpressionAst from, ExpressionAst to) implements ArrayIndexAst {
            @Override
            public ArrayIndexRangeAst astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImListLinked.of(from, to);
            }
        }

        record ArrayIndexTypeAst(ImList<String> typeId) implements ArrayIndexAst {
            @Override
            public ArrayIndexTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }
        }
    }
}
