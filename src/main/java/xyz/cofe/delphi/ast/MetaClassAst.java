package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

public record MetaClassAst(ImList<String> typeId) implements TypeDeclAst, StructuredTypeAst {
    @Override
    public MetaClassAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }
}
