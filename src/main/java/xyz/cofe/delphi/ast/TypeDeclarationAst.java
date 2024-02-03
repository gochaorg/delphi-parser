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
 * @param comments Комментарий к типу
 */
public record TypeDeclarationAst(
    TypeIdentAst typeIdent,
    TypeDeclAst typeDecl,
    SourcePosition position,
    ImList<Comment> comments,
    ImList<CustomAttributeAst> attributes
) implements AstNode, SrcPos, Commented<TypeDeclarationAst>, AstUpdate<TypeDeclarationAst> {
    @Override
    public TypeDeclarationAst astUpdate(AstUpdate.UpdateContext ctx) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var t1= typeIdent.astUpdate(ctx);
        var t2 = typeDecl.astUpdate(ctx);
        var t3 = ctx.update(attributes);

        var res = this;
        if( ctx instanceof AstUpdate.CommentingContext cc ){
            res = cc.commenting(res);
        }

        //noinspection ConstantConditions
        if( t1==typeIdent
            && t2==typeDecl
            && res.comments==this.comments
            && t3.isEmpty()
        )return this;
        return new TypeDeclarationAst(
            t1,
            t2,
            position,
            res.comments,
            t3.orElse(attributes)
        );
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return ImListLinked.of(typeIdent, typeDecl);
    }

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
