package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record RecordFieldAst(
    String name,
    TypeDeclAst type,
    ImList<FDirective.Hinting> directives,
    SourcePosition position,
    ImList<Comment> comments
) implements AstNode, AstUpdate<RecordFieldAst>, Commented<RecordFieldAst>, SrcPos, RecordItemAst
{
    @Override
    public RecordFieldAst astUpdate(AstUpdate.UpdateContext ctx) {
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        var typ = type.astUpdate(ctx);
        var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
            cc.commenting(this) : this;
        if( typ==type && cmts==this )return this;
        return new RecordFieldAst(
            name,
            typ,
            directives,
            position,
            cmts.comments
        );
    }

    @Override
    public RecordFieldAst withComments(ImList<Comment> comments) {
        if( comments==null )throw new IllegalArgumentException("comments==null");
        return new RecordFieldAst(
            name,
            type,
            directives,
            position,
            comments
        );
    }

    public static ImList<RecordItemAst> of(DelphiParser.RecordFieldContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        if( ctx.identList()==null )throw AstParseError.unExpected(ctx);
        if( ctx.identList().paramName()==null )throw AstParseError.unExpected(ctx);
        if( ctx.typeDecl()==null )throw AstParseError.unExpected(ctx);

        var typeDecl = TypeDeclAst.of(ctx.typeDecl());
        var directives =
            ctx.hintingDirective()!=null && !ctx.hintingDirective().isEmpty() ?
                ImList.of(ctx.hintingDirective()).map(FDirective::of) :
                ImList.<FDirective.Hinting>of();

        return ImList.of(ctx.identList().paramName()).map(RuleContext::getText).map(name ->
            new RecordFieldAst(name,typeDecl,directives,SourcePosition.of(ctx),ImList.of()));
    }

    public static ImList<RecordFieldAst> of(DelphiParser.RecordVariantFieldContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        if( ctx.identList()==null )throw AstParseError.unExpected(ctx);
        if( ctx.identList().paramName()==null )throw AstParseError.unExpected(ctx);
        if( ctx.typeDecl()==null )throw AstParseError.unExpected(ctx);

        var typeDecl = TypeDeclAst.of(ctx.typeDecl());
        var directives =
            ctx.hintingDirective()!=null && !ctx.hintingDirective().isEmpty() ?
                ImList.of(ctx.hintingDirective()).map(FDirective::of) :
                ImList.<FDirective.Hinting>of();

        return ImList.of(ctx.identList().paramName()).map(RuleContext::getText).map(name ->
            new RecordFieldAst(name,typeDecl,directives,SourcePosition.of(ctx),ImList.of()));
    }
}
