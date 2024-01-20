package xyz.cofe.delphi.lexer;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;
import xyz.cofe.delphi.parser.DelphiLexer;
import xyz.cofe.io.fs.File;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class PreProcTest {
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
}
