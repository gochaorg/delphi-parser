package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;
import java.util.stream.Collectors;

import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Единица компиляции
 *
 * @param name     имя модуля
 * @param api      Экспортируемое api
 * @param position Позиция в исходниках
 * @param comments Комментарии
 */
public record UnitAst(
    ImList<String> name,
    ImList<FDirective.Hinting> hintings,
    UnitInterfaceAst api,
    Optional<UnitImplementationAst> impl,
    UnitBlock unitBlock,
    SourcePosition position,
    ImList<Comment> comments
) implements PascalFileAst,
             Commented<UnitAst> {
    @Override
    public UnitAst withComments(ImList<Comment> comments) {
        return new UnitAst(name, hintings, api, impl, unitBlock, position, comments);
    }

    @Override
    public String toString() {
        return "unit " + name + "\n" +
            "interface:\n" +
            indent("  ", api.toString());
    }
}
