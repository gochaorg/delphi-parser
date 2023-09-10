package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface InterfaceDecl
permits
    TypeSection,
    MethodDecl,
    ProcDecl,
    ExportedProcHeading,
    ConstSection,
    ExportsSection,
    VarSection
{
    static InterfaceDecl of(DelphiParser.InterfaceDeclContext itf){
        if( !itf.typeSection().isEmpty() ){
            return
                new TypeSection(
                    TypeDeclaration.of(itf.typeSection().typeDeclaration())
                );
        }

        throw AstParseError.notImplemented();
    }

    static ImList<InterfaceDecl,?> of(Iterable<DelphiParser.InterfaceDeclContext> ctx){
        return ImListLinked.of(ctx).map(InterfaceDecl::of);
    }
}
