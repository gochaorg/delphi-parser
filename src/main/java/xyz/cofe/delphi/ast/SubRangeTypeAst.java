package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public sealed interface SubRangeTypeAst extends TypeDeclAst {
    public static SubRangeTypeAst of(DelphiParser.SubRangeTypeContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.constExpression() == null) throw AstParseError.unExpected(ctx);
        if (ctx.constExpression().size() < 1) throw AstParseError.unExpected(ctx);
        if (ctx.constExpression().size() == 1) {
            return new Single(
                ConstSectionAst.ConstExpression.of(
                    ctx.constExpression(0)
                ),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
        if (ctx.constExpression().size() == 2) {
            return new FromTo(
                ConstSectionAst.ConstExpression.of(ctx.constExpression(0)),
                ConstSectionAst.ConstExpression.of(ctx.constExpression(1)),
                SourcePosition.of(ctx),
                ImList.of()
            );
        }
        throw AstParseError.unExpected(ctx);
    }

    record Single(ConstSectionAst.ConstExpression expression,
                  SourcePosition position,
                  ImList<Comment> comments
    ) implements SubRangeTypeAst,
                 Commented<Single> {
        @Override
        public Single withComments(ImList<Comment> comments) {
            if (comments == null) throw new IllegalArgumentException("comments==null");
            return new Single(expression, position, comments);
        }
    }

    record FromTo(ConstSectionAst.ConstExpression from,
                  ConstSectionAst.ConstExpression to,
                  SourcePosition position,
                  ImList<Comment> comments
    ) implements SubRangeTypeAst,
                 Commented<FromTo> {
        @Override
        public FromTo withComments(ImList<Comment> comments) {
            if (comments == null) throw new IllegalArgumentException("comments==null");
            return new FromTo(from, to, position, comments);
        }
    }
}
