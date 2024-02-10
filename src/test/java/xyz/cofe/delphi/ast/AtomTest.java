package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.delphi.lexer.TokenizedFile;
import xyz.cofe.delphi.parser.DelphiParser;

public class AtomTest {
    @Test
    public void str(){
        var sample = "  'abc'#10'xyz''def' ";
        var parser = TokenizedFile.parse(sample,"src").toDelphiParser();
        var ast = parser.stringFactor();
        var iAst = AtomAst.StringValue.of(ast);
    }

    @Test
    public void set(){
        var sample = "[ 1..2, 7, 4..5, 6 ]";
        var parser = TokenizedFile.parse(sample,"src").toDelphiParser();
        var ast = parser.setExpression();
        var iAst = AtomAst.DelphiSet.of(ast);
        System.out.println(iAst);
    }
}
