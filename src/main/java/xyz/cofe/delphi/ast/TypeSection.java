package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.impl.Indent.indent;

public record TypeSection(
    ImList<TypeDeclaration,?> types
) implements InterfaceDecl, ClassItem
{
    public String toString(){
        var sb = new StringBuilder();
        sb.append("type(section):\n");
        types.forEach(t -> {
            sb.append(indent("  ",t.toString()));
        });
        return sb.toString();
    }

    public static TypeSection of(DelphiParser.TypeSectionContext ctx){
        return new TypeSection(
            TypeDeclaration.of(ctx.typeDeclaration())
        );
    }
}
