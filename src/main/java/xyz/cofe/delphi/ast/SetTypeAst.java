package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record SetTypeAst(TypeDeclAst type, SourcePosition position, ImList<Comment> comments )
    implements TypeDeclAst, Commented<SetTypeAst>, SrcPos, StructuredTypeAst
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
    public SetTypeAst withComments(ImList<Comment> comments) {
        if( comments==null ) throw new IllegalArgumentException("comments==null");
        return new SetTypeAst(type,position,comments);
    }
}
