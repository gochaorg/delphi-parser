package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.RuleContext;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.List;
import java.util.Optional;

/**
 * Аргумент функции/метода
 *
 * <pre>
 *   (parmType)? identListFlat (':' typeDecl)? ('=' expression)?
 * </pre>
 *
 * @param constraint ограничение
 * @param name имя аргумента
 * @param typeDecl тип аргумента
 * @param defaultValue значение по молчанию
 */
public record ArgumentAst(
    Optional<Constraint> constraint,
    String name,
    Optional<TypeDeclAst> typeDecl,
    Optional<ExpressionAst> defaultValue
) implements AstNode {
    /**
     * Указывает направление аргумента out | var | const
     */
    public static enum Constraint {
        Const,
        Var,
        Out
    }

    static ImList<ArgumentAst> of(List<DelphiParser.FormalParameterContext> lst){
        var params = ImListLinked.<ArgumentAst>of();
        for( var f_param : lst ){
            var constraint = Optional.<ArgumentAst.Constraint>empty();
            if( f_param.parmType()!=null
            && !f_param.parmType().isEmpty()
            ){
                constraint = switch( f_param.parmType().getText().toLowerCase() ){
                    case "out" -> Optional.of(ArgumentAst.Constraint.Out);
                    case "var" -> Optional.of(ArgumentAst.Constraint.Var);
                    case "const" -> Optional.of(ArgumentAst.Constraint.Const);
                    default -> Optional.<ArgumentAst.Constraint>empty();
                };
            }else{
                constraint = Optional.empty();
            }

            var id_list =
                f_param.identListFlat().paramName().stream().map(RuleContext::getText).toList();

            var arg_type = Optional.<TypeDeclAst>empty();
            if( f_param.typeDecl()!=null && !f_param.typeDecl().isEmpty() ){
                arg_type = Optional.of(TypeDeclAst.of(f_param.typeDecl()));
            }

            var def_exp = Optional.<ExpressionAst>empty();
            if( f_param.expression()!=null && !f_param.isEmpty() ){
                def_exp = Optional.of(ExpressionAst.of(f_param.expression()));
            }

            for( var id : id_list ){
                var arg = new ArgumentAst(
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

    static ImList<ArgumentAst> of(DelphiParser.FormalParameterListContext lst){
        return of(lst.formalParameter());
    }
}
