package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Объявление типа
 * @param typeIdent идентификатор типа
 * @param typeDecl описание типа
 */
public record TypeDeclaration(
    TypeIdent typeIdent,
    TypeDecl typeDecl
) implements AstNode {
    @Override
    public ImList<? extends AstNode, ?> nestedAstNodes() {
        return ImListLinked.of(typeIdent, typeDecl);
    }

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
