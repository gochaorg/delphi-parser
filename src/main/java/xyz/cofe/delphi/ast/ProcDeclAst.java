package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.tree.TerminalNode;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Tuple3;
import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

/**
 * Процедура/функция объявленная в публичной секции
 *
 * @param name      имя процедуры
 * @param arguments аргументы
 * @param result    тип результата в случае функции
 */
public record ProcDeclAst(
    String name,
    ImList<ArgumentAst> arguments,
    Optional<TypeDeclAst> result,
    ImList<FDirective.FunctionDirective> directives,
    Optional<ProcBody> body,
    SourcePosition position,
    ImList<Comment> comments
) implements InterfaceDecl,
             DeclSectionAst,
             Commented<ProcDeclAst> // TODO Comments
{
    public sealed interface ProcBody permits BodyAst,
                                             ProcBody.External,
                                             ProcBody.Forward {

        record Forward(
            ImList<FDirective.FunctionDirective> directives,
            SourcePosition position,
            ImList<Comment> comments
        ) implements ProcBody, WithComments<Forward> {}

        sealed interface Key {
            record Name(ExpressionAst expression) implements Key {}
            record Index(ExpressionAst expression) implements Key {}
        }

        record External(
            ImList<Key> keys,
            ImList<FDirective.FunctionDirective> directives,
            SourcePosition position,
            ImList<Comment> comments
        ) implements ProcBody, WithComments<External> {}

        static ProcBody of(DelphiParser.ProcBodyContext ctx) {
            if (ctx == null) throw new IllegalArgumentException("ctx==null");
            if (ctx.FORWARD() != null) {
                return new Forward(
                    ImList.of(ctx.functionDirective()).fmap(FDirective::of),
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            if (ctx.EXTERNAL() != null) {
                ImList<Key> keys = ImList.of();
                var state = "";

                for (var i = 0; i < ctx.getChildCount(); i++) {
                    var c = ctx.getChild(i);
                    switch (state) {
                        case "" -> {
                            if (c instanceof TerminalNode t
                                && t.getText().equalsIgnoreCase("name")
                            ) {
                                state = "name";
                            } else if (
                                c instanceof TerminalNode t
                                    && t.getText().equalsIgnoreCase("name")
                            ) {
                                state = "index";
                            }
                        }
                        case "name" -> {
                            if (c instanceof DelphiParser.ExpressionContext e) {
                                state = "";
                                keys = keys.prepend(
                                    new Key.Name(ExpressionAst.of(e))
                                );
                            }
                        }
                        case "index" -> {
                            if (c instanceof DelphiParser.ExpressionContext e) {
                                state = "";
                                keys = keys.prepend(
                                    new Key.Name(ExpressionAst.of(e))
                                );
                            }
                        }
                    }
                }

                var directives = ImList.of(ctx.functionDirective()).fmap(FDirective::of);

                return new External(
                    keys.reverse(),
                    directives,
                    SourcePosition.of(ctx),
                    ImList.of()
                );
            }

            if( ctx.block()!=null ){
                return BodyAst.of(ctx.block());
            }

            throw AstParseError.unExpected(ctx);
        }
    }

    @Override
    public ProcDeclAst withComments(ImList<Comment> comments) {
        return new ProcDeclAst(name, arguments, result, directives, body, position, comments);
    }

    private static Tuple3<String, ImList<ArgumentAst>, Optional<TypeDeclAst>> of(DelphiParser.ProcDeclHeadingContext ctx) {
        ImList<ArgumentAst> formal_params =
            ctx.formalParameterSection() == null
                ? ImList.of()
                : ctx.formalParameterSection().formalParameterList() == null
                ? ImList.of()
                : ArgumentAst.of(ctx.formalParameterSection().formalParameterList());

        var fun_name = ctx.ident().getText();
        var fun_res = ctx.typeDecl() != null && !ctx.typeDecl().isEmpty()
            ? Optional.of(TypeDeclAst.of(ctx.typeDecl()))
            : Optional.<TypeDeclAst>empty();

        return Tuple3.of(
            fun_name,
            formal_params,
            fun_res
        );
    }

    static ProcDeclAst of(DelphiParser.ProcDeclContext ctx) {
        if (ctx == null) throw new IllegalArgumentException("ctx==null");
        var fTup3 = of(ctx.procDeclHeading());

        var directives = ctx.funcDirective() != null && !ctx.funcDirective().isEmpty()
            ? ImListLinked.of(ctx.funcDirective())
            .map(FDirective::of)
            : ImListLinked.<FDirective.FunctionDirective>of();

        Optional<ProcBody> body =
            ctx.procBody()!=null
                ? Optional.of(ProcBody.of(ctx.procBody()))
                : Optional.empty();

        return new ProcDeclAst(
            fTup3._1(),
            fTup3._2(),
            fTup3._3(),
            directives,
            body,
            SourcePosition.of(ctx),
            ImList.of()
        );
    }
}
