package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.stream.Collectors;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Экспортируемое api
 *
 * @param uses         какие используются модули
 * @param declarations Объявляемое API
 */
public record UnitInterfaceAst(
    ImList<NamespaceAst> uses,
    ImList<InterfaceDeclAst> declarations
) {
    static UnitInterfaceAst of(DelphiParser.UnitInterfaceContext unt) {
        if (unt == null) throw new IllegalArgumentException("unt==null");

        ImList<NamespaceAst> uses =
            unt.usesClause() == null
                ? ImList.of()
                : unt.usesClause().namespaceNameList() == null
                ? ImList.of()
                : unt.usesClause().namespaceNameList().namespaceName() == null
                ? ImList.of()
                : ImList.of(unt.usesClause().namespaceNameList().namespaceName().stream().map(NamespaceAst::of).collect(Collectors.toList()));

        return new UnitInterfaceAst(
            uses,
            InterfaceDeclAst.of(unt.interfaceDecl())
        );
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();

        sb.append("uses:\n");
        uses.each(n -> sb.append("  ").append(n).append("\n"));

        sb.append("declarations:\n");
        declarations.each(d -> sb.append(indent("  ", d.toString())).append("\n"));

        return sb.toString();
    }
}
