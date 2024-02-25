package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Публичная часть
 */
public sealed interface InterfaceDeclAst
    permits ConstSectionAst,
            ExportSectionAst,
            ExportedProcHeadingAst,
            MethodDeclAst,
            ProcDeclAst,
            TypeSectionAst,
            VarSectionAst {

    static InterfaceDeclAst of(DelphiParser.InterfaceDeclContext ctx) {
        if (ctx.typeSection() != null && !ctx.typeSection().isEmpty()) {
            return
                new TypeSectionAst(
                    TypeDeclarationAst.of(ctx.typeSection().typeDeclaration()),
                    SourcePosition.of(ctx.typeSection())
                );
        }

        if (ctx.constSection() != null && !ctx.constSection().isEmpty())
            return ConstSectionAst.Constants.of(ctx.constSection());

        if (ctx.varSection() != null && !ctx.varSection().isEmpty())
            return VarSectionAst.Variables.of(ctx.varSection());

        if (ctx.procDecl() != null && !ctx.procDecl().isEmpty())
            return ProcDeclAst.of(ctx.procDecl());

        if (ctx.methodDecl() != null && !ctx.methodDecl().isEmpty())
            return MethodDeclAst.of(ctx.methodDecl());

        if (ctx.exportedProcHeading() != null && !ctx.exportedProcHeading().isEmpty())
            return ExportedProcHeadingAst.of(ctx.exportedProcHeading());

        if (ctx.exportsSection() != null && !ctx.exportsSection().isEmpty())
            return ExportSectionAst.of(ctx.exportsSection());

        throw AstParseError.unExpected(ctx);
    }

    static ImList<InterfaceDeclAst> of(Iterable<DelphiParser.InterfaceDeclContext> ctx) {
        return ImListLinked.of(ctx).map(InterfaceDeclAst::of);
    }
}
