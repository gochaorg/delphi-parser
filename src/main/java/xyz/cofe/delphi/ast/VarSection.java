package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Секция переменных
 */
public sealed interface VarSection
    extends InterfaceDecl
{
    /**
     * Перечень переменных
     * @param key некая особенность переменной в отношении потоков
     * @param variables перемень переменных
     */
    record Variables(VarKey key, ImList<VarDeclaration,?> variables) implements VarSection {
        static Variables of(DelphiParser.VarSectionContext ctx){
            var key = VarKey.Var;
            if( ctx.varKey()!=null && ctx.varKey().getText()!=null ){
                if(ctx.varKey().getText().toLowerCase().startsWith("var")) key = VarKey.Var;
                if(ctx.varKey().getText().toLowerCase().startsWith("thre")) key = VarKey.ThreadVar;
                throw AstParseError.unExpected(ctx.varKey());
            }

            ImList<VarDeclaration,?> lst = ImListLinked.of(ctx.varDeclaration())
                .fmap(VarDeclaration::of);

            return new Variables( key, lst );
        }
    }

    public static enum VarKey {
        Var,
        ThreadVar
    }

    /**
     * Определение переменной
     * @param name имя переменной
     * @param type тип переменной
     * @param valueSpec Спецификация переменной
     */
    record VarDeclaration(
        String name,
        TypeDecl type,
        Optional<VarValueSpec> valueSpec
    ) {
        static ImList<VarDeclaration,?> of(DelphiParser.VarDeclarationContext ctx){
            var type = TypeDecl.of(ctx.typeDecl());
            Optional<VarValueSpec> valueSpec = Optional.empty();
            if(ctx.varValueSpec()!=null && !ctx.varValueSpec().isEmpty()){
                valueSpec = Optional.of(
                    VarValueSpec.of(ctx.varValueSpec())
                );
            }
            var spec = valueSpec;

            return ImListLinked.of(ctx.identListFlat().ident())
                .map(RuleContext::getText)
                .map(name -> new VarDeclaration(name,type,spec));
        }
    }

    sealed interface VarValueSpec {
        static VarValueSpec of(DelphiParser.VarValueSpecContext ctx){
            if(ctx.ABSOLUTE()!=null){
                if(ctx.ident()!=null && ctx.ident().getText()!=null){
                    return new AbsoluteId(ctx.ident().getText());
                }
                if(ctx.constExpression()!=null && !ctx.constExpression().isEmpty()){
                    return new AbsoluteExp(ConstSection.ConstExpression.of(ctx.constExpression()));
                }
                throw AstParseError.unExpected(ctx);
            }
            if(ctx.constExpression()!=null && !ctx.constExpression().isEmpty()){
                return new Expr(ConstSection.ConstExpression.of(ctx.constExpression()));
            }
            throw AstParseError.unExpected(ctx);
        }
    }

    record AbsoluteId(String name) implements VarValueSpec {}
    record AbsoluteExp(ConstSection.ConstExpression expression) implements VarValueSpec {}
    record Expr(ConstSection.ConstExpression expression) implements VarValueSpec {}
}
