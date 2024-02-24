package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record BodyAst(
    ImList<DeclSectionAst> declarations,
    BlockBodyAst body,
    SourcePosition position,
    ImList<Comment> comments
) implements WithComments<BodyAst>, ProcDeclAst.ProcBody {
    public static BodyAst of(DelphiParser.BlockContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        return new BodyAst(
            ImList.of(ctx.declSection()).map(DeclSectionAst::of),
            BlockBodyAst.of(ctx.blockBody()),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
