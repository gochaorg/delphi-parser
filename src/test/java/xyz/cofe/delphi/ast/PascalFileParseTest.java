package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PascalFileParseTest {
    public static String textResource(String name){
        var url = PascalFileParseTest.class.getResource(name);
        try( var strm = url.openStream() ) {
            var source = new String( strm.readAllBytes(), Charset.forName("windows-1251") );
            return source;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void try_parse(){
        System.out.println(
            PascalFile.parse(
                textResource("/samples/Map.pas"),
                "Map.pas"));
    }


    @Test
    public void try_parse_ignore_case(){
        System.out.println(
            PascalFile.parse(
                textResource("/samples/MapIC.pas"),
                "MapIC.pas"));
    }

    @Test
    public void parse_map(){
        var pascal_file = PascalFile.parse(
            textResource("/samples/Map.pas"),
            "Map.pas");

        assertTrue(pascal_file instanceof PascalFile.Unit);
        var unit = (PascalFile.Unit)pascal_file;

        assertTrue(
            unit.api().uses().containsAll(
                Namespace.of("SysUtils"),
                Namespace.of("Variants"),
                Namespace.of("Classes")
            )
        );

        var types = unit.api().declarations()
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

//        var ctrs = iStringMap.body().fmap(itm ->
//            itm instanceof ClassMethod.Constructor ctr
//            ? ImListLinked.of(ctr) : ImListLinked.of()
//        );

//        var createCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Create")).get();
//        var copyCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Copy")).get();
//        var copyiCtr = ctrs.find(c -> c.name().equalsIgnoreCase("Copyi")).get();
//
//        var destrs = iStringMap.body().fmap(itm ->
//            itm instanceof ClassMethod.Destructor ctr
//                ? ImListLinked.of(ctr) : ImListLinked.of()
//        );
//        var destr = destrs.find(c -> c.name().equalsIgnoreCase("Destroy")).get();
    }
}
