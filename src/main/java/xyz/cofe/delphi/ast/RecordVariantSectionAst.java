package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record RecordVariantSectionAst(
    Optional<String> ident,
    TypeDeclAst type,
    ImList<RecordVariantAst> variants,
    SourcePosition position,
    ImList<Comment> comments
)
    implements SrcPos,
               Commented<RecordVariantSectionAst>,
               RecordItemAst {
    @Override
    public RecordVariantSectionAst withComments(ImList<Comment> comments) {
        if (comments == null) throw new IllegalArgumentException("comments==null");
        return new RecordVariantSectionAst(ident, type, variants, position, comments);
    }

    public static RecordVariantSectionAst of(DelphiParser.RecordVariantSectionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.typeDecl() == null || ctx.typeDecl().isEmpty()) throw AstParseError.unExpected(ctx);

        var id = ctx.ident() != null && !ctx.ident().isEmpty() ?
            Optional.of(ctx.ident().getText()) :
            Optional.<String>empty();
        var type = TypeDeclAst.of(ctx.typeDecl());
        var variants =
            ctx.recordVariant() != null && !ctx.recordVariant().isEmpty() ?
                ImList.of(ctx.recordVariant()).map(RecordVariantAst::of) : ImList.<RecordVariantAst>of();

        return new RecordVariantSectionAst(id, type, variants, SourcePosition.of(ctx), ImList.of());
    }
}
