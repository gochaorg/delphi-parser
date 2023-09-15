package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class PascalFileParse {
    @Test
    public void parse_test(){
        var url = this.getClass().getResource("/samples/Map.pas");
        try( var strm = url.openStream() ) {
            var source = new String( strm.readAllBytes(), Charset.forName("windows-1251") );
            var res = PascalFile.parse(source, "Map.pas");
            System.out.println(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
