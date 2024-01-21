package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Result;
import xyz.cofe.delphi.lexer.parser.LexCondCompileLexer;
import xyz.cofe.delphi.lexer.parser.LexCondCompileParser;

import java.util.ArrayList;
import java.util.List;

import static xyz.cofe.delphi.lexer.parser.LexCondCompileParser.*;

/**
 * Вычисление условной компиляции
 */
public class EvalCondition {
    public static Result<ExprContext,Throwable> parse(String source, String sourceName){
        if( source==null )throw new IllegalArgumentException("source==null");
        if( sourceName==null )throw new IllegalArgumentException("sourceName==null");

        var charStr = CharStreams.fromString(source,sourceName);
        LexCondCompileLexer lexer = new LexCondCompileLexer(charStr);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        LexCondCompileParser parser = new LexCondCompileParser(tokenStream);

        try {
            var e = parser.expr();
            return Result.ok(e);
        } catch (Throwable err){
            return Result.error(err);
        }
    }

    public static class EvalError extends Error {
        public EvalError() {
        }

        public EvalError(String message) {
            super(message);
        }

        public EvalError(String message, Throwable cause) {
            super(message, cause);
        }

        public EvalError(Throwable cause) {
            super(cause);
        }
    }

    private static EvalError unExpected(ParserRuleContext ctx){
        if( ctx==null )throw new IllegalArgumentException("ctx==null");
        return new EvalError("unExpected");
    }

    private static EvalError notImplemented(){
        return new EvalError("not implemented");
    }

    public sealed interface Value {
        record Bool( boolean value ) implements Value {}
        record Numeric( double value ) implements Value {}
        record Variable( String name ) implements Value {}
    }

    public interface Compute {
        Result<Value,EvalError> or(Value left,Value right);
        Result<Value,EvalError> and(Value left,Value right);
        Result<Value,EvalError> lessOrEquals(Value left,Value right);
        Result<Value,EvalError> moreOrEquals(Value left,Value right);
        Result<Value,EvalError> less(Value left,Value right);
        Result<Value,EvalError> more(Value left,Value right);
        Result<Value,EvalError> equals(Value left,Value right);
        Result<Value,EvalError> notEquals(Value left,Value right);
        Result<Value,EvalError> invert(Value value);
        Result<Value,EvalError> plus(Value value);
        Result<Value,EvalError> minus(Value value);
        Result<Value,EvalError> call(Value base,ImList<Value> args);

        static Compute dummy(){
            return new Compute() {
                @Override
                public Result<Value, EvalError> or(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> and(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> lessOrEquals(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> moreOrEquals(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> less(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> more(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> equals(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> notEquals(Value left, Value right) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> invert(Value value) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> plus(Value value) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> minus(Value value) {
                    return Result.error(notImplemented());
                }

                @Override
                public Result<Value, EvalError> call(Value base, ImList<Value> args) {
                    return Result.error(notImplemented());
                }
            };
        }
    }

    private final Compute compute;

    public EvalCondition(Compute compute){
        if( compute==null )throw new IllegalArgumentException("compute==null");
        this.compute = compute;
    }

    public Result<Value,EvalError> eval( ExprContext expr ){
        if( expr==null )throw new IllegalArgumentException("expr==null");
        if( expr.logicOp()==null )return Result.error(unExpected(expr));
        return eval(expr.logicOp());
    }

    private Result<Value,EvalError> eval(LogicOpContext orOpContext){
        if( orOpContext.left==null )return Result.error(unExpected(orOpContext));
        return eval(orOpContext.left).fmap( left -> {
            if( orOpContext.right==null )return Result.ok(left);
            return eval(orOpContext.right).fmap(right -> {
                return compute.or(left,right);
            });
        });
    }

    private Result<Value,EvalError> eval(AndOpContext andOpContext){
        if( andOpContext.left==null )return Result.error(unExpected(andOpContext));
        return eval(andOpContext.left).fmap( left -> {
            if( andOpContext.right==null )return Result.ok(left);
            return eval(andOpContext.right).fmap(right -> {
                return compute.and(left,right);
            });
        });
    }

    private Result<Value,EvalError> eval(RelOpContext relOp){
        if( relOp.left==null )return Result.error(unExpected(relOp));
        return eval(relOp.left).fmap( left -> {
            if( relOp.op!=null && relOp.right!=null ){
                return eval(relOp.right).fmap( right -> {
                    switch (relOp.op.getText()){
                        case "<=":
                            return compute.lessOrEquals(left,right);
                        case ">=":
                            return compute.moreOrEquals(left,right);
                        case "<":
                            return compute.less(left,right);
                        case ">":
                            return compute.more(left,right);
                        case "=":
                            return compute.equals(left,right);
                        case "!=":
                        case "<>":
                            return compute.notEquals(left,right);
                        default:
                            return Result.error(unExpected(relOp));
                    }
                });
            }else{
                return Result.ok(left);
            }
        });
    }

    private Result<Value,EvalError> eval(UnaryOpContext unaryOp){
        if( unaryOp.atom()==null )return Result.error(unExpected(unaryOp));
        return eval(unaryOp.atom()).fmap( value -> {
            if (unaryOp.op != null && !unaryOp.isEmpty()) {
                switch (unaryOp.op.getText().toLowerCase()){
                    case "!":
                    case "not":
                        return compute.invert(value);
                    case "-":
                        return compute.minus(value);
                    case "+":
                        return compute.plus(value);
                    default:
                        break;
                }
                return Result.ok(value);
            }else{
                return Result.ok(value);
            }
        });
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Result<Value,EvalError> eval(AtomContext atom ){
        Result<Value,EvalError> v = null;
        if( atom.IDENT()!=null ){
            v = Result.ok( ident(atom.IDENT()) );
        }else if( atom.NUM()!=null ){
            v = Result.ok( numeric(atom.NUM()) );
        }else if( atom.expr()!=null ){
            v = eval(atom.expr());
        }else{
            return Result.error(unExpected(atom));
        }

        if( atom.postfix()!=null && !atom.postfix().isEmpty() ){
            for( var postfix: atom.postfix() ){
                if( v.isOk() ) {
                    var argsRes = args(postfix);
                    if (argsRes.isError()) return Result.error(argsRes.getError().get());

                    var args = argsRes.getOk().get();
                    var base = v.getOk().get();
                    v = compute.call(base, args);
                }
            }
        }

        return v;
    }

    private Value.Variable ident(TerminalNode terminalNode){
        return new Value.Variable(terminalNode.getText());
    }
    private Value.Numeric numeric(TerminalNode terminalNode){
        var n = Double.parseDouble(terminalNode.getText());
        return new Value.Numeric(n);
    }

    private Result<ImList<Value>,EvalError> args(PostfixContext postfix){
        if(postfix.call()!=null)return args(postfix.call());
        return Result.error(unExpected(postfix));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private Result<ImList<Value>,EvalError> args(CallContext call){
        if( call.expr()==null || call.expr().isEmpty() )return Result.ok(ImList.of());
        List<Value> lst = new ArrayList<>();
        for( var e : call.expr() ){
            var r = eval(e);
            if( r.isError() )return Result.error(r.getError().get());
            lst.add(r.getOk().get());
        }
        return Result.ok(ImList.of(lst));
    }
}
