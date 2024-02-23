package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.ast.impl.Ident.identifier;

/**
 * Массив
 * <span style="color: blue; font-family:mono">
 * 'array' ('[' (arrayIndex)? (',' (arrayIndex)?)* ']')? 'of' arraySubType
 * </span>
 *
 * @param indexes  тип индекса, размерность, если 0 - элементов, тогда динамический
 * @param subType  тип элемента массива
 * @param packed   флаг packed // TODO разобраться что за packed
 * @param comments Комментарии
 * @param position Позиция в исходниках
 */
public record ArrayTypeAst(
    ImList<ArrayIndexAst> indexes,
    ArraySubTypeAst subType,
    boolean packed,
    SourcePosition position,
    ImList<Comment> comments
) implements StructuredTypeAst,
             TypeDeclAst,
             SrcPos,
             Commented<ArrayTypeAst> {

    public static ArrayTypeAst of(DelphiParser.ArrayTypeContext ctx, boolean packed) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var arrIdx = ImList.of(ctx.arrayIndex()).map(ArrayIndexAst::of);
        var arrSt = ArraySubTypeAst.of(ctx.arraySubType());
        return new ArrayTypeAst(
            arrIdx,
            arrSt,
            packed,
            SourcePosition.of(ctx),
            ImList.of()
        );
    }

    @Override
    public ArrayTypeAst withComments(ImList<Comment> comments) {
        return new ArrayTypeAst(
            indexes,
            subType,
            packed,
            position,
            comments
        );
    }

    /**
     * Тип элемента массива
     */
    sealed public static interface ArraySubTypeAst {

        public static ArraySubTypeAst of(DelphiParser.ArraySubTypeContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.CONST() != null && !ctx.CONST().getText().isBlank()) {
                return new Const(SourcePosition.of(ctx));
            }

            if (ctx.typeDecl() != null) {
                return new TypeRef(
                    TypeDeclAst.of(ctx.typeDecl()),
                    SourcePosition.of(ctx)
                );
            }

            throw AstParseError.unExpected(ctx);
        }

        /**
         * Константа
         *
         * @param position позиция в исходниках
         */
        record Const(SourcePosition position) implements ArraySubTypeAst,
                                                         SrcPos {
        }

        /**
         * Ссылка на тип
         *
         * @param decl     тип
         * @param position позиция в исходниках
         */
        record TypeRef(TypeDeclAst decl, SourcePosition position) implements ArraySubTypeAst,
                                                                             SrcPos {
        }
    }

    /**
     * Тип индекса массива
     */
    sealed public static interface ArrayIndexAst extends AstNode,
                                                         SrcPos {
        public static ArrayIndexAst of(DelphiParser.ArrayIndexContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.typeId() != null && !ctx.typeId().isEmpty()) {
                var id = identifier(ctx.typeId());
                return new ArrayIndexTypeAst(id, SourcePosition.of(ctx));
            }

            if (ctx.expression() != null && ctx.expression().size() == 2) {
                return new ArrayIndexRangeAst(
                    ExpressionAst.of(ctx.expression(0)),
                    ExpressionAst.of(ctx.expression(1)),
                    SourcePosition.of(ctx)
                );
            }

            throw AstParseError.unExpected(ctx);
        }

        /**
         * Элементы массива
         *
         * @param from     с какого элемента
         * @param to       по какой элемент включительно
         * @param position позиция в исходниках
         */
        record ArrayIndexRangeAst(ExpressionAst from, ExpressionAst to, SourcePosition position)
            implements ArrayIndexAst,
                       SrcPos {
        }

        /**
         * Ссылка на тип
         *
         * @param typeId   тип
         * @param position позиция в исходниках
         */
        record ArrayIndexTypeAst(ImList<String> typeId, SourcePosition position)
            implements ArrayIndexAst,
                       SrcPos {
        }
    }
}
