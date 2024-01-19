package xyz.cofe.delphi.ast;

import static xyz.cofe.delphi.ast.AstNode.upcast;

public record ExportsSectionAst() implements InterfaceDecl, AstNode {
    @Override
    public ExportsSectionAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
