package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.Tuple;
import xyz.cofe.delphi.ast.PascalFileAst;
import xyz.cofe.delphi.tsys.tm.Constructor;
import xyz.cofe.delphi.tsys.tm.NamedType;
import xyz.cofe.delphi.tsys.tm.TypeName;

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

        var tRefs = TypeResolver.typeRefs(unit);
        tRefs.each(System.out::println);

        var ctorArgsTRefs = tRefs.fmap(TypeResolver.ArgOfMethod.class).fmap(
            arg -> arg.fun() instanceof Constructor ctor && arg.classOrItfType() instanceof NamedType nt ?
                ImListLinked.of(Tuple.of(arg.argType(), arg.arg(), ctor, nt.name())) : ImListLinked.of()
        );

        var copyiCtorArg = ctorArgsTRefs.find( ctor ->
            ctor._4().equals(TypeName.of("Map.TStringMap"))
            && ctor._3().getName().equalsIgnoreCase("copyi")
            && ctor._2().getName().equalsIgnoreCase("sample")
        );

        tRefs.each( tref -> {
            System.out.println("---------------");
            System.out.println("type ref: ");
            System.out.println(indent("    ", tref.toString()));

            var resolveOpt = TypeResolver.resolve(tref, ts);
            resolveOpt.ifPresentOrElse( resolvedType -> {
                System.out.println("resolved as:");
                System.out.println(indent("    ", resolvedType.toString()));
            }, () -> {
                System.out.println("not resolved");
            });
        });
    }
}
