package xyz.cofe.delphi.ast;

import org.antlr.v4.runtime.ParserRuleContext;

/**
 * Ошибка парсинга AST
 */
public class AstParseError extends Error {
    public AstParseError(String message) {
        super(message);
    }

    public AstParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public AstParseError(Throwable cause) {
        super(cause);
    }

    public static AstParseError notImplemented(){
        return new AstParseError("Not implemented");
    }

    public static AstParseError notImplemented(String message){
        return new AstParseError(message);
    }

    public static AstParseError notImplemented(ParserRuleContext ctx){
        if(ctx==null)throw new IllegalArgumentException("ctx==null");
        return new AstParseError(
            "Not implemented for "+ctx.getClass()+
            "\n"+(
                ctx.getText().length()>40 ?
                    ctx.getText().substring(0,40) : ctx.getText()
            )
        );
    }

    public static AstParseError unExpected(){
        return new AstParseError("UnExpected input");
    }

    public static AstParseError unExpected(ParserRuleContext ctx){
        if(ctx==null)throw new IllegalArgumentException("ctx==null");
        return new AstParseError(
            "UnExpected for "+ctx.getClass()+
                "\n"+(
                ctx.getText().length()>40 ?
                    ctx.getText().substring(0,40) : ctx.getText()
            )
        );
    }
}
