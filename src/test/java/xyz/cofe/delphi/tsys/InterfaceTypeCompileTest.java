package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class InterfaceTypeCompileTest {
    private static PascalFile sampleFile = PascalFile.parse(
        textResource("/samples/Map.pas"),
        "Map.pas"
    );

    private static PascalFile.Unit sampleUnit =
        (PascalFile.Unit)sampleFile;

    @Test
    public void try1(){
        var types = sampleUnit.api().declarations()
            .find(dec -> dec instanceof TypeSection)
            .map(d -> (TypeSection)d).get().types();

        var iStringMap = types.fmap( a ->
            a.typeIdent().equals( TypeIdent.of("IStringMap") ) &&
                a.typeDecl() instanceof TypeDecl.Interface itf
                ? ImListLinked.of(itf) : ImListLinked.of()
        ).head().get();

        var funs = iStringMap.body().fmap(itm ->
            itm instanceof ClassMethod.Function fn
                ? ImListLinked.of(fn) : ImListLinked.of()
        );

        var countFn = funs.find(c -> c.name().equalsIgnoreCase("count")).get();
        assertTrue( countFn.result().equals( TypeIdent.of("Integer") ) );
        
        var getFn = funs.find(c -> c.name().equalsIgnoreCase("get")).get();
        var existsFn = funs.find(c -> c.name().equalsIgnoreCase("exists")).get();
        var keyFn = funs.find(c -> c.name().equalsIgnoreCase("key")).get();
        var putFn = funs.find(c -> c.name().equalsIgnoreCase("put")).get();
        var deleteFn = funs.find(c -> c.name().equalsIgnoreCase("delete")).get();
        var toStringFn = funs.find(c -> c.name().equalsIgnoreCase("toString")).get();
    }
}
