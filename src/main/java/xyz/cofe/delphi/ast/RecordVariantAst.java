package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record RecordVariantAst(
    ImList<ConstSectionAst.ConstExpression> determinants,
    ImList<RecordFieldAst> fields,
    SourcePosition position,
    ImList<Comment> comments
) implements Commented<RecordVariantAst>, SrcPos
{
    @Override
    public RecordVariantAst withComments(ImList<Comment> comments) {
        if( comments==null )throw new IllegalArgumentException("comments==null");
        return new RecordVariantAst(determinants,fields,position,comments);
    }

    public static RecordVariantAst of(DelphiParser.RecordVariantContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        if( ctx.constExpression()==null || ctx.constExpression().isEmpty() )
            throw AstParseError.unExpected(ctx);
        var determ = ImList.of(ctx.constExpression()).map(ConstSectionAst.ConstExpression::of);
        ImList<RecordFieldAst> flds =
            ctx.recordVariantField()!=null && !ctx.recordVariantField().isEmpty() ?
                ImList.of(ctx.recordVariantField()).fmap(RecordFieldAst::of) : ImList.<RecordFieldAst>of();
        return new RecordVariantAst(determ,flds,SourcePosition.of(ctx),ImList.of());
    }
}
