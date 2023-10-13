package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Tuple;
import xyz.cofe.delphi.ast.PascalFileAst;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class TypeResolverTest {
    private static final PascalFileAst sampleFile = PascalFileAst.parse(
        textResource("/samples/Map.pas"),
        "Map.pas",true
    );

    private static final PascalFileAst.Unit sampleUnit =
        (PascalFileAst.Unit)sampleFile;

    @Test
    public void findTypeRefs(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var unit = ts.unitMap().get(TypeName.of("Map")).get(0);
        assertTrue(unit!=null);

        var tRefs = TypeResolver.typeRefs(unit);
        tRefs.each(System.out::println);

        var ctorArgsTRefs = tRefs.fmap(TypeResolver.ArgOfMethod.class).fmap(
            arg -> arg.fun() instanceof Constructor ctor && arg.classOrItfType() instanceof NamedType nt ?
                ImListLinked.of(Tuple.of(arg.argType(), arg.arg(), ctor, nt.name())) : ImListLinked.of()
        );

        ctorArgsTRefs.find( ctor ->
            ctor._4().equals(TypeName.of("Map.TStringMap"))
            && ctor._3().getName().equalsIgnoreCase("copyi")
            && ctor._2().getName().equalsIgnoreCase("sample")
        );
    }
}
