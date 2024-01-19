package xyz.cofe.delphi.tsys.tm.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import xyz.cofe.delphi.tsys.tm.ClassType;

import java.util.Optional;
import java.util.WeakHashMap;

public class JsonAttr {
    public static final String JSON_TYPE_FIELD = "@type";
    public static final String RECURSIVE_FIELD = "_recursive";

    public record ClassStack(ClassType classType, Optional<ClassStack> next) {}
    private static final WeakHashMap<JsonGenerator, Object> linked = new WeakHashMap<>();

    public static Optional<ClassType> classType(JsonGenerator jgen) {
        if( jgen==null ) throw new IllegalArgumentException("jgen==null");

        var r = linked.get(jgen);
        if( r instanceof ClassStack cs ) {
            return Optional.of(cs.classType);
        }

        return Optional.empty();
    }

    public static void classType(JsonGenerator jgen, ClassType ct, Runnable code){
        if( jgen==null ) throw new IllegalArgumentException("jgen==null");
        if( ct==null ) throw new IllegalArgumentException("ct==null");
        if( code==null ) throw new IllegalArgumentException("code==null");

        var attr  = linked.get(jgen);
        if( attr instanceof ClassStack cs ){
            var csHead = new ClassStack(ct, Optional.of(cs));
            linked.put(jgen,csHead);
        }else{
            var csHead = new ClassStack(ct,Optional.empty());
            linked.put(jgen,csHead);
        }

        code.run();

        if(attr!=null) linked.put(jgen, attr); else linked.remove(jgen);
    }
}
