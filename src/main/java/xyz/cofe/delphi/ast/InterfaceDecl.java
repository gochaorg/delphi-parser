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
        if( !itf.typeSection().isEmpty() ){
            return
                new TypeSectionAst(
                    TypeDeclarationAst.of(itf.typeSection().typeDeclaration()),
                    SourcePosition.of(itf.typeSection())
                );
        }

        throw AstParseError.notImplemented();
    }

    static ImList<InterfaceDecl,?> of(Iterable<DelphiParser.InterfaceDeclContext> ctx){
        return ImListLinked.of(ctx).map(InterfaceDecl::of);
    }
}
