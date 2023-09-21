package xyz.cofe.delphi.ast;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

public record ExportsSection() implements InterfaceDecl, AstNode {
    @Override
    public ExportsSection astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
