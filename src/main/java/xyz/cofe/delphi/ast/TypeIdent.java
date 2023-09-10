package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

public record TypeIdent(
    ImList<String,?> name,
    ImList<Generic.Param,?> params
)
{
    static TypeIdent of(DelphiParser.GenericTypeIdentContext ctx){
        var name = ImListLinked.of(
            ctx.qualifiedIdent().ident().stream()
            .map(RuleContext::getText).toList());

        var genParams = ImListLinked.<Generic.Param>of();
        if( ctx.genericDefinition()!=null
        && !ctx.genericDefinition().isEmpty()
        ){
            var genDef =
                ctx.genericDefinition();

            if( genDef!=null ) {
                genParams = Generic.of(genDef);
            }
        }

        return new TypeIdent(
            name,
            genParams
        );
    }

    static TypeIdent of(DelphiParser.TypeDeclarationContext dec) {
        var name = ImListLinked.of(
            dec.genericTypeIdent().qualifiedIdent().ident().stream()
                .map(RuleContext::getText).toList());

        var genParams = ImListLinked.<Generic.Param>of();
        if( dec.genericTypeIdent()!=null
        &&  !dec.genericTypeIdent().isEmpty()
        ){
            var genDef =
                dec.genericTypeIdent().genericDefinition();

            if( genDef!=null ) {
                genParams = Generic.of(genDef);
            }
        }

        return new TypeIdent(
            name,
            genParams
        );
    }
}
