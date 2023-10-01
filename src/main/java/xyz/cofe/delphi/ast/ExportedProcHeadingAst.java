package xyz.cofe.delphi.ast;

import static xyz.cofe.delphi.ast.AstNode.upcast;

public record ExportedProcHeadingAst() implements InterfaceDecl, AstNode {
    @Override
    public ExportedProcHeadingAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
