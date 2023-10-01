package xyz.cofe.delphi.tsys;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Область видимости типов
 */
public class TypeScope {
    private Set<Type> types = new HashSet<>();


    /**
     * Добавляем тип в область видимости
     * @param type тип
     */
    public void add( Type type ){
        if( type==null ) throw new IllegalArgumentException("type==null");
    }
}
