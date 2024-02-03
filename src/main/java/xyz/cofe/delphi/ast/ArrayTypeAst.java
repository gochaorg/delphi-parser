package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.ast.impl.Ident.identifier;

/**
 * Массив
 *
 * <pre>
 * 'array' ('[' (arrayIndex)? (',' (arrayIndex)?)* ']')? 'of' arraySubType
 * </pre>
 *
 * @param indexes тип индекса
 * @param subType тип элемента массива
 * @param packed  флаг packed // TODO разобраться что за packed
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
             Commented<ArrayTypeAst>,
             AstUpdate<ArrayTypeAst>
{

    public static ArrayTypeAst of(DelphiParser.ArrayTypeContext ctx, boolean packed){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
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
    public ArrayTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
        var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
            cc.commenting(this) : this;

        var idx = ctx.update(indexes);
        var st = subType.astUpdate(ctx);

        if (cmts == this && st == subType && idx.isEmpty())
            return this;

        return new ArrayTypeAst(
            idx.orElse(indexes),
            st,
            packed,
            position,
            cmts.comments
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

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(indexes).append(subType);
    }

    sealed public static interface ArraySubTypeAst extends AstNode,
                                                           AstUpdate<ArraySubTypeAst> {

        public static ArraySubTypeAst of(DelphiParser.ArraySubTypeContext ctx){
            if( ctx==null ) throw new IllegalArgumentException("ctx==null");
            if( ctx.CONST()!=null && !ctx.CONST().getText().isBlank() ){
                return new Const(SourcePosition.of(ctx));
            }

            if( ctx.typeDecl()!=null ){
                return new TypeRef(
                    TypeDeclAst.of(ctx.typeDecl()),
                    SourcePosition.of(ctx)
                );
            }

            throw AstParseError.unExpected(ctx);
        }

        @Override
        ArraySubTypeAst astUpdate(AstUpdate.UpdateContext ctx);

        record Const(SourcePosition position) implements ArraySubTypeAst,
                                                         SrcPos {
            @Override
            public Const astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }
        }

        record TypeRef(TypeDeclAst decl, SourcePosition position) implements ArraySubTypeAst,
                                                                             SrcPos {
            @Override
            public TypeRef astUpdate(AstUpdate.UpdateContext ctx) {
                var d = decl.astUpdate(ctx);
                if (d == decl) return this;
                return new TypeRef(d, position);
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImListLinked.of(decl);
            }
        }
    }

    sealed public static interface ArrayIndexAst extends AstNode,
                                                         AstUpdate<ArrayIndexAst>,
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

        @Override
        ArrayIndexAst astUpdate(AstUpdate.UpdateContext ctx);

        record ArrayIndexRangeAst(ExpressionAst from, ExpressionAst to, SourcePosition position)
            implements ArrayIndexAst,
                       SrcPos {
            @Override
            public ArrayIndexRangeAst astUpdate(AstUpdate.UpdateContext ctx) {
                var frm = from.astUpdate(ctx);
                var t2 = to.astUpdate(ctx);
                if (frm == from && t2 == to) return this;
                return new ArrayIndexRangeAst(frm, t2, position);
            }

            @Override
            public ImList<? extends AstNode> nestedAstNodes() {
                return ImList.of(from, to);
            }
        }

        record ArrayIndexTypeAst(ImList<String> typeId, SourcePosition position)
            implements ArrayIndexAst,
                       SrcPos {
            @Override
            public ArrayIndexTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
                return this;
            }
        }
    }
}
