package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.ImListLinkedBase;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.stream.Collectors;

public sealed interface Generic {
    record Param(String name, ImList<Bound,?> constraints) implements Generic {}

    sealed interface Bound {}
    record ItfBound(String name) implements Bound {}
    record Record() implements Bound {}
    record Class() implements Bound {}
    record Constructor() implements Bound {}

    static Param param(String name) {
        return new Param(name, ImListLinked.of());
    }

    static Generic.Param param(DelphiParser.ConstrainedGenericContext ctx){
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

    static ImListLinked<Generic.Param> of(DelphiParser.GenericDefinitionContext ctx){
        var genParams = ImListLinked.<Generic.Param>of();
        if (!ctx.simpleGenericDefinition().isEmpty()) {
            genParams = ImListLinked.of(
                ctx.simpleGenericDefinition().ident().stream()
                    .map(RuleContext::getText).toList()
            ).foldLeft(genParams, (acc, it) -> acc.append(
                Generic.param(it)
            ));
        }

        if (!ctx.constrainedGenericDefinition().isEmpty()) {
            genParams = ImListLinked.of(
                ctx.constrainedGenericDefinition().constrainedGeneric()
                    .stream()
                    .map(Generic::param)
                    .collect(Collectors.toList())
            ).foldLeft(genParams, ImListLinkedBase::append);
        }
        return genParams;
    }
}
