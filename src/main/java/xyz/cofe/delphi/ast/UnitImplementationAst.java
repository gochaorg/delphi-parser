package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.stream.Collectors;

public record UnitImplementationAst(
    ImList<NamespaceAst> uses,
    ImList<DeclSectionAst> declarations,
    SourcePosition position,
    ImList<Comment> comments
) implements WithComments<UnitImplementationAst> {
    public static UnitImplementationAst of(DelphiParser.UnitImplementationContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        ImList<NamespaceAst> uses =
            ctx.usesClause() == null
                ? ImList.of()
                : ctx.usesClause().namespaceNameList() == null
                ? ImList.of()
                : ctx.usesClause().namespaceNameList().namespaceName() == null
                ? ImList.of()
                : ImList.of(
                    ctx.usesClause().namespaceNameList().namespaceName().stream().map(NamespaceAst::of)
                        .collect(Collectors.toList()));

        var decls = ImList.of(ctx.declSection()).map(DeclSectionAst::of);

        return new UnitImplementationAst(
            uses,
            decls,
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
