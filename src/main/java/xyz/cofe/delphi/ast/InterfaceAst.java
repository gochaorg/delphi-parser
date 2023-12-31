package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

import static xyz.cofe.delphi.ast.AstNode.upcast;
import static xyz.cofe.delphi.impl.Indent.indent;

/**
 * Описание интерфейса
 *
 * @param parents  родительский интерфейс(ы)
 * @param itfType  обычный или IDispatch
 * @param guid     идентификатор
 * @param body     содержание интерфейса
 * @param position позиция в исходном коде
 */
public record InterfaceAst(
    ImList<TypeIdentAst> parents,
    InterfaceType itfType,
    Optional<String> guid,
    ImList<InterfaceItemAst> body,
    SourcePosition position,
    ImList<Comment> comments
) implements StructuredTypeAst, TypeDeclAst, SrcPos, Commented<InterfaceAst> {
    @Override
    public InterfaceAst astUpdate(AstUpdate.UpdateContext ctx) {
        var parents = ctx.update(this.parents);
        Optional<ImList<InterfaceItemAst>> body = ctx.update(this.body);

        var res = this;
        if (ctx instanceof AstUpdate.CommentingContext cc) {
            res = cc.commenting(res);
        }

        if (parents.isEmpty()
            && body.isEmpty()
            && res.comments == this.comments
        ) return this;

        return new InterfaceAst(
            parents.orElse(this.parents),
            itfType,
            guid,
            body.orElse(this.body),
            position,
            res.comments
        );
    }

    @Override
    public InterfaceAst withComments(ImList<Comment> comments) {
        return this;
    }

    @Override
    public ImList<? extends AstNode> nestedAstNodes() {
        return upcast(parents).append(upcast(body));
    }

    public static InterfaceAst of(DelphiParser.InterfaceTypeDeclContext ctx) {
        ImList<TypeIdentAst> parents = ctx.classParent() != null && !ctx.classParent().isEmpty() ?
            ImListLinked.of(ctx.classParent().genericTypeIdent()).map(TypeIdentAst::of)
            : ImListLinked.of();

        Optional<String> guid =
            ctx.interfaceGuid() == null || ctx.interfaceGuid().isEmpty() ?
                Optional.empty() :
                Optional.of(ctx.interfaceGuid().getText());

        var itype = switch (ctx.interfaceKey().INTERFACE().getText().toLowerCase()) {
            case "interface" -> InterfaceType.Basic;
            case "dispinterface" -> InterfaceType.DispInterface;
            default -> throw AstParseError.unExpected();
        };

        return new InterfaceAst(
            parents,
            itype,
            guid,
            ImListLinked.of(ctx.interfaceItem()).map(InterfaceItemAst::of),
            SourcePosition.of(ctx),
            ImListLinked.of()
        );
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("Interface\n");

        sb.append("  itfType: ").append(itfType).append("\n");
        guid.ifPresent(s -> sb.append("  guid: ").append(s).append("\n"));

        if (parents.size() > 0) {
            sb.append("  parent:\n");
            parents.each(p -> {
                sb.append(indent("    ", p.toString()));
            });
        }

        if (body.size() > 0) {
            body.each(itm -> {
                sb.append(indent("  ", itm.toString()));
            });
        }

        return sb.toString();
    }

    public enum InterfaceType {
        Basic,
        DispInterface
    }
}
