package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Структурный тип
 */
sealed public interface StructuredTypeAst extends TypeDeclAst permits ArrayTypeAst, ClazzTypeAst, InterfaceTypeAst, MetaClassAst {
    @Override
    StructuredTypeAst astUpdate(AstUpdate.UpdateContext ctx);

    static StructuredTypeAst of(DelphiParser.StrucTypeContext ctx) {
        if (ctx.strucTypePart() != null
            && !ctx.strucTypePart().isEmpty()
        ) return of(ctx.strucTypePart());

        throw AstParseError.notImplemented(ctx);
    }

    static StructuredTypeAst of(DelphiParser.StrucTypePartContext ctx) {
        if (ctx.classDecl() != null
            && !ctx.classDecl().isEmpty()
        ) return of(ctx.classDecl());
        throw AstParseError.notImplemented(ctx);
    }

    static StructuredTypeAst of(DelphiParser.ClassDeclContext ctx) {
        if (ctx.interfaceTypeDecl() != null
            && !ctx.interfaceTypeDecl().isEmpty()
        ) return InterfaceTypeAst.of(ctx.interfaceTypeDecl());

        if (ctx.classTypeDecl() != null
            && !ctx.classTypeDecl().isEmpty()
        ) return ClazzTypeAst.of(ctx.classTypeDecl());

        throw AstParseError.notImplemented(ctx);
    }
}
