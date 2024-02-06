package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * "Запись" record
 *
 * <p>
 * Тут какой-то беспорядок в оформлении синтаксиса
 *
 * <pre>
 * recordDecl                   : simpleRecord
 *                              | variantRecord
 *                              ;
 * simpleRecord                 : 'record' (recordField)* (recordItem)* 'end'
 *                              ;
 * variantRecord                : 'record' (recordField)* recordVariantSection 'end'
 *                              ;
 * recordItem                   : visibility     //ADDED
 *                              | classMethod
 *                              | classProperty
 *                              | constSection
 *                              | typeSection
 *                              | recordField
 *                              | ('class')? varSection
 *                              ;
 * recordField                  : identList ':' typeDecl (hintingDirective)* (';')?  //CHANGED not needed ; at the end
 *                              ;
 * recordVariantField           : identList ':' typeDecl (hintingDirective)* (';') ?
 *                              ;
 * recordVariantSection         : 'case' (ident ':')? typeDecl 'of' (recordVariant | ';') (recordVariant | ';')*
 *                              ;
 * recordVariant                : constExpression (',' constExpression)* ':' '(' (recordVariantField)* ')'   //CHANGED to recordVariantField from recordField
 *                              ;
 * </pre>
 * @param items
 */
@SuppressWarnings("rawtypes")
public record RecordAst(
    ImList<RecordItemAst> items,
    SourcePosition position,
    ImList<Comment> comments
) implements AstNode, AstUpdate, Commented<RecordAst>, SrcPos, TypeDeclAst, StructuredTypeAst
{
    @SuppressWarnings("unchecked")
    @Override
    public RecordAst astUpdate(AstUpdate.UpdateContext ctx) {
        var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
            cc.commenting(this) : this;
        var itms = ctx.updateUnsafe(items);
        if( cmts==this && itms.isEmpty() )return this;
        return new RecordAst(
            itms.orElse((ImList)items),
            position,
            comments);
    }

    @Override
    public RecordAst withComments(ImList<Comment> comments) {
        if( comments==null )throw new IllegalArgumentException("comments==null");
        return new RecordAst(items,position,comments);
    }

    public static RecordAst of(DelphiParser.SimpleRecordContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        ImList<RecordItemAst> fields =
            ctx.recordField()!=null && !ctx.recordField().isEmpty()
            ? ImList.of(ctx.recordField()).fmap(RecordFieldAst::of)
                : ImList.of();
        ImList<RecordItemAst> items =
            ctx.recordItem()!=null && !ctx.recordItem().isEmpty()
            ? ImList.of(ctx.recordItem()).fmap(RecordItemAst::of)
                : ImList.of();
        return new RecordAst(
            fields.append(items),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}