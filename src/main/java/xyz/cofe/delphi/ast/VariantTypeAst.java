package xyz.cofe.delphi.ast;

/**
 * Вариант
 */
public record VariantTypeAst() implements TypeDeclAst {
    @Override
    public xyz.cofe.delphi.ast.VariantTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
