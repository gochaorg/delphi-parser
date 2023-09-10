package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

public record TypeDeclaration(
    TypeIdent typeIdent,
    TypeDecl typeDecl
) {
    static ImList<TypeDeclaration,?> of(Iterable<DelphiParser.TypeDeclarationContext> decs){
        return ImListLinked.of(decs).map(TypeDeclaration::of);
    }

    static TypeDeclaration of(DelphiParser.TypeDeclarationContext dec) {
        return new TypeDeclaration(
            TypeIdent.of(dec),
            TypeDecl.of(dec.typeDecl())
        );
    }
}
