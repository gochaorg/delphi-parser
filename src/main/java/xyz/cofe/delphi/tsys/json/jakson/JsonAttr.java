package xyz.cofe.delphi.tsys.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import xyz.cofe.delphi.tsys.ClassType;

import java.util.Optional;

public class JsonAttr {
    public static String JSON_TYPE_FIELD = "jsonKind";

    public enum Key {
        ClassTypeKey
    }

    public static Optional<ClassType> classType(SerializerProvider provider) {
        if( provider==null ) throw new IllegalArgumentException("provider==null");
        var r = provider.getAttribute(Key.ClassTypeKey);
        if( r instanceof ClassType ct )return Optional.of(ct);
        return Optional.empty();
    }

    public static void classType(SerializerProvider provider, ClassType ct){
        if( provider==null ) throw new IllegalArgumentException("provider==null");
        if( ct==null ) throw new IllegalArgumentException("ct==null");
        provider.setAttribute(Key.ClassTypeKey, ct);
    }
}
