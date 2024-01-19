package xyz.cofe.delphi.ast;

import xyz.cofe.delphi.parser.DelphiParser;

import java.util.Optional;

public sealed interface HintingDirectiveAst extends FunctionDirectiveAst {
    record Deprecated(Optional<String> stringFactor) implements HintingDirectiveAst {}
    record Experimental() implements HintingDirectiveAst {}
    record Platform() implements HintingDirectiveAst {}
    record Library() implements HintingDirectiveAst {}

    static HintingDirectiveAst of(DelphiParser.HintingDirectiveContext ctx){
        if( ctx.DEPRECATED()!=null && ctx.DEPRECATED().getText().equalsIgnoreCase("deprecated")){
            Optional<String> str = ctx.stringFactor()!=null && !ctx.stringFactor().isEmpty()
                ? Optional.of(ctx.stringFactor().getText())
                : Optional.empty();

            return new Deprecated(str);
        }

        if( ctx.EXPERIMENTAL()!=null && !ctx.EXPERIMENTAL().getText().isEmpty()){
            return new Experimental();
        }

        if( ctx.PLATFORM()!=null && !ctx.PLATFORM().getText().isEmpty()){
            return new Platform();
        }

        if( ctx.LIBRARY()!=null && !ctx.LIBRARY().getText().isEmpty()){
            return new Library();
        }

        throw AstParseError.unExpected(ctx);
    }
}
