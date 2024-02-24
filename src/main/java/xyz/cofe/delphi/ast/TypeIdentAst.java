package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.impl.Ident;
import xyz.cofe.delphi.parser.DelphiParser;
import xyz.cofe.delphi.tsys.tm.TypeName;

import java.util.Objects;

public record TypeIdentAst(
    ImList<String> name,
    ImList<GenericAst.Param> params
) implements TypeDeclAst
{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeIdentAst typeIdent = (TypeIdentAst) o;

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

    public static TypeIdentAst of(String ... name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        return new TypeIdentAst(
            ImListLinked.of(name), ImListLinked.of()
        );
    }

    public static TypeIdentAst of(TypeName typeName){
        if( typeName==null ) throw new IllegalArgumentException("typeName==null");
        return new TypeIdentAst(typeName,ImListLinked.of());
    }

    public TypeIdentAst withParams(ImList<GenericAst.Param> params ){
        if( params==null ) throw new IllegalArgumentException("params==null");
        return new TypeIdentAst(name, params);
    }

    public static TypeIdentAst of(DelphiParser.GenericTypeIdentContext ctx){
        var name = ImListLinked.of(
            ctx.qualifiedIdent().ident().stream()
            .map(RuleContext::getText).toList());

        var genParams = ImListLinked.<GenericAst.Param>of();
        if( ctx.genericDefinition()!=null
        && !ctx.genericDefinition().isEmpty()
        ){
            var genDef =
                ctx.genericDefinition();

            if( genDef!=null ) {
                genParams = GenericAst.of(genDef);
            }
        }

        return new TypeIdentAst(
            name,
            genParams
        );
    }

    public static TypeIdentAst of(DelphiParser.TypeDeclarationContext dec) {
        var name = ImListLinked.of(
            dec.genericTypeIdent().qualifiedIdent().ident().stream()
                .map(RuleContext::getText).toList());

        var genParams = ImListLinked.<GenericAst.Param>of();
        if( dec.genericTypeIdent()!=null
        &&  !dec.genericTypeIdent().isEmpty()
        ){
            var genDef =
                dec.genericTypeIdent().genericDefinition();

            if( genDef!=null ) {
                genParams = GenericAst.of(genDef);
            }
        }

        return new TypeIdentAst(
            name,
            genParams
        );
    }

    public static TypeIdentAst of(DelphiParser.TypeIdContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        return new TypeIdentAst(
            Ident.identifier(ctx),
            ImList.of()
        );
    }
}
