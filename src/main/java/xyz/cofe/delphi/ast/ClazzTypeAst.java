package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Описание класса: <span style="font-family:mono">class(parent) ... end;</span>
 *
 * @param state    абстрактный/запечатанный
 * @param parents  родительские классы/интерфейсы
 * @param body     содержание
 * @param position позиция в исходном коде
 * @param comments комментарии
 */
public record ClazzTypeAst(
    Optional<ClassState> state,
    ImList<TypeIdentAst> parents,
    ImList<ClassItemAst> body,
    SourcePosition position,
    ImList<Comment> comments
) implements StructuredTypeAst, SrcPos, Commented<ClazzTypeAst>, TypeDeclAst {
    @Override
    public ClazzTypeAst withComments(ImList<Comment> comments) {
        return new ClazzTypeAst(state, parents, body, position, comments);
    }

    public String toString() {
        var sb = new StringBuilder();
        sb.append("Class\n");

        state.ifPresent(s -> sb.append("  state: ").append(s).append("\n"));

        if (parents.size() > 0) {
            sb.append("  parent:\n");
            parents.each(p -> {
                sb.append(indent("    ", p.toString())).append("\n");
            });
        }

        if (body.size() > 0) {
            sb.append("  body:\n");
            body.each(itm -> {
                sb.append(indent("    ", itm.toString())).append("\n");
            });
        }

        return sb.toString();
    }

    static ClazzTypeAst of(DelphiParser.ClassTypeDeclContext ctx) {
        var state = Optional.<ClassState>empty();
        if (ctx.classState() != null && !ctx.classState().isEmpty()) {
            if (ctx.classState().getText().toLowerCase().startsWith("seal"))
                state = Optional.of(ClassState.Sealed);
            else if (ctx.classState().getText().toLowerCase().startsWith("abst"))
                state = Optional.of(ClassState.Abstract);
            else throw AstParseError.unExpected();
        }

        ImList<TypeIdentAst> parents = ImListLinked.of();
        if (ctx.classParent() != null && !ctx.classParent().isEmpty()) {
            parents = ImListLinked.of(ctx.classParent().genericTypeIdent())
                .map(TypeIdentAst::of);
        }

        ImList<ClassItemAst> body =
            ctx.classItem() != null && !ctx.classItem().isEmpty() ?
                ImListLinked.of(ctx.classItem()).fmap(
                    ClassItemAst::of
                ) :
                ImListLinked.of();

        // todo methods?
        return new ClazzTypeAst(state, parents, body, SourcePosition.of(ctx), ImListLinked.of());
    }

    /**
     * Абстрактный или запечатанный класс
     */
    public enum ClassState {
        Sealed,
        Abstract
    }
}
