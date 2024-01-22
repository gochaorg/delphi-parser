package xyz.cofe.delphi.lexer;

import java.util.*;

public class PreProcState {
    public PreProcState(){
        define("MSWINDOWS");
    }

    public PreProcState(PreProcState sample){
        if( sample==null )throw new IllegalArgumentException("sample==null");
        symbols.putAll(sample.symbols);
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public PreProcState clone(){
        PreProcState clone = (PreProcState) new PreProcState(this);
        return new PreProcState(this);
    }
    private final Map<String,Double> symbols = new HashMap<>();
    public void define(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.put(symbol,1.0);
    }
    public void define(String symbol,double value){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.put(symbol,value);
    }
    public void undef(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        symbols.remove(symbol);
    }
    public boolean isDefined(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        return symbols.containsKey(symbol);
    }
    public Optional<Double> get(String symbol){
        if( symbol==null )throw new IllegalArgumentException("symbol==null");
        var v = symbols.get(symbol);
        return v==null ? Optional.empty() : Optional.of(v);
    }
    public Set<String> toSet(){
        return new TreeSet<>(symbols.keySet());
    }
}
