package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Секция переменных
 */
public sealed interface VarSectionAst
    extends InterfaceDecl, DeclSectionAst {

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
        public static Variables of(DelphiParser.VarSectionContext ctx) {
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
    ) implements
      SrcPos,
      Commented<VarDeclaration> {
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
    }

    record AbsoluteExp(ConstSectionAst.ConstExpression expression) implements VarValueSpec,
                                                                              AstNode {
    }

    record Expr(ConstSectionAst.ConstExpression expression) implements VarValueSpec,
                                                                       AstNode {
    }
}
