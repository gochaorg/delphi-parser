package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface LambdaAst {
    record AnonProcedure(
        ImList<ArgumentAst> arguments,
        BodyAst bodyAst,
        SourcePosition position,
        ImList<Comment> comments
    ) implements LambdaAst, WithComments<AnonProcedure> {}

    record AnonFunction(
        ImList<ArgumentAst> arguments,
        TypeDeclAst returns,
        BodyAst bodyAst,
        SourcePosition position,
        ImList<Comment> comments
    ) implements LambdaAst, WithComments<AnonFunction> {}

    static LambdaAst of(DelphiParser.AnonymousExpressionContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        if( ctx.PROCEDURE()!=null && !ctx.PROCEDURE().getText().isEmpty() ){
            return new AnonProcedure(
                ArgumentAst.of(ctx.formalParameterSection().formalParameterList()),
                BodyAst.of(ctx.block()),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }

        if( ctx.FUNCTION()!=null && !ctx.FUNCTION().getText().isEmpty() ){
            return new AnonFunction(
                ArgumentAst.of(ctx.formalParameterSection().formalParameterList()),
                TypeDeclAst.of(ctx.typeDecl()),
                BodyAst.of(ctx.block()),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }

        throw AstParseError.unExpected(ctx);
    }
}
