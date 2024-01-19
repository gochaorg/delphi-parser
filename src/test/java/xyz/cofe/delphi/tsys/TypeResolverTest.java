package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Tuple;
import xyz.cofe.delphi.ast.PascalFileAst;
import xyz.cofe.delphi.tsys.tm.Constructor;
import xyz.cofe.delphi.tsys.tm.NamedType;
import xyz.cofe.delphi.tsys.tm.TypeName;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;
import static xyz.cofe.delphi.impl.Indent.indent;

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

        var tRefs = TypeRefOwner.typeRefs(unit);

        var iStringMapType = ts.getType(TypeName.of("Map","iStringMap")).get();

        var copyiArg = tRefs.fmap(TypeRefOwner.ArgOfConstructorNamed.class).find(a -> a.fun().getName().equalsIgnoreCase("copyi")).get();
        var copyiArgResolved = new AtomicBoolean(false);

        tRefs.each( tref -> {
            System.out.println("---------------");
            System.out.println("type ref: ");
            System.out.println(indent("    ", tref.toString()));

            var resolveOpt = TypeResolver.resolve(tref, ts);
            resolveOpt.ifPresentOrElse( resolvedType -> {
                System.out.println("resolved as:");
                System.out.println(indent("    ", resolvedType.toString()));

                if(tref==copyiArg && resolvedType.resolved()==iStringMapType) copyiArgResolved.set(true);
            }, () -> {
                System.out.println("not resolved");
            });
        });

        assertTrue(copyiArgResolved.get());
    }
}
