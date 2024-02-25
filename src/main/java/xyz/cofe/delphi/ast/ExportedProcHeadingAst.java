package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface ExportedProcHeadingAst extends InterfaceDeclAst, DeclSectionAst {
    record Procedure(
        String name,
        ImList<ArgumentAst> params,
        ImList<FDirective.FunctionDirective> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ExportedProcHeadingAst, WithComments<Procedure> {}

    record Function(
        String name,
        ImList<ArgumentAst> params,
        ImList<CustomAttributeAst> returnsAttributes,
        TypeDeclAst returns,
        ImList<FDirective.FunctionDirective> directives,
        SourcePosition position,
        ImList<Comment> comments
    ) implements ExportedProcHeadingAst, WithComments<Function> {}

    static ExportedProcHeadingAst of(DelphiParser.ExportedProcHeadingContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        if( ctx.PROCEDURE()!=null ){
            ImList<ArgumentAst> params =
                ctx.formalParameterSection()!=null && !ctx.formalParameterSection().isEmpty()
                    ? ArgumentAst.of(ctx.formalParameterSection().formalParameterList())
                    : ImList.of();

            ImList<CustomAttributeAst> attrs =
                ctx.customAttribute()!=null
                    ? ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of)
                    : ImList.of();

            var rets = TypeDeclAst.of(ctx.typeDecl());

            ImList<FDirective.FunctionDirective> dirs =
                ImList.of(ctx.functionDirective()).fmap(FDirective::of);

            return new Function(ctx.ident().getText(), params, attrs, rets, dirs, SourcePosition.of(ctx), ImList.of());
        }

        if( ctx.FUNCTION()!=null ){
            ImList<ArgumentAst> params =
                ctx.formalParameterSection()!=null && !ctx.formalParameterSection().isEmpty()
                    ? ArgumentAst.of(ctx.formalParameterSection().formalParameterList())
                    : ImList.of();

            ImList<FDirective.FunctionDirective> dirs =
                ImList.of(ctx.functionDirective()).fmap(FDirective::of);

            return new Procedure(ctx.ident().getText(), params, dirs, SourcePosition.of(ctx), ImList.of());
        }

        throw AstParseError.unExpected(ctx);
    }
}
