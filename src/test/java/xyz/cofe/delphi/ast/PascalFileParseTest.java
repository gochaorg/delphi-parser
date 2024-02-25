package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.HTree;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.htree.Nest;
import xyz.cofe.delphi.lexer.TokenizedFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class PascalFileParseTest {

    @Test
    public void try_parse() {
        var astRoot = PascalFileAst.parse(textResource("/samples/IntegerList.pas"),"IntegerList.pas");
        System.out.println(astRoot);
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
        var pascal_file =
            PascalFileAst.parse(
                //textResource("/samples/Map.pas"),"Map.pas"
                TokenizedFile.parse(textResource("/samples/Map.pas"), "Map.pas"), true
            );

        assertTrue(pascal_file instanceof UnitAst);
        var unit = (UnitAst) pascal_file;

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
                a.typeDecl() instanceof InterfaceTypeAst itf
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
        assertTrue(destr.directives().fmap(FDirective.Override.class).size()>0);

        funs = tStringMap.body().fmap(itm ->
            itm instanceof ClassMethodAst.Function fn
                ? ImListLinked.of(fn) : ImListLinked.of()
        );
        countFn = funs.find(c -> c.name().equalsIgnoreCase("count")).get();
        assertTrue(countFn.directives().fmap(FDirective.Virtual.class).size()>0);
    }

    @Test
    public void parse_config() {
        var pascal_file = PascalFileAst.parse(textResource("/samples/Config.pas"), "Config.pas");
    }

    @Test
    public void test_tokenizedFile() {
        var pascal_file = PascalFileAst.parse(
            TokenizedFile.parse(textResource("/samples/Config.pas"), "Config.pas"), true);
    }

    private static String take(String str,int max){
        if( str==null )return null;
        if( str.length()>max )return str.substring(0,max);
        return str;
    }

    @Test
    public void htree_visit(){
        var pascal_file =
            PascalFileAst.parse(
                TokenizedFile.parse(textResource("/samples/Map.pas"), "Map.pas"), false
            );

        var commented = HTree.visit(pascal_file, new Object(){
            void enter(ImList<Nest.PathNode> path){
                System.out.print("enter "+">>> ".repeat(path.size())+" ");
                var value = path.head().get().pathValue();
                if( value instanceof String s ){
                    System.out.print(s.replaceAll("[\\n\\r]"," "));
                }else if( value!=null ) {
                    var str = value.toString();
                    str = str.replaceAll("[\\n\\r]"," ");
                    if( str.length()>60 )str = str.substring(0,60);
                    System.out.print(str);
                }
                System.out.println();
            }

            Commented<?> commenting(Commented<?> cmt){
                System.out.println("commented "+take(cmt.toString().replaceAll("[\\n\\r]"," "),50));
                var res = cmt.withComments(ImList.of(new Comment("aaa", new SourcePosition.FileLessPoint(-1,-1))));
                return (Commented<?>)res;
            }
        });

        System.out.println(commented);
    }
}
