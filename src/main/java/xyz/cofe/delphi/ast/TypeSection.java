package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Секция определения типов
 * @param types типы
 */
public record TypeSection(
    ImList<TypeDeclaration,?> types,
    SourcePosition position
) implements InterfaceDecl, ClassItem, AstNode, SrcPos
{
    @Override
    public ImList<? extends AstNode, ?> nestedAstNodes() {
        return upcast(types);
    }

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
            TypeDeclaration.of(ctx.typeDeclaration()),
            SourcePosition.of(ctx)
        );
    }
}
