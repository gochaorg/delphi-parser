package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.parser.DelphiParser;

public record LabelDeclSectionAst(
    ImList<String> label,
    SourcePosition position,
    ImList<Comment> comments
) implements DeclSectionAst, WithComments<LabelDeclSectionAst> {
    public static LabelDeclSectionAst of(DelphiParser.LabelDeclSectionContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        return new LabelDeclSectionAst(
            ImList.of(ctx.label()).map(RuleContext::getText),
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
