package xyz.cofe.delphi.tsys;

public interface NamesOfType extends NamedType {
    String[] names();

    @Override
    default String name() {
        var n = names();
        if( n.length==0 )throw new RuntimeException("no names!! bug!!");
        return n[0];
    }
}
