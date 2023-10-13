package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Объявление типа
 * @param typeIdent идентификатор типа
 * @param typeDecl описание типа
 * @param position позиция в исходнике
 */
public record TypeDeclarationAst(
    TypeIdentAst typeIdent,
    TypeDeclAst typeDecl,
    SourcePosition position
) implements AstNode, SrcPos {
    @Override
    public TypeDeclarationAst astUpdate(AstUpdate.UpdateContext ctx) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var t1= typeIdent.astUpdate(ctx);
        var t2 = typeDecl.astUpdate(ctx);
        //noinspection ConstantConditions
        if( t1==typeIdent && t2==typeDecl )return this;
        return new TypeDeclarationAst(t1,t2,position);
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return ImListLinked.of(typeIdent, typeDecl);
    }

    static ImList<TypeDeclarationAst> of(Iterable<DelphiParser.TypeDeclarationContext> decs){
        return ImListLinked.of(decs).map(TypeDeclarationAst::of);
    }

    static TypeDeclarationAst of(DelphiParser.TypeDeclarationContext dec) {
        return new TypeDeclarationAst(
            TypeIdentAst.of(dec),
            TypeDeclAst.of(dec.typeDecl()),
            SourcePosition.of(dec)
        );
    }
}
