package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record SetTypeAst(TypeDeclAst type, SourcePosition position, ImList<Comment> comments )
    implements TypeDeclAst, AstUpdate<SetTypeAst>, Commented<SetTypeAst>, SrcPos, StructuredTypeAst
{
    public static SetTypeAst of(DelphiParser.SetTypeContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.typeDecl()==null )throw AstParseError.unExpected(ctx);
        return new SetTypeAst(
            TypeDeclAst.of(ctx.typeDecl()),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return ImList.of(type);
    }

    @Override
    public SetTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
            cc.commenting(this) : this;
        var t = type.astUpdate(ctx);
        if( t==type && cmts==this )return this;
        return new SetTypeAst(
            t,
            position,
            cmts.comments
        );
    }

    @Override
    public SetTypeAst withComments(ImList<Comment> comments) {
        if( comments==null ) throw new IllegalArgumentException("comments==null");
        return new SetTypeAst(type,position,comments);
    }
}
