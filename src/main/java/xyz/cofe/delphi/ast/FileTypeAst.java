package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record FileTypeAst(Optional<TypeDeclAst> type, SourcePosition position, ImList<Comment> comments)
    implements TypeDeclAst,
               StructuredTypeAst,
               Commented<FileTypeAst> {
    public static FileTypeAst of(DelphiParser.FileTypeContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var t = ctx.typeDecl() != null && !ctx.typeDecl().isEmpty()
            ? Optional.of(TypeDeclAst.of(ctx.typeDecl()))
            : Optional.<TypeDeclAst>empty();
        return new FileTypeAst(t, SourcePosition.of(ctx), ImList.of());
    }
    @Override
    public FileTypeAst withComments(ImList<Comment> comments) {
        if (comments == null) throw new IllegalArgumentException("comments==null");
        return new FileTypeAst(type, position, comments);
    }
}
