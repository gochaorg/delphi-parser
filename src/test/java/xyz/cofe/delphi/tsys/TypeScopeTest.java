package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.*;
import xyz.cofe.delphi.lexer.TokenizedFile;
import xyz.cofe.delphi.tsys.tm.*;
import xyz.cofe.io.fs.File;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class TypeScopeTest {
    private static final PascalFileAst sampleFile = PascalFileAst.parse(
        textResource("/samples/Map.pas"),
        "Map.pas",true
    );

    private static final PascalFileAst.Unit sampleUnit =
        (PascalFileAst.Unit)sampleFile;

    @Test
    public void iStringMap(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var itfOpt = ts.getType(TypeName.of("Map","IStringMap"));

        assertTrue(itfOpt.isPresent());
        assertTrue(itfOpt.get() instanceof InterfaceType);
        var itf = (InterfaceType)itfOpt.get();

        itf.getBody().each(System.out::println);
    }

    @Test
    public void tStringMap(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var clsOpt = ts.getType(TypeName.of("Map","TStringMap"));

        assertTrue(clsOpt.isPresent());
        assertTrue(clsOpt.get() instanceof ClassType);
        var cls = (ClassType)clsOpt.get();

        cls.getBody().each(System.out::println);

        var brokens = cls.getBody().fmap(i -> i instanceof ClassItem.Broken b ? ImListLinked.of(b) : ImListLinked.of());
        assertTrue(brokens.size()==0);

        var ctors = cls.getBody().fmap(i -> i instanceof Constructor c ? ImListLinked.of(c) : ImListLinked.of() );

        var copyCtor = ctors.find(c -> c.getName().equalsIgnoreCase("Copy") ).get();
        assertTrue(copyCtor.getArguments().size()==1);
        assertTrue(copyCtor.getArguments().get(0).get().getType() == cls );
    }

    public void TStringPair(){
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var clsOpt = ts.getType(TypeName.of("Map","TStringPair"));
        assertTrue(clsOpt.get() instanceof ClassType);
        var cls = (ClassType)clsOpt.get();

        cls.getBody().each(System.out::println);
    }

    public void funcImport_01(){
        var file = new File("C:\\code\\");
        if( !file.exists() ){
            System.out.println("not found "+file);
            return;
        }

        var fileParsed = PascalFileAst.parse(TokenizedFile.parse(file.path, Charset.forName("windows-1251")), true);
        var unit = (PascalFileAst.Unit)fileParsed;

        var ts = new TypeScope();
        var tunit = ts.add(unit);

        System.out.println("functions: ");
        tunit.getFunctions().each(fun -> {
            System.out.println(fun.getName()+"("+fun.getArguments()+"):"+fun.getReturns());
        });
        System.out.println("procedures: "+tunit.getProcedures());
    }
}
