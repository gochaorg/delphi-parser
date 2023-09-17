package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.stream.Collectors;

/**
 * Определяет пространство имен
 * @param name имя
 */
public record Namespace(ImList<String,?> name) {
    static Namespace of(DelphiParser.NamespaceNameContext nm) {
        return new Namespace(
            ImListLinked.of(
                nm.ident().stream().map(RuleContext::getText).collect(Collectors.toList())
            )
        );
    }
}
