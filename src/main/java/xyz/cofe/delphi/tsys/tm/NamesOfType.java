package xyz.cofe.delphi.tsys.tm;

public interface NamesOfType extends NamedType {
    TypeName[] names();

    @Override
    default TypeName name() {
        var n = names();
        if( n.length==0 )throw new RuntimeException("no names!! bug!!");
        return n[0];
    }
}
