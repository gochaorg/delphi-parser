package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Секция определения типов
 * @param types типы
 * @param position позиция в исходниках
 */
public record TypeSectionAst(
    ImList<TypeDeclarationAst> types,
    SourcePosition position
) implements InterfaceDecl, ClassItemAst, SrcPos
{
    @Override
    public TypeSectionAst astUpdate(AstUpdate.UpdateContext ctx) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var changed = new boolean[]{ false };
        var types1 = types.foldRight(ImListLinked.<TypeDeclarationAst>of(), (acc, it) -> {
            var it1 = it.astUpdate(ctx);
            if( it1!=it )changed[0] = true;
            return acc.prepend(it1);
        });

        if( changed[0] )return new TypeSectionAst(types1,position);
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(types);
    }

    public String toString(){
        var sb = new StringBuilder();
        sb.append("type(section):\n");
        types.each(t -> {
            sb.append(indent("  ",t.toString()));
        });
        return sb.toString();
    }

    public static TypeSectionAst of(DelphiParser.TypeSectionContext ctx){
        return new TypeSectionAst(
            TypeDeclarationAst.of(ctx.typeDeclaration()),
            SourcePosition.of(ctx)
        );
    }
}
