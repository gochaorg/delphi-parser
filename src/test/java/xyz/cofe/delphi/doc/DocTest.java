package xyz.cofe.delphi.doc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.PascalFileAst;
import xyz.cofe.delphi.tsys.*;
import xyz.cofe.delphi.tsys.tm.ClassType;
import xyz.cofe.delphi.tsys.tm.Constructor;
import xyz.cofe.delphi.tsys.tm.InterfaceType;
import xyz.cofe.delphi.tsys.tm.TypeName;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static xyz.cofe.delphi.TextResource.textResource;

public class DocTest {
    private static PascalFileAst sampleFile = PascalFileAst.parse(
        textResource("/samples/Map.pas"),
        "Map.pas",true
    );

    private static PascalFileAst.Unit sampleUnit =
        (PascalFileAst.Unit)sampleFile;

    @Test
    public void classJsonOut() {
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var clsOpt = ts.getType(TypeName.of("Map","TStringMap"));

        assertTrue(clsOpt.isPresent());
        assertTrue(clsOpt.get() instanceof ClassType);
        var cls = (ClassType)clsOpt.get();

        var ctors = cls.getBody().fmap(i -> i instanceof Constructor c ? ImListLinked.of(c) : ImListLinked.of() );
        var copyCtor = ctors.find(c -> c.getName().equalsIgnoreCase("Copy") ).get();

        var om = new ObjectMapper();
        om.findAndRegisterModules();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            System.out.println(om.writeValueAsString(cls));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void interfaceJsonOut() {
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var clsOpt = ts.getType(TypeName.of("Map","IStringMap"));

        assertTrue(clsOpt.isPresent());
        assertTrue(clsOpt.get() instanceof InterfaceType);
        var itf = (InterfaceType)clsOpt.get();

        var om = new ObjectMapper();
        om.findAndRegisterModules();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            System.out.println(om.writeValueAsString(itf));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void unitOut() {
        var ts = new TypeScope();
        ts.add(sampleUnit);

        var unit = ts.unitMap().get(TypeName.of("Map")).get(0);

        var om = new ObjectMapper();
        om.findAndRegisterModules();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        try {
            System.out.println(om.writeValueAsString(unit));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
