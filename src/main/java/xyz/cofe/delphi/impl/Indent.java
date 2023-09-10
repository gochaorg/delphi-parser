package xyz.cofe.delphi.impl;

public class Indent {
    public static String indent(String indent,String source){
        if(indent==null)throw new IllegalArgumentException("indent==null");
        if(source==null)throw new IllegalArgumentException("source==null");
        var lines = source.split("\r?\n");
        var sb = new StringBuilder();
        for( var line : lines ){
            sb.append(indent).append(line).append("\n");
        }
        return sb.toString();
    }
}
