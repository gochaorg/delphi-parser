package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Определяет пространство имен
 * @param name имя
 */
public record Namespace(ImList<String,?> name) {
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Namespace namespace = (Namespace) o;
        if( namespace.name.size()!= name.size() )return false;
        for( var i=0;i<name.size(); i++ ){
            if( !Objects.equals(name.get(i).get(), namespace.name.get(i).get()) )
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    static Namespace of(DelphiParser.NamespaceNameContext nm) {
        return new Namespace(
            ImListLinked.of(
                nm.ident().stream().map(RuleContext::getText).collect(Collectors.toList())
            )
        );
    }

    static Namespace of(String ... name){
        return new Namespace(
            ImListLinked.of(name)
        );
    }
}
