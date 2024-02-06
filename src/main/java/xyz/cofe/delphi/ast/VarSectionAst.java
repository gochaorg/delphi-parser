package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import static xyz.cofe.delphi.ast.AstNode.upcast;

import java.util.Optional;

/**
 * Секция переменных
 */
public sealed interface VarSectionAst
    extends InterfaceDecl,
            AstNode {

    @Override
    VarSectionAst astUpdate(AstUpdate.UpdateContext ctx);

    /**
     * Перечень переменных
     *
     * @param key       некая особенность переменной в отношении потоков
     * @param variables перемень переменных
     * @param position  позиция в исзоднике
     */
    record Variables(
        VarKey key,
        ImList<VarDeclaration> variables,
        SourcePosition position,
        ImList<Comment> comments
    ) implements VarSectionAst,
                 AstNode,
                 SrcPos,
                 Commented<Variables> {
        @Override
        public Variables astUpdate(AstUpdate.UpdateContext ctx) {
            var vars = ctx.update(variables);
            var cmts = ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;
            if (cmts == this && vars.isEmpty()) return this;
            return new Variables(key, variables, position, cmts.comments);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(key).append(upcast(variables));
        }

        static Variables of(DelphiParser.VarSectionContext ctx) {
            var key = VarKey.Var;

            if (ctx.varKey() != null && ctx.varKey().getText() != null) {
                if (ctx.varKey().getText().toLowerCase().startsWith("var")) {
                    key = VarKey.Var;
                } else if (ctx.varKey().getText().toLowerCase().startsWith("thre")) {
                    key = VarKey.ThreadVar;
                } else {
                    throw AstParseError.unExpected(ctx.varKey());
                }
            }

            ImList<VarDeclaration> lst = ImListLinked.of(ctx.varDeclaration())
                .fmap(VarDeclaration::of);

            return new Variables(key, lst, SourcePosition.of(ctx), ImList.of());
        }

        @Override
        public Variables withComments(ImList<Comment> comments) {
            return new Variables(key, variables, position, comments);
        }
    }

    public static enum VarKey implements AstNode {
        Var,
        ThreadVar
    }

    /**
     * Определение переменной
     *
     * @param name       имя переменной
     * @param type       тип переменной
     * @param valueSpec  Спецификация переменной
     * @param position   Позиция в исходнике
     * @param comments   Комментарии
     * @param attributes Аттрибуты
     */
    record VarDeclaration(
        String name,
        TypeDeclAst type,
        Optional<VarValueSpec> valueSpec,
        SourcePosition position,
        ImList<CustomAttributeAst> attributes,
        ImList<Comment> comments
    ) implements AstNode,
                 SrcPos,
                 Commented<VarDeclaration>,
                 AstUpdate<VarDeclaration> {
        @Override
        public VarDeclaration astUpdate(AstUpdate.UpdateContext ctx) {
            var cmnt = ctx instanceof AstUpdate.CommentingContext cc ?
                cc.commenting(this) : this;

            var valSpec = ctx.updateUnsafe(valueSpec);
            var attr = ctx.update(attributes);
            var t = type.astUpdate(ctx);

            if (valSpec.isEmpty()
                && attr.isEmpty()
                && t == type
                && cmnt == this
            ) return this;

            return new VarDeclaration(
                name,
                t,
                valSpec.orElse(valueSpec),
                position,
                attr.orElse(attributes),
                cmnt.comments
            );
        }

        @Override
        public VarDeclaration withComments(ImList<Comment> comments) {
            return new VarDeclaration(
                name,
                type,
                valueSpec,
                position,
                attributes,
                comments
            );
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(type).append(upcast(valueSpec)).append(upcast(attributes));
        }

        static ImList<VarDeclaration> of(DelphiParser.VarDeclarationContext ctx) {
            var type = TypeDeclAst.of(ctx.typeDecl());
            Optional<VarValueSpec> valueSpec = Optional.empty();

            if (ctx.varValueSpec() != null && !ctx.varValueSpec().isEmpty()) {
                valueSpec = Optional.of(
                    VarValueSpec.of(ctx.varValueSpec())
                );
            }

            var spec = valueSpec;

            return ImListLinked.of(ctx.identListFlat().paramName())
                .map(RuleContext::getText)
                .map(name -> new VarDeclaration(
                        name, type, spec, SourcePosition.of(ctx),
                        ctx.customAttribute() != null && !ctx.customAttribute().isEmpty() ?
                            ImList.of(ctx.customAttribute()).map(CustomAttributeAst::of) :
                            ImList.of(),
                        ImList.of()
                    )
                );
        }
    }

    sealed interface VarValueSpec extends AstNode {
        @Override
        VarValueSpec astUpdate(AstUpdate.UpdateContext ctx);

        static VarValueSpec of(DelphiParser.VarValueSpecContext ctx) {
            if (ctx.ABSOLUTE() != null) {
                if (ctx.ident() != null && ctx.ident().getText() != null) {
                    return new AbsoluteId(ctx.ident().getText());
                }
                if (ctx.constExpression() != null && !ctx.constExpression().isEmpty()) {
                    return new AbsoluteExp(ConstSectionAst.ConstExpression.of(ctx.constExpression()));
                }

                throw AstParseError.unExpected(ctx);
            }
            if (ctx.constExpression() != null && !ctx.constExpression().isEmpty()) {
                return new Expr(ConstSectionAst.ConstExpression.of(ctx.constExpression()));
            }
            throw AstParseError.unExpected(ctx);
        }
    }

    record AbsoluteId(String name) implements VarValueSpec {
        @Override
        public AbsoluteId astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }
    }

    record AbsoluteExp(ConstSectionAst.ConstExpression expression) implements VarValueSpec,
                                                                              AstNode {
        @Override
        public AbsoluteExp astUpdate(AstUpdate.UpdateContext ctx) {
            var exp = expression.astUpdate(ctx);
            if (exp == expression) return this;
            return new AbsoluteExp(exp);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Expr(ConstSectionAst.ConstExpression expression) implements VarValueSpec,
                                                                       AstNode {
        @Override
        public Expr astUpdate(AstUpdate.UpdateContext ctx) {
            var expr = expression.astUpdate(ctx);
            if (expr == expression) return this;
            return new Expr(expr);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }
}
