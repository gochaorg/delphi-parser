package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

public record TypeAliasAst(ImList<String> name, ImList<TypeDeclAst> genericParams, SourcePosition position,
                           ImList<Comment> comments)
    implements TypeDeclAst,
               SrcPos,
               Commented<TypeAliasAst> {
    @Override
    public TypeAliasAst withComments(ImList<Comment> comments) {
        return this;
    }
}
