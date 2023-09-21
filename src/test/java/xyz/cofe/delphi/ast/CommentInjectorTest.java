package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.ast.PascalFileParseTest.textResource;
import static xyz.cofe.delphi.impl.Indent.indent;

public class CommentInjectorTest {

    @Test
    public void inject(){
        var pascal_file = PascalFile.parse(
            textResource("/samples/Map.pas"),
            "Map.pas");

        assertTrue(pascal_file instanceof PascalFile.Unit);
        var unit = (PascalFile.Unit)pascal_file;

        var injector = new CommentInjector();
        var commented = injector.inject(unit);

        var types = commented.api().declarations()
            .find(dec -> dec instanceof TypeSection)
            .map(d -> (TypeSection)d).get().types();

        var iStringMap = types.fmap( a ->
            a.typeIdent().equals( TypeIdent.of("IStringMap") ) &&
                a.typeDecl() instanceof TypeDecl.Interface itf
                ? ImListLinked.of(itf) : ImListLinked.of()
        ).head().get();

        System.out.println(iStringMap);

    }
}
