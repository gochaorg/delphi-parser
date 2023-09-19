package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;

public record TypeIdent(
    ImList<String,?> name,
    ImList<Generic.Param,?> params
) implements TypeDecl, AstNode
{
    @Override
    public ImList<? extends AstNode, ?> nestedAstNodes() {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeIdent typeIdent = (TypeIdent) o;

        // name compare
        if( name.size() != typeIdent.name.size() )return false;
        for( var i=0; i<name.size(); i++ ){
            var ii = i;
            var r = name.get(i).flatMap( n1 ->
                typeIdent.name.get(ii).map(n1::equalsIgnoreCase)
            );
            if( !r.orElse(false) )return false;
        }

        // param compare
        if( params.size() != typeIdent.params.size() )return false;
        for( var i=0; i<params.size(); i++ ){
            var ii = i;
            var r = params.get(i).flatMap( p1 ->
                typeIdent.params.get(ii).map(p1::equals)
            );
            if( !r.orElse(false) )return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, params);
    }

    public static TypeIdent of(String ... name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        return new TypeIdent(
            ImListLinked.of(name), ImListLinked.of()
        );
    }

    public TypeIdent withParams( ImList<Generic.Param,?> params ){
        if( params==null ) throw new IllegalArgumentException("params==null");
        return new TypeIdent(name, params);
    }

    public static TypeIdent of(DelphiParser.GenericTypeIdentContext ctx){
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

    public static TypeIdent of(DelphiParser.TypeDeclarationContext dec) {
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
