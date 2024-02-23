package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Объявление типа
 * @param typeIdent идентификатор типа
 * @param typeDecl описание типа
 * @param position позиция в исходнике
 * @param comments Комментарий к типу
 */
public record TypeDeclarationAst(
    TypeIdentAst typeIdent,
    TypeDeclAst typeDecl,
    SourcePosition position,
    ImList<Comment> comments,
    ImList<CustomAttributeAst> attributes
) implements SrcPos, Commented<TypeDeclarationAst> {
    static ImList<TypeDeclarationAst> of(Iterable<DelphiParser.TypeDeclarationContext> decs){
        return ImListLinked.of(decs).map(TypeDeclarationAst::of);
    }

    static TypeDeclarationAst of(DelphiParser.TypeDeclarationContext dec) {
        if( dec==null ) throw new IllegalArgumentException("dec==null");
        return new TypeDeclarationAst(
            TypeIdentAst.of(dec),
            TypeDeclAst.of(dec.typeDecl()),
            SourcePosition.of(dec),
            ImList.of(),
            dec.customAttribute()!=null && !dec.customAttribute().isEmpty() ?
                ImList.of(dec.customAttribute()).map(CustomAttributeAst::of) :
                ImList.of()
        );
    }

    @Override
    public TypeDeclarationAst withComments(ImList<Comment> comments) {
        if( comments==null ) throw new IllegalArgumentException("comments==null");
        return new TypeDeclarationAst(
            typeIdent, typeDecl, position, comments, attributes
        );
    }
}
