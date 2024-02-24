package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Секция определения типов
 * @param types типы
 * @param position позиция в исходниках
 */
public record TypeSectionAst(
    ImList<TypeDeclarationAst> types,
    SourcePosition position
) implements InterfaceDecl, ClassItemAst, SrcPos, RecordItemAst, DeclSectionAst
{
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
