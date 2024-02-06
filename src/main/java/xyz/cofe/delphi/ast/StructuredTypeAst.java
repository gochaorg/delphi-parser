package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Структурный тип
 */
sealed public interface StructuredTypeAst extends TypeDeclAst permits ArrayTypeAst,
                                                                      ClazzTypeAst,
                                                                      FileTypeAst,
                                                                      InterfaceTypeAst,
                                                                      MetaClassTypeAst,
                                                                      RecordAst,
                                                                      SetTypeAst {
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
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if (ctx.classDecl() != null && !ctx.classDecl().isEmpty()) return of(ctx.classDecl());
        if (ctx.arrayType() != null && !ctx.arrayType().isEmpty()) return of(ctx.arrayType(), packed);
        if (ctx.setType() != null && !ctx.setType().isEmpty()) return SetTypeAst.of(ctx.setType());
        if (ctx.fileType() != null && !ctx.fileType().isEmpty()) return FileTypeAst.of(ctx.fileType());

        throw AstParseError.notImplemented(ctx);
    }

    static StructuredTypeAst of(DelphiParser.ClassDeclContext ctx) {
        if (ctx.interfaceTypeDecl() != null
            && !ctx.interfaceTypeDecl().isEmpty()
        ) return InterfaceTypeAst.of(ctx.interfaceTypeDecl());

        if (ctx.classTypeDecl() != null
            && !ctx.classTypeDecl().isEmpty()
        ) return ClazzTypeAst.of(ctx.classTypeDecl());

        if (ctx.recordDecl()!=null && !ctx.recordDecl().isEmpty()){
            return RecordAst.of(ctx.recordDecl());
        }

        if( ctx.classTypeTypeDecl()!=null && !ctx.classTypeTypeDecl().isEmpty() ){
            return MetaClassTypeAst.of(ctx.classTypeTypeDecl());
        }

        throw AstParseError.notImplemented(ctx);
    }

    static StructuredTypeAst of(DelphiParser.ArrayTypeContext ctx, boolean packed) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        return ArrayTypeAst.of(ctx, packed);
    }
}
