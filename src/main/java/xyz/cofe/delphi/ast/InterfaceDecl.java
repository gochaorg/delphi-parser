package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;

/**
 * Публичная часть
 */
public sealed interface InterfaceDecl
extends AstNode
permits
    TypeSectionAst,
    MethodDeclAst,
    ProcDeclAst,
    ExportedProcHeadingAst,
    ConstSectionAst,
    ExportsSectionAst,
    VarSectionAst
{
    @Override
    InterfaceDecl astUpdate(AstUpdate.UpdateContext ctx);

    static InterfaceDecl of(DelphiParser.InterfaceDeclContext itf){
        if( itf.typeSection()!=null && !itf.typeSection().isEmpty() ){
            return
                new TypeSectionAst(
                    TypeDeclarationAst.of(itf.typeSection().typeDeclaration()),
                    SourcePosition.of(itf.typeSection())
                );
        }

        if( itf.constSection()!=null && !itf.constSection().isEmpty() ){
            return ConstSectionAst.Constants.of(itf.constSection());
        }

        if( itf.varSection()!=null && !itf.varSection().isEmpty() ){
            return VarSectionAst.Variables.of(itf.varSection());
        }

        if( itf.procDecl()!=null && !itf.procDecl().isEmpty() ){
            return ProcDeclAst.of(itf.procDecl());
        }

        if( itf.methodDecl()!=null && !itf.methodDecl().isEmpty() ){
            return MethodDeclAst.of(itf.methodDecl());
        }

        throw AstParseError.notImplemented();
    }

    static ImList<InterfaceDecl> of(Iterable<DelphiParser.InterfaceDeclContext> ctx){
        return ImListLinked.of(ctx).map(InterfaceDecl::of);
    }
}
