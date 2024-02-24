package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface DeclSectionAst permits ConstSectionAst,
                                               ExportSectionAst,
                                               ExportedProcHeadingAst,
                                               LabelDeclSectionAst,
                                               MethodDeclAst,
                                               ProcDeclAst,
                                               TypeSectionAst,
                                               VarSectionAst {

    static DeclSectionAst of(DelphiParser.DeclSectionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if (ctx.labelDeclSection() != null && !ctx.labelDeclSection().isEmpty())
            return LabelDeclSectionAst.of(ctx.labelDeclSection());

        if (ctx.constSection() != null && !ctx.constSection().isEmpty())
            return ConstSectionAst.Constants.of(ctx.constSection());

        if (ctx.typeSection() != null && !ctx.typeSection().isEmpty())
            return TypeSectionAst.of(ctx.typeSection());

        if (ctx.varSection() != null && !ctx.varSection().isEmpty())
            return VarSectionAst.Variables.of(ctx.varSection());

        if (ctx.exportedProcHeading() != null && !ctx.exportedProcHeading().isEmpty())
            return ExportedProcHeadingAst.of(ctx.exportedProcHeading());

        if (ctx.methodDecl() != null && !ctx.methodDecl().isEmpty())
            return MethodDeclAst.of(ctx.methodDecl());

        if (ctx.procDecl() != null && !ctx.procDecl().isEmpty())
            return ProcDeclAst.of(ctx.procDecl());

        if (ctx.exportsSection() != null && !ctx.exportsSection().isEmpty())
            return ExportSectionAst.of(ctx.exportsSection());

        return null;
    }
}
