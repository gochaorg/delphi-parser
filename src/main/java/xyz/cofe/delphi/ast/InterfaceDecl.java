package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Публичная часть
 */
public sealed interface InterfaceDecl
extends AstNode
permits
    TypeSection,
    MethodDecl,
    ProcDecl,
    ExportedProcHeading,
    ConstSection,
    ExportsSection,
    VarSection
{
    @Override
    InterfaceDecl astUpdate(AstUpdate.UpdateContext ctx);

    static InterfaceDecl of(DelphiParser.InterfaceDeclContext itf){
        if( !itf.typeSection().isEmpty() ){
            return
                new TypeSection(
                    TypeDeclaration.of(itf.typeSection().typeDeclaration()),
                    SourcePosition.of(itf.typeSection())
                );
        }

        throw AstParseError.notImplemented();
    }

    static ImList<InterfaceDecl,?> of(Iterable<DelphiParser.InterfaceDeclContext> ctx){
        return ImListLinked.of(ctx).map(InterfaceDecl::of);
    }
}
