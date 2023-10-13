package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.List;
import java.util.Optional;

/**
 * Аргумент функции/метода
 * @param constraint ограничение
 * @param name имя аргумента
 * @param typeDecl тип аргумента
 * @param defaultValue значение по молчанию
 */
public record Argument(
    Optional<Constraint> constraint,
    String name,
    Optional<TypeDeclAst> typeDecl,
    Optional<ExpressionAst> defaultValue
) implements AstNode, AstUpdate<Argument>
{
    @Override
    public Argument astUpdate(AstUpdate.UpdateContext updateCtx) {
        return this;
    }

    public static enum Constraint {
        Const,
        Var,
        Out
    }

    static ImList<Argument> of(List<DelphiParser.FormalParameterContext> lst){
        var params = ImListLinked.<Argument>of();
        for( var f_param : lst ){
            var constraint = Optional.<Argument.Constraint>empty();
            if( f_param.parmType()!=null
            && !f_param.parmType().isEmpty()
            ){
                constraint = switch( f_param.parmType().getText().toLowerCase() ){
                    case "out" -> Optional.of(Argument.Constraint.Out);
                    case "var" -> Optional.of(Argument.Constraint.Var);
                    case "const" -> Optional.of(Argument.Constraint.Const);
                    default -> Optional.<Argument.Constraint>empty();
                };
            }else{
                constraint = Optional.empty();
            }

            var id_list =
                f_param.identListFlat().ident().stream().map(RuleContext::getText).toList();

            var arg_type = Optional.<TypeDeclAst>empty();
            if( f_param.typeDecl()!=null && !f_param.typeDecl().isEmpty() ){
                arg_type = Optional.of(TypeDeclAst.of(f_param.typeDecl()));
            }

            var def_exp = Optional.<ExpressionAst>empty();
            if( f_param.expression()!=null && !f_param.isEmpty() ){
                def_exp = Optional.of(ExpressionAst.of(f_param.expression()));
            }

            for( var id : id_list ){
                var arg = new Argument(
                    constraint,
                    id,
                    arg_type,
                    def_exp
                );

                params = params.prepend(arg);
            }
        }
        return params.reverse();
    }

    static ImList<Argument> of(DelphiParser.FormalParameterListContext lst){
        return of(lst.formalParameter());
    }
}
