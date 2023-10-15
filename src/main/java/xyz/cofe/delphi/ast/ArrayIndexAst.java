package xyz.cofe.delphi.ast;

sealed public interface ArrayIndexAst extends AstNode permits TypeDeclAst.ArrayIndexRange, TypeDeclAst.ArrayIndexType {
    @Override
    ArrayIndexAst astUpdate(AstUpdate.UpdateContext ctx);
}
