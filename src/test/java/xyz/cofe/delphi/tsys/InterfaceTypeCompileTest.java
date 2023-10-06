package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class InterfaceTypeCompileTest {
    private static PascalFileAst sampleFile = PascalFileAst.parse(
        textResource("/samples/Map.pas"),
        "Map.pas",true
    );

    private static PascalFileAst.Unit sampleUnit =
        (PascalFileAst.Unit)sampleFile;

    @Test
    public void try1(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var itfOpt = ts.get(TypeName.of("Map","IStringMap"));

        assertTrue(itfOpt.isPresent());
        assertTrue(itfOpt.get() instanceof InterfaceType);
        var itf = (InterfaceType)itfOpt.get();

        itf.getBody().forEach( itm -> System.out.println(itm) );
    }
}
