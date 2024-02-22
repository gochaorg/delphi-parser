package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class CommentInjectorTest {

    @Test
    public void inject(){
        var pascal_file = PascalFileAst.parse(
            textResource("/samples/Map.pas"),
            "Map.pas");

        assertTrue(pascal_file instanceof PascalFileAst.Unit);
        var unit = (PascalFileAst.Unit)pascal_file;

        var injector = new CommentInjector();
        var commented = injector.inject(unit);

        var types = commented.api().declarations()
            .find(dec -> dec instanceof TypeSectionAst)
            .map(d -> (TypeSectionAst)d).get().types();

        var iStringMap = types.fmap( a ->
            a.typeIdent().equals( TypeIdentAst.of("IStringMap") ) &&
                a.typeDecl() instanceof InterfaceTypeAst itf
                ? ImListLinked.of(itf) : ImListLinked.of()
        ).head().get();

        System.out.println(iStringMap);

        var methodsFuns = iStringMap.body().fmap(ClassMethodAst.Function.class);
        assertTrue( methodsFuns.find(m -> m.name().equalsIgnoreCase("count"))
            .map(m -> m.comments().map(Comment::text).foldLeft("",(a, b)->a+b) )
            .map(c -> c.contains("Возвращает кол-во пар")
        ).orElse(false) );

        assertTrue( methodsFuns.find(m -> m.name().equalsIgnoreCase("get"))
            .map(m -> m.comments().map(Comment::text).foldLeft("",(a, b)->a+b) )
            .map(c -> c.contains("возвращает значение по указанному ключу")
            ).orElse(false) );

        assertTrue( methodsFuns.find(m -> m.name().equalsIgnoreCase("exists"))
            .map(m -> m.comments().map(Comment::text).foldLeft("",(a, b)->a+b) )
            .map(c -> c.contains("проверка наличия ключа")
            ).orElse(false) );
    }
}
