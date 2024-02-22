package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Tuple3;
import xyz.cofe.delphi.parser.DelphiParser;

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
    Optional<TypeDeclAst> result,
    ImList<FDirective.FunctionDirective> directives,
    SourcePosition position,
    ImList<Comment> comments

) implements InterfaceDecl, Commented<ProcDeclAst> // TODO Comments
{
    @Override
    public ProcDeclAst withComments(ImList<Comment> comments) {
        return new ProcDeclAst(name,arguments,result,directives,position,comments);
    }

    private static Tuple3<String,ImList<ArgumentAst>,Optional<TypeDeclAst>> of(DelphiParser.ProcDeclHeadingContext ctx) {
        ImList<ArgumentAst> formal_params =
            ctx.formalParameterSection()==null
            ? ImList.of()
            : ctx.formalParameterSection().formalParameterList()==null
                ? ImList.of()
                : ArgumentAst.of(ctx.formalParameterSection().formalParameterList());

        var fun_name = ctx.ident().getText();
        var fun_res = ctx.typeDecl()!=null && !ctx.typeDecl().isEmpty()
            ? Optional.of(TypeDeclAst.of(ctx.typeDecl()))
            : Optional.<TypeDeclAst>empty();

        return Tuple3.of(
            fun_name,
            formal_params,
            fun_res
        );
    }

    static ProcDeclAst of(DelphiParser.ProcDeclContext ctx){
        if( ctx==null ) throw new IllegalArgumentException("ctx==null");
        var fTup3 = of(ctx.procDeclHeading());
        var directives = ctx.funcDirective()!=null && !ctx.funcDirective().isEmpty()
            ? ImListLinked.of(ctx.funcDirective())
            .map(FDirective::of)
            : ImListLinked.<FDirective.FunctionDirective>of();
        return new ProcDeclAst(
            fTup3._1(),
            fTup3._2(),
            fTup3._3(),
            directives,
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
