package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.coll.im.ImListLinkedBase;
import xyz.cofe.coll.im.PositionalRead;

import java.util.Arrays;

/**
 * Имя типа
 */
public class TypeName extends ImListLinkedBase<String> implements Comparable<TypeName> {
    public static final TypeName DEFAULT = TypeName.of();

    private TypeName(){
      super(null,null);
    }

    private TypeName(String one, TypeName next){
      super(one,next);
    }

    @Override
    public TypeName empty() {
        return new TypeName();
    }



    @Override
    public TypeName one(String s) {
        if( s==null ) throw new IllegalArgumentException("s==null");
        return new TypeName(s, null);
    }

    @Override
    public TypeName prepend(String s) {
        return new TypeName(s,this);
    }

    public TypeName prepend(PositionalRead<? extends String> values){
        return (TypeName) super.prepend(values);
    }

    public TypeName prepend(TypeName typeName){
        if( typeName==null ) throw new IllegalArgumentException("typeName==null");
        return prepend(ImListLinked.of(typeName));
    }

    public TypeName append(PositionalRead<? extends String> values){
        return (TypeName) super.append(values);
    }

    public TypeName append(TypeName typeName){
        if( typeName==null ) throw new IllegalArgumentException("typeName==null");
        return append(ImListLinked.of(typeName));
    }

    @Override
    public TypeName append(String s) {
        if( s==null ) throw new IllegalArgumentException("s==null");
        if( size()==0 )return new TypeName(s,null);
        if( size()==1 )return new TypeName(get(0).get(), new TypeName(s,null));
        return foldRight( new TypeName(s,null), (acc,it) -> {
            return new TypeName(it,acc);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <A> ImListLinkedBase<A> selfConstructor(A value, ImListLinkedBase<A> next) {
        return new ImListLinked(value,next);
    }

    public static TypeName of(String ... name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        if( name.length==0 )return DEFAULT;
        if( name.length==1 )return new TypeName(name[0],null);
        var tn = new TypeName();
        for( var n : name ){
            tn = tn.append(n);
        }
        return tn;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public static TypeName of(ImList<String> name){
        if( name==null ) throw new IllegalArgumentException("name==null");
        if( name.size()==0 )return DEFAULT;
        if( name.size()==1 )return new TypeName(name.get(0).get(),null);
        var tn = new TypeName();
        for( var i=0; i< name.size(); i++ ){
            var n = name.get(i).get();
            tn = tn.append(n);
        }
        return tn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypeName typeName = (TypeName) o;
        if(size()!=typeName.size())return false;

        for( var i=0; i<size();i++ ){
            var a1 = get(i);
            var a2 = typeName.get(i);
            if( a1.isEmpty() && a2.isEmpty() )continue;
            if( a1.isEmpty() )return false;
            if( a2.isEmpty() )return false;

            var n1 = a1.get();
            var n2 = a2.get();
            if( !n1.equalsIgnoreCase(n2) )return false;
        }

        return true;
    }

    private Integer hashCode;

    @Override
    public int hashCode() {
        if( hashCode!=null )return hashCode;
        var n = new String[size()];
        for( var i=0;i<size();i++ ){
            n[i] = get(i).get().toLowerCase();
        }
        hashCode = Arrays.hashCode(n);
        return hashCode;
    }

    @Override
    public int compareTo(TypeName o) {
        if( o==null )return -1;
        for( var i=0; i<size(); i++ ){
            var n0 = get(i);
            if( n0.isEmpty() )break;

            var n1 = o.get(i);
            if( n1.isEmpty() )break;

            int cmp = n1.flatMap( a -> n1.map(a::compareToIgnoreCase) ).get();
            if( cmp!=0 )return cmp;
        }
        return Integer.compare(size(), o.size());
    }

    @Override
    public String toString() {
        return foldLeft("", (acc,it) -> !acc.isEmpty() ? acc + "." + it : it);
    }
}
