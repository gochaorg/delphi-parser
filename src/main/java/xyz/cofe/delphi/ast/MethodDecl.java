package xyz.cofe.delphi.ast;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Декларачия метода/функции
 */
public record MethodDecl() implements InterfaceDecl, AstNode {
    @Override
    public MethodDecl astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
