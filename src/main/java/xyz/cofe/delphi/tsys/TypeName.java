package xyz.cofe.delphi.tsys;

import java.util.Arrays;

/**
 * Имя типа
 */
public class TypeName {
    private final String[] name;
    private final int hashCode;

    public TypeName(String ... name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        this.name = Arrays.copyOf(name,name.length);

        var lcName = Arrays.copyOf(name,name.length);
        for( var i=0;i<lcName.length;i++ )lcName[i] = lcName[i].toLowerCase();

        this.hashCode = Arrays.hashCode(lcName);
    }

    @Override
    public String toString() {
        return "TypeName{" +
            "name=" + Arrays.toString(name) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeName typeName = (TypeName) o;
        if(name.length!=typeName.name.length)return false;
        for( var i=0; i<name.length;i++ ){
            if(!name[i].equalsIgnoreCase(typeName.name[i]))return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
