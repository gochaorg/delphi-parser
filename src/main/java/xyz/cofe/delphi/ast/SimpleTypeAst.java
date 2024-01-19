package xyz.cofe.delphi.ast;

/**
 * Некий простой тип
 *
 * @param name имя типа
 */
public record SimpleTypeAst(String name) implements TypeDeclAst {
    @Override
    public TypeDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
