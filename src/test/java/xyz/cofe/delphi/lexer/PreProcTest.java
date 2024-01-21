package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.Result;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.io.fs.File;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static xyz.cofe.delphi.TextResource.textResource;

public class PreProcTest {
    /*
    Вот это

    {$IFDEF Cond1}
        {$IF Cond3}
            a = 1;
        {$ELSE}
            a = 2;
        {$ENDIF}
    {$ELSEIF Cond2}
        {$IF Cond3}
            a = 3;
        {$ELSE}
            a = 4;
        {$ENDIF}
    {$ELSE}
        {$IF Cond3}
            a = 5;
        {$ELSE}
            a = 6;
        {$ENDIF}
    {$ENDIF}

    Можно переписать в это

    {$IFDEF Cond1}
        {$IF Cond3}
            a = 1;
        {$ELSE}
            a = 2;
        {$ENDIF}
    {$ELSE}
        {$IF Cond2}
            {$IF Cond3}
                a = 3;
            {$ELSE}
                a = 4;
            {$ENDIF}
        {$ELSE}
            {$IF Cond3}
                a = 5;
            {$ELSE}
                a = 6;
            {$ENDIF}
        {$ENDIF}
    {$ENDIF}
     */

    @SuppressWarnings("SimplifiableAssertion")
    @Test
    public void conditional(){
        var src = textResource("/aspects/lex-if.pas");
        System.out.println(src);

        var tokFile = TokenizedFile.parse(src,"lexIf.pas");
        var lexParser = new LexAstParser();
        var lexToks = lexParser.parse(tokFile);
        var idx = -1;

        var conditions = new ArrayList<String>();

        for( var lexAst : lexToks ){
            idx += 1;
            System.out.println("index "+idx);

            var level = new AtomicInteger(0);
            LexAstVisitor.visit(
                lexAst,
                la -> {
                    var lvl = level.incrementAndGet();

                    System.out.print("  ".repeat(lvl));
                    if( la instanceof LexAst.ConditionalCompile cc ){
                        System.out.println("condition "+cc.condition());
                        if( cc.condition() instanceof LexAst.Condition.If _if ){
                            conditions.add(".".repeat(lvl)+_if.code());
                        }else if( cc.condition() instanceof LexAst.Condition.IfDef _if ){
                            conditions.add(".".repeat(lvl)+_if.name());
                        }
                    }else{
                        System.out.println(la.getClass().getSimpleName());
                    }
                },
                la -> {
                    level.decrementAndGet();
                }
            );
        }

        System.out.println("....");
        conditions.forEach(System.out::println);

        assertTrue(conditions.size()>=5);
        assertTrue(conditions.get(0).equals(".Cond1"));
        assertTrue(conditions.get(1).equals("...Cond3"));
        assertTrue(conditions.get(2).equals("...Cond2"));
        assertTrue(conditions.get(3).equals(".....Cond4"));
        assertTrue(conditions.get(4).equals(".....Cond5"));
    }

    @Test
    public void conditionalEval(){
        EvalCondition.parse("debug and defined(win)", "src").fold(
            expr -> {
                EvalCondition.Compute compute = new EvalCondition.Compute() {
                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> or(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> and(EvalCondition.Value left, EvalCondition.Value right) {
                        if( left instanceof EvalCondition.Value.Variable l && right instanceof EvalCondition.Value.Bool r ){
                            var la = l.name().equalsIgnoreCase("debug");
                            var rb = r.value();
                            return Result.ok(
                                new EvalCondition.Value.Bool(la && rb)
                            );
                        }
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> lessOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> moreOrEquals(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> less(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> more(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> equals(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> notEquals(EvalCondition.Value left, EvalCondition.Value right) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> invert(EvalCondition.Value value) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> plus(EvalCondition.Value value) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> minus(EvalCondition.Value value) {
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }

                    @Override
                    public Result<EvalCondition.Value, EvalCondition.EvalError> call(EvalCondition.Value base, ImList<EvalCondition.Value> args) {
                        if( base instanceof EvalCondition.Value.Variable baseName && baseName.name().equalsIgnoreCase("defined") ){
                            if( args.get(0).map( v -> v instanceof EvalCondition.Value.Variable sym && sym.name().equalsIgnoreCase("win") ).orElse(false) ){
                                return Result.ok(new EvalCondition.Value.Bool(true));
                            }
                        }
                        return Result.error(new EvalCondition.EvalError("not impl"));
                    }
                };
                var eval = new EvalCondition(compute);
                eval.eval(expr).fold( res -> {
                    System.out.println(res);
                    return null;
                }, err -> {
                    System.out.println(err);
                    return null;
                });
                return null;
            },
            err -> {
                fail(err);
                return null;
            }
        );
    }
}
