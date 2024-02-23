package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record CustomAttributeAst(ImList<Attribute> attributes, SourcePosition position, ImList<Comment> comments)
    implements SrcPos,
               Commented<CustomAttributeAst> {
    private static ImList<String> idOf(DelphiParser.NamespacedQualifiedIdentContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var ids = ctx.namespaceName() != null && ctx.namespaceName().ident() != null ?
            ImList.of(ctx.namespaceName().ident()).map(RuleContext::getText)
            : ImList.<String>of();
        ids = ids.append(
            ctx.qualifiedIdent() != null &&
                ctx.qualifiedIdent().ident() != null
                ? ImList.of(ctx.qualifiedIdent().ident()).map(RuleContext::getText)
                : ImList.of()
        );
        return ids;
    }

    private static ImList<String> idOf(DelphiParser.CustomAttributeNamedCallContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        if (ctx.namespacedQualifiedIdent() != null && !ctx.namespacedQualifiedIdent().isEmpty())
            return idOf(ctx.namespacedQualifiedIdent());

        if (ctx.paramName() != null && !ctx.paramName().isEmpty())
            return ImList.of(ctx.paramName().getText());

        throw AstParseError.unExpected(ctx);
    }

    private static ImList<ExpressionAst> exprsOf(DelphiParser.CustomAttributeNamedCallContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.expressionList() == null) return ImList.of();
        if (ctx.expressionList().expression() == null) return ImList.of();
        if (!ctx.expressionList().expression().isEmpty()) return ImList.of();
        return ImList.of(ctx.expressionList().expression()).map(ExpressionAst::of);
    }

    public record Attribute(ImList<String> id, ImList<ExpressionAst> args, SourcePosition position,
                            ImList<Comment> comments)
        implements SrcPos,
                   Commented<Attribute> {

        public static Attribute of(DelphiParser.CustomAttributeNamedCallContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            var id = idOf(ctx);
            var args = exprsOf(ctx);
            return new Attribute(id, args, SourcePosition.of(ctx), ImList.of());
        }

        @Override
        public Attribute withComments(ImList<Comment> comments) {
            return new Attribute(id, args, position, comments);
        }
    }

    public static CustomAttributeAst of(DelphiParser.CustomAttributeContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        if (ctx.customAttributeDecl() == null || ctx.customAttributeDecl().isEmpty()) {
            throw AstParseError.unExpected(ctx);
        }

        if (ctx.customAttributeDecl().customAttributeNamedCall() == null) {
            throw AstParseError.unExpected(ctx);
        }

        if (ctx.customAttributeDecl().customAttributeNamedCall().isEmpty()) {
            throw AstParseError.unExpected(ctx);
        }

        return new CustomAttributeAst(
            ImList.of(ctx.customAttributeDecl().customAttributeNamedCall())
                .map(Attribute::of),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }

    @Override
    public CustomAttributeAst withComments(ImList<Comment> comments) {
        return new CustomAttributeAst(attributes, position, comments);
    }
}
