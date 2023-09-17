package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

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
    public void parse_test(){
        System.out.println(
            PascalFile.parse(
                textResource("/samples/Map.pas"),
                "Map.pas"));
    }


    @Test
    public void parse_test_ic(){
        System.out.println(
            PascalFile.parse(
                textResource("/samples/MapIC.pas"),
                "MapIC.pas"));
    }
}
