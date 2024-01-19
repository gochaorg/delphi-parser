package xyz.cofe.delphi.tsys.tm.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.delphi.tsys.tm.Type;

import java.io.IOException;

@SuppressWarnings("unused")
public class VoidTypeSer extends StdSerializer<Type.VoidType> {
    public VoidTypeSer() {
        this(null);
    }
    public VoidTypeSer(Class<Type.VoidType> t) {
        super(t);
    }

    @Override
    public void serialize(Type.VoidType voidType, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        gen.writeString("void");
    }
}
