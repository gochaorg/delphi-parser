package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Generic параметр
 */
// TODO требует переименования/рефакторинга
public sealed interface GenericAst {
    record Param(String name, ImList<Bound> constraints) implements GenericAst, AstNode {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Param param = (Param) o;
            return Objects.equals(name, param.name) && Objects.equals(constraints, param.constraints);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, constraints);
        }
    }

    sealed interface Bound {}
    record ItfBound(String name) implements Bound {}
    record Record() implements Bound {}
    record Class() implements Bound {}
    record Constructor() implements Bound {}

    static Param param(String name) {
        return new Param(name, ImListLinked.of());
    }
    static GenericAst.Param param(DelphiParser.ConstrainedGenericContext ctx){
        ImListLinked<Bound> bounds = ImListLinked.of(
        ctx.genericConstraint().stream()
            .map( c -> {
                if(!c.ident().isEmpty()){
                    return (Bound)new ItfBound(c.ident().getText());
                }else{
                    return switch (c.getText().toLowerCase()){
                        case "record" -> new Record();
                        case "class" -> new Class();
                        case "constructor" -> new Constructor();
                        default -> throw new AstParseError("Un expected "+c.ident().getText());
                    };
                }
            }).toList());

        var name = ctx.ident().getText();

        return new Param(
            name,
            bounds
        );
    }
    static ImListLinked<GenericAst.Param> of(DelphiParser.GenericDefinitionContext ctx){
        var genParams = ImListLinked.<GenericAst.Param>of();

        if ( ctx.simpleGenericDefinition()!=null && !ctx.simpleGenericDefinition().isEmpty()) {
            genParams = ImListLinked.of(
                ctx.simpleGenericDefinition().ident().stream()
                    .map(RuleContext::getText).toList()
            ).foldLeft(genParams, (acc, it) -> acc.append(
                GenericAst.param(it)
            ));
        }

        if ( ctx.constrainedGenericDefinition()!=null && !ctx.constrainedGenericDefinition().isEmpty()) {
            genParams = ImListLinked.of(
                ctx.constrainedGenericDefinition().constrainedGeneric()
                    .stream()
                    .map(GenericAst::param)
                    .collect(Collectors.toList())
            ).foldLeft(genParams, ImListLinked::append);
        }
        return genParams;
    }
}
