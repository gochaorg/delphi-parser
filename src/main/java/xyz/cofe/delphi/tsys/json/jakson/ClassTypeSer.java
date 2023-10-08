package xyz.cofe.delphi.tsys.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.delphi.tsys.ClassType;

import java.io.IOException;

import static xyz.cofe.delphi.tsys.json.jakson.JsonAttr.JSON_TYPE_FIELD;

public class ClassTypeSer extends StdSerializer<ClassType> {
    public ClassTypeSer(Class<ClassType> t) {
        super(t);
    }
    public ClassTypeSer(){
        this(null);
    }

    @Override
    public void serialize(ClassType classType, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        JsonAttr.classType(serializerProvider, classType);

        gen.writeStartObject();

        gen.writeStringField(JSON_TYPE_FIELD, classType instanceof ClassType.Named ? "ClassType.Named" : "ClassType");
        if( classType instanceof ClassType.Named n ){
            gen.writeObjectField("name", n.name());
        }

        //gen.writeObjectField( "parents", classType.getParents() );
        //gen.writeObjectField( "comments", classType.getComments() );
        //gen.writeObjectField( "specifier", classType.getSpecifier() );

        gen.writeEndObject();
    }
}
