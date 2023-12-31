package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class PascalFileParseTest {

    @Test
    public void try_parse() {
        System.out.println(
            PascalFileAst.parse(
                textResource("/samples/Map.pas"),
                "Map.pas"));
    }


    @Test
    public void try_parse_ignore_case() {
        System.out.println(
            PascalFileAst.parse(
                textResource("/samples/MapIC.pas"),
                "MapIC.pas"));
    }

    @Test
    public void parse_map() {
        var pascal_file = PascalFileAst.parse(
            textResource("/samples/Map.pas"),
            "Map.pas");

        assertTrue(pascal_file instanceof PascalFileAst.Unit);
        var unit = (PascalFileAst.Unit) pascal_file;

        assertTrue(
            unit.api().uses().containsAll(
                NamespaceAst.of("SysUtils"),
                NamespaceAst.of("Variants"),
                NamespaceAst.of("Classes")
            )
        );

        var types = unit.api().declarations()
            .find(dec -> dec instanceof TypeSectionAst)
            .map(d -> (TypeSectionAst) d).get().types();

        var iStringMap = types.fmap(a ->
            a.typeIdent().equals(TypeIdentAst.of("IStringMap")) &&
                a.typeDecl() instanceof InterfaceAst itf
                ? ImListLinked.of(itf) : ImListLinked.of()
        ).head().get();

        var funs = iStringMap.body().fmap(itm ->
            itm instanceof ClassMethodAst.Function fn
                ? ImListLinked.of(fn) : ImListLinked.of()
        );

        var countFn = funs.find(c -> c.name().equalsIgnoreCase("count")).get();
        assertTrue(countFn.result().equals(TypeIdentAst.of("Integer")));

        var getFn = funs.find(c -> c.name().equalsIgnoreCase("get")).get();
        assertTrue(getFn.name().equalsIgnoreCase("get"));
        assertTrue(getFn.arguments().size() == 1);
        assertTrue(getFn.arguments().get(0).get().name().equalsIgnoreCase("name"));
        assertTrue(getFn.arguments().get(0).get().typeDecl().get().equals(
            new StringTypeAst.StrIng(Optional.empty(), SourcePosition.non(), ImListLinked.of())
        ));
        assertTrue(getFn.result().equals(new VariantTypeAst()));

        var existsFn = funs.find(c -> c.name().equalsIgnoreCase("exists")).get();
        assertTrue(existsFn.result().equals(TypeIdentAst.of("boolean")));

        var keyFn = funs.find(c -> c.name().equalsIgnoreCase("key")).get();
        var putFn = funs.find(c -> c.name().equalsIgnoreCase("put")).get();
        var deleteFn = funs.find(c -> c.name().equalsIgnoreCase("delete")).get();
        var toStringFn = funs.find(c -> c.name().equalsIgnoreCase("toString")).get();

        var tStringMap = types.fmap(a ->
            a.typeIdent().equals(TypeIdentAst.of("TStringMap")) &&
                a.typeDecl() instanceof ClazzTypeAst itf
                ? ImListLinked.of(itf) : ImListLinked.of()
        ).head().get();

        var ctrs = tStringMap.body().fmap(itm ->
            itm instanceof ClassMethodAst.Constructor ctr
                ? ImListLinked.of(ctr) : ImListLinked.of()
        );

        var createCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Create")).get();

        var copyCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Copy")).get();
        assertTrue(copyCtr.arguments().size() == 1);
        assertTrue(copyCtr.arguments().get(0).get().typeDecl().get().equals(TypeIdentAst.of("TStringMap")));

        var copyiCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Copyi")).get();
        assertTrue(copyiCtr.arguments().size() == 1);
        assertTrue(copyiCtr.arguments().get(0).get().typeDecl().get().equals(TypeIdentAst.of("IStringMap")));

        var destrs = tStringMap.body().fmap(itm ->
            itm instanceof ClassMethodAst.Destructor ctr
                ? ImListLinked.of(ctr) : ImListLinked.of()
        );
        var destr = destrs.find(c -> c.name().equalsIgnoreCase("Destroy")).get();
        assertTrue(destr.directives().containsAll(new ClassMethodAst.MethodDirective.Binding.Override()));

        funs = tStringMap.body().fmap(itm ->
            itm instanceof ClassMethodAst.Function fn
                ? ImListLinked.of(fn) : ImListLinked.of()
        );
        countFn = funs.find(c -> c.name().equalsIgnoreCase("count")).get();
        assertTrue(countFn.directives().containsAll(new ClassMethodAst.MethodDirective.Binding.Virtual()));
    }

    @Test
    public void parse_config() {
        var pascal_file = PascalFileAst.parse(textResource("/samples/Config.pas"), "Map.pas");
    }
}
