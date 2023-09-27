package xyz.cofe.delphi;

import xyz.cofe.delphi.ast.PascalFileParseTest;

import java.io.IOException;
import java.nio.charset.Charset;

public class TextResource {
    public static String textResource(String name){
        var url = PascalFileParseTest.class.getResource(name);
        try( var strm = url.openStream() ) {
            var source = new String( strm.readAllBytes(), Charset.forName("windows-1251") );
            return source;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
