package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Структурный тип
 */
sealed public interface StructuredTypeAst extends TypeDeclAst permits ArrayTypeAst,
                                                                      ClazzTypeAst,
                                                                      InterfaceTypeAst,
                                                                      MetaClassAst {
    @Override
    StructuredTypeAst astUpdate(AstUpdate.UpdateContext ctx);

    static StructuredTypeAst of(DelphiParser.StrucTypeContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        boolean packed = ctx.PACKED() != null && !ctx.PACKED().getText().isBlank();

        if (ctx.strucTypePart() != null
            && !ctx.strucTypePart().isEmpty()
        ) return of(ctx.strucTypePart(), packed);

        throw AstParseError.notImplemented(ctx);
    }

    static StructuredTypeAst of(DelphiParser.StrucTypePartContext ctx, boolean packed) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        if (ctx.classDecl() != null && !ctx.classDecl().isEmpty()) return of(ctx.classDecl());
        if (ctx.arrayType() != null && !ctx.arrayType().isEmpty()) return of(ctx.arrayType(), packed);

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

    static StructuredTypeAst of(DelphiParser.ArrayTypeContext ctx, boolean packed) {
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        return ArrayTypeAst.of(ctx,packed);
    }
}
