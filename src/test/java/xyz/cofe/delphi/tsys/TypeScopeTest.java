package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.delphi.ast.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class TypeScopeTest {
    private static PascalFileAst sampleFile = PascalFileAst.parse(
        textResource("/samples/Map.pas"),
        "Map.pas",true
    );

    private static PascalFileAst.Unit sampleUnit =
        (PascalFileAst.Unit)sampleFile;

    @Test
    public void iStringMap(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var itfOpt = ts.get(TypeName.of("Map","IStringMap"));

        assertTrue(itfOpt.isPresent());
        assertTrue(itfOpt.get() instanceof InterfaceType);
        var itf = (InterfaceType)itfOpt.get();

        itf.getBody().forEach(System.out::println);
    }

    @Test
    public void tStringMap(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var clsOpt = ts.get(TypeName.of("Map","TStringMap"));

        assertTrue(clsOpt.isPresent());
        assertTrue(clsOpt.get() instanceof ClassType);
        var cls = (ClassType)clsOpt.get();

        cls.getBody().forEach(System.out::println);
    }
}
