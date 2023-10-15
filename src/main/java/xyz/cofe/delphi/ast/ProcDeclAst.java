package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;
import static xyz.cofe.delphi.ast.AstNode.upcast;

import java.util.Optional;

/**
 * Процедура/функция объявленная в публичной секции
 * @param name имя процедуры
 * @param arguments аргументы
 * @param result тип результата в случае функции
 */
public record ProcDeclAst(
    String name,
    ImList<ArgumentAst> arguments,
    Optional<TypeDeclAst> result
) implements InterfaceDecl, AstNode {
    @Override
    public ProcDeclAst astUpdate(AstUpdate.UpdateContext ctx) {
        return this;
    }

    static ProcDeclAst of(DelphiParser.ProcDeclHeadingContext prc) {
        var params = ImListLinked.<ArgumentAst>of();
        var formal_params =
            ArgumentAst.of(prc.formalParameterSection().formalParameterList());

        var fun_name = prc.ident().getText();
        var fun_res = Optional.<TypeDeclAst>empty();

        return new ProcDeclAst(
            fun_name,
            ImListLinked.of(params.reverse()),
            fun_res
        );
    }
}
