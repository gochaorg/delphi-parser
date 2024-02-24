package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record ExportSectionAst(
    ImList<Export> exports,
    SourcePosition position,
    ImList<Comment> comments
) implements DeclSectionAst {

    public record Export(
        String ident,
        ImList<ArgumentAst> params,
        Optional<ExpressionAst> index,
        Optional<ExpressionAst> name,
        boolean resident,
        SourcePosition position,
        ImList<Comment> comments
    ) implements WithComments<Export> {
        public static Export build(String ident, SourcePosition identPos, DelphiParser.ExportItemContext ctx) {
            if (ident == null) throw new IllegalArgumentException("ident==null");
            if (identPos == null) throw new IllegalArgumentException("identPos==null");
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            var pos = SourcePosition.mergeExtend(identPos, SourcePosition.of(ctx));

            ImList<ArgumentAst> params = ImList.of();
            if (ctx.formalParameterList() != null && !ctx.formalParameterList().isEmpty()) {
                params = ArgumentAst.of(ctx.formalParameterList());
            }

            boolean resident = ctx.RESIDENT() != null && !ctx.RESIDENT().getText().isEmpty();

            Optional<ExpressionAst> idx =
                ctx.index1 != null && !ctx.index1.isEmpty()
                    ? Optional.of(ExpressionAst.of(ctx.index1))
                    : Optional.empty();

            Optional<ExpressionAst> name =
                ctx.name1 != null && !ctx.name1.isEmpty()
                    ? Optional.of(ExpressionAst.of(ctx.name1))
                    : Optional.empty();

            return new Export(
                ident,
                params,
                idx,
                name,
                resident,
                pos,
                ImList.of()
            );
        }
    }

    public static ExportSectionAst of(DelphiParser.ExportsSectionContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        ImList<Export> exports = ImList.of();

        String ident = null;
        SourcePosition identPos = null;

        var state = "";

        for (var chIdx = 0; chIdx < ctx.getChildCount(); chIdx++) {
            var ch = ctx.getChild(chIdx);
            switch (state) {
                case "" -> {
                    if (ch instanceof DelphiParser.IdentContext id) {
                        ident = id.getText();
                        identPos = SourcePosition.of(id);
                        state = "ident";
                    }
                }
                case "ident" -> {
                    if (ch instanceof DelphiParser.ExportItemContext exp && ident != null) {
                        exports = exports.prepend(Export.build(ident, identPos, exp));
                        state = "";
                    }
                }
            }
        }

        return new ExportSectionAst(exports.reverse(), SourcePosition.of(ctx), ImList.of());
    }
}
