package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

/**
 * Выражение
 */
public interface ExpressionAst extends AstNode,
                                       SrcPos {

    static ExpressionAst of(DelphiParser.ExpressionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if(ctx.anonymousExpression()!=null && !ctx.anonymousExpression().isEmpty()){
            return LambdaAst.of(ctx.anonymousExpression());
        }

        if(ctx.simpleExpression()!=null && !ctx.simpleExpression().isEmpty()){
            return SimpleExpressionAst.of(ctx.simpleExpression());
        }

        throw AstParseError.unExpected(ctx);
    }
}
