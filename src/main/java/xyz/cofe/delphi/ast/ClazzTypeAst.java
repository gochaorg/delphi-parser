package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Описание класса
 *
 * @param state    абстрактный/запечатанный
 * @param parents  родительские классы/интерфейсы
 * @param body     содержание
 * @param position позиция в исходном коде
 */
public record ClazzTypeAst(
    Optional<ClassState> state,
    ImList<TypeIdentAst> parents,
    ImList<ClassItemAst> body,
    SourcePosition position,
    ImList<Comment> comments
) implements StructuredTypeAst, SrcPos, Commented<ClazzTypeAst>, TypeDeclAst {
    @Override
    public ClazzTypeAst astUpdate(AstUpdate.UpdateContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");

        var parents = ctx.update(this.parents);
        //noinspection unchecked
        Optional<ImList<ClassItemAst>> body = ctx.update(this.body);

        var res = this;
        if (ctx instanceof AstUpdate.CommentingContext cc) res = cc.commenting(res);

        if (parents.isEmpty() && body.isEmpty() && res.comments == this.comments) return this;

        ImList<ClassItemAst> b2 = body.orElse(this.body);

        return new ClazzTypeAst(
            state, parents.orElse(this.parents),
            b2,
            position,
            res.comments);
    }

    @Override
    public ClazzTypeAst withComments(ImList<Comment> comments) {
        return new ClazzTypeAst(state, parents, body, position, comments);
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(parents).append(upcast(body));
    }

    public String toString() {
        var sb = new StringBuilder();
        sb.append("Class\n");

        state.ifPresent(s -> sb.append("  state: ").append(s).append("\n"));

        if (parents.size() > 0) {
            sb.append("  parent:\n");
            parents.each(p -> {
                sb.append(indent("    ", p.toString()));
            });
        }

        if (body.size() > 0) {
            sb.append("  body:\n");
            body.each(itm -> {
                sb.append(indent("    ", itm.toString()));
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
