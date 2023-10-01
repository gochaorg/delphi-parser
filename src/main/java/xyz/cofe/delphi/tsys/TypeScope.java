package xyz.cofe.delphi.tsys;

/**
 * Область видимости типов
 */
public class TypeScope {
    public void add( Type type ){
        if( type==null ) throw new IllegalArgumentException("type==null");
    }
}
