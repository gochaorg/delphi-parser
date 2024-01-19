package xyz.cofe.delphi.lexer;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PreProcState {
    public PreProcState(){
        define("MSWINDOWS");
    }

    public PreProcState(PreProcState sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        symbols.addAll(sample.symbols);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public PreProcState clone(){
        PreProcState clone = (PreProcState) new PreProcState(this);
        return new PreProcState(this);
    }
    private final HashSet<String> symbols = new HashSet<>();
    public void define(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.add(symbol);
    }
    public void undef(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.remove(symbol);
    }
    public boolean isDefined(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        return symbols.contains(symbol);
    }
    public Set<String> toSet(){
        return new TreeSet<>(symbols);
    }
}
