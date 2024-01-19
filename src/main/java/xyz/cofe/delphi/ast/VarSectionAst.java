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
    extends InterfaceDecl, AstNode
{
    /**
     * Перечень переменных
     * @param key некая особенность переменной в отношении потоков
     * @param variables перемень переменных
     * @param position позиция в исзоднике
     */
    record Variables(
        VarKey key,
        ImList<VarDeclaration> variables,
        SourcePosition position
    ) implements VarSectionAst, AstNode, SrcPos {
        @Override
        public Variables astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(key).append(upcast(variables));
        }

        static Variables of(DelphiParser.VarSectionContext ctx){
            var key = VarKey.Var;

            if( ctx.varKey()!=null && ctx.varKey().getText()!=null ){
                if(ctx.varKey().getText().toLowerCase().startsWith("var")) {
                    key = VarKey.Var;
                }else if(ctx.varKey().getText().toLowerCase().startsWith("thre")) {
                    key = VarKey.ThreadVar;
                }else {
                    throw AstParseError.unExpected(ctx.varKey());
                }
            }

            ImList<VarDeclaration> lst = ImListLinked.of(ctx.varDeclaration())
                .fmap(VarDeclaration::of);

            return new Variables( key, lst, SourcePosition.of(ctx) );
        }
    }

    public static enum VarKey implements AstNode {
        Var,
        ThreadVar
    }

    /**
     * Определение переменной
     * @param name имя переменной
     * @param type тип переменной
     * @param valueSpec Спецификация переменной
     * @param position Позиция в исходнике
     */
    record VarDeclaration(
        String name,
        TypeDeclAst type,
        Optional<VarValueSpec> valueSpec,
        SourcePosition position
    ) implements AstNode, SrcPos {
        @Override
        public VarDeclaration astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(type).append(upcast(valueSpec));
        }

        static ImList<VarDeclaration> of(DelphiParser.VarDeclarationContext ctx){
            var type = TypeDeclAst.of(ctx.typeDecl());
            Optional<VarValueSpec> valueSpec = Optional.empty();

            if(ctx.varValueSpec()!=null && !ctx.varValueSpec().isEmpty()){
                valueSpec = Optional.of(
                    VarValueSpec.of(ctx.varValueSpec())
                );
            }

            var spec = valueSpec;

            return ImListLinked.of(ctx.identListFlat().ident())
                .map(RuleContext::getText)
                .map(name -> new VarDeclaration(name,type,spec,SourcePosition.of(ctx)));
        }
    }

    sealed interface VarValueSpec extends AstNode {
        @Override
        VarValueSpec astUpdate(AstUpdate.UpdateContext ctx);

        static VarValueSpec of(DelphiParser.VarValueSpecContext ctx){
            if(ctx.ABSOLUTE()!=null){
                if(ctx.ident()!=null && ctx.ident().getText()!=null){
                    return new AbsoluteId(ctx.ident().getText());
                }
                if(ctx.constExpression()!=null && !ctx.constExpression().isEmpty()){
                    return new AbsoluteExp(ConstSectionAst.ConstExpression.of(ctx.constExpression()));
                }

                throw AstParseError.unExpected(ctx);
            }
            if(ctx.constExpression()!=null && !ctx.constExpression().isEmpty()){
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

    record AbsoluteExp(ConstSectionAst.ConstExpression expression) implements VarValueSpec, AstNode {
        @Override
        public AbsoluteExp astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }

    record Expr(ConstSectionAst.ConstExpression expression) implements VarValueSpec, AstNode {
        @Override
        public Expr astUpdate(AstUpdate.UpdateContext ctx) {
            return this;
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return upcast(expression);
        }
    }
}
