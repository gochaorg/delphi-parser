package xyz.cofe.delphi.docgen;

import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.cofe.delphi.ast.PascalFile;

import java.io.PrintWriter;

public class DocGen {
    private final PrintWriter out;
    private final ObjectMapper om;

    public DocGen(PrintWriter out){
        if( out==null ) throw new IllegalArgumentException("out==null");
        this.out = out;
        this.om = new ObjectMapper();
        this.om.findAndRegisterModules();
    }

    public void write(PascalFile.Unit unit){
        if( unit==null ) throw new IllegalArgumentException("unit==null");

    }
}
