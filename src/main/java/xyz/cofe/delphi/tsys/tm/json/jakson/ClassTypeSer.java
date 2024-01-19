package xyz.cofe.delphi.tsys.tm.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.delphi.tsys.tm.ClassType;

import java.io.IOError;
import java.io.IOException;

import static xyz.cofe.delphi.tsys.tm.json.jakson.JsonAttr.JSON_TYPE_FIELD;
import static xyz.cofe.delphi.tsys.tm.json.jakson.JsonAttr.RECURSIVE_FIELD;

public class ClassTypeSer extends StdSerializer<ClassType> {
    public ClassTypeSer(Class<ClassType> t) {
        super(t);
    }
    public ClassTypeSer(){
        this(null);
    }

    @Override
    public void serialize(ClassType classType, JsonGenerator w, SerializerProvider serializerProvider) throws IOException {
        var ctOpt = JsonAttr.classType(w);
        if( ctOpt.isPresent() ){
            var ct = ctOpt.get();
            if( ct==classType ){
                w.writeStartObject();
                w.writeStringField(JSON_TYPE_FIELD, classType instanceof ClassType.Named ? "ClassType.Named" : "ClassType");
                w.writeBooleanField(RECURSIVE_FIELD, true);
                w.writeEndObject();
                return;
            }
        }

        JsonAttr.classType(w, classType, ()->{
            try {
                w.writeStartObject();

                w.writeStringField(JSON_TYPE_FIELD, classType instanceof ClassType.Named ? "ClassType.Named" : "ClassType");
                if( classType instanceof ClassType.Named n ){
                    w.writeObjectField("name", n.name());
                }

                w.writeObjectField( "parents", classType.getParents() );
                w.writeObjectField( "comments", classType.getComments() );
                w.writeObjectField( "specifier", classType.getSpecifier() );
                classType.getDeclaration().ifPresent( decl -> {
                    try {
                        w.writeObjectField("declaration", classType.getDeclaration());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                w.writeObjectField("body", classType.getBody() );

                w.writeEndObject();
            } catch (IOException e) {
                throw new IOError(e);
            }
        });
    }
}
