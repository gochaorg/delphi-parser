package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Определяет пространство имен
 *
 * @param name имя
 */
public record NamespaceAst(ImList<String> name) implements AstNode {
    @Override
    public NamespaceAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamespaceAst namespace = (NamespaceAst) o;
        if (namespace.name.size() != name.size()) return false;
        for (var i = 0; i < name.size(); i++) {
            if (!Objects.equals(name.get(i).get(), namespace.name.get(i).get()))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    static NamespaceAst of(DelphiParser.NamespaceNameContext nm) {
        return new NamespaceAst(
            ImListLinked.of(
                nm.ident().stream().map(RuleContext::getText).collect(Collectors.toList())
            )
        );
    }

    static NamespaceAst of(String... name) {
        return new NamespaceAst(
            ImListLinked.of(name)
        );
    }
}
