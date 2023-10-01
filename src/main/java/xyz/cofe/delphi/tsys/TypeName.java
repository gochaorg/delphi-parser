package xyz.cofe.delphi.tsys;

import java.util.Arrays;

/**
 * Имя типа
 */
public class TypeName {
    private final String[] name;

    public TypeName(String ... name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        this.name = Arrays.copyOf(name,name.length);
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
        return Arrays.equals(name, typeName.name);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(name);
    }
}
