package xyz.cofe.delphi.ast.impl;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.ast.AstParseError;
import xyz.cofe.delphi.parser.DelphiParser;

public final class Ident {
    public static ImList<String> identifier(DelphiParser.NamespacedQualifiedIdentContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");

        var ids = ctx.namespaceName()!=null &&
            ctx.namespaceName().ident()!=null
            ? ImList.of(ctx.namespaceName().ident())
            .map(RuleContext::getText)
            : ImList.<String>of();

        var ids2 = ctx.qualifiedIdent()!=null && ctx.qualifiedIdent().ident()!=null ?
            ImList.of(ctx.qualifiedIdent().ident())
                .map(RuleContext::getText)
            : ImList.<String>of();

        return ids.append(ids2);
    }

    public static ImList<String> identifier(DelphiParser.TypeIdContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        if( ctx.namespacedQualifiedIdent()==null )throw AstParseError.unExpected(ctx);

        return identifier(ctx.namespacedQualifiedIdent());
    }
}
