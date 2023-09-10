package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public record ProcDecl(
    String name,
    ImList<Argument,?> arguments,
    Optional<TypeDecl> result
) implements InterfaceDecl {
    static ProcDecl of(DelphiParser.ProcDeclHeadingContext prc) {
        var params = ImListLinked.<Argument>of();
        var formal_params =
            Argument.of(prc.formalParameterSection().formalParameterList());

        var fun_name = prc.ident().getText();
        var fun_res = Optional.<TypeDecl>empty();

        return new ProcDecl(
            fun_name,
            ImListLinked.of(params.reverse()),
            fun_res
        );
    }
}
