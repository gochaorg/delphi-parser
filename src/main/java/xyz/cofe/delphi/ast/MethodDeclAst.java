package xyz.cofe.delphi.ast;

import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Декларачия метода/функции
 */
public record MethodDeclAst() implements InterfaceDecl, AstNode {
    @Override
    public MethodDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
