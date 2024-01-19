package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;
import java.util.Optional;

/**
 * Текстовый тип
 */
sealed public interface StringTypeAst extends TypeDeclAst, SrcPos {
    record StrIng(Optional<ExpressionAst> expression, SourcePosition position,
                  ImList<Comment> comments) implements StringTypeAst, Commented<StrIng> {
        @Override
        public StrIng astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public StrIng withComments(ImList<Comment> comments) {
            return new StrIng(expression, position, comments);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StrIng strIng = (StrIng) o;
            return Objects.equals(expression, strIng.expression);
        }

        @Override
        public int hashCode() {
            return Objects.hash(expression);
        }
    }

    record AnsiString(Optional<String> codePageNum, boolean typeFlag, SourcePosition position,
                      ImList<Comment> comments) implements StringTypeAst, Commented<AnsiString> {
        @Override
        public AnsiString astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public AnsiString withComments(ImList<Comment> comments) {
            return new AnsiString(codePageNum, typeFlag, position, comments);
        }
    }

    static StringTypeAst of(DelphiParser.StringTypeContext ctx) {
        if (ctx.STRING() != null && ctx.STRING().getText() != null) {
            if (ctx.expression() != null && !ctx.expression().isEmpty()) {
                return new StrIng(
                    Optional.of(ExpressionAst.of(ctx.expression())),
                    SourcePosition.of(ctx),
                    ImListLinked.of()
                );
            }

            return new StrIng(Optional.empty(), SourcePosition.of(ctx), ImListLinked.of());
        }

        if (ctx.ANSISTRING() != null && ctx.ANSISTRING().getText() != null) {
            var typeFlag = ctx.TYPE() != null && ctx.TYPE().getText() != null;
            var codePage =
                ctx.codePageNumber() != null &&
                    ctx.codePageNumber().intNum() != null &&
                    ctx.codePageNumber().intNum().getText() != null ?
                    Optional.of(ctx.codePageNumber().intNum().getText()) :
                    Optional.<String>empty();
            return new AnsiString(codePage, typeFlag, SourcePosition.of(ctx), ImListLinked.of());
        }

        throw AstParseError.unExpected(ctx);
    }
}
