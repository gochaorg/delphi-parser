package xyz.cofe.delphi.tsys.tm.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.delphi.tsys.tm.Type;

import java.io.IOException;

import static xyz.cofe.delphi.tsys.tm.json.jakson.JsonAttr.JSON_TYPE_FIELD;

@SuppressWarnings("unused")
public class UnitTypeRefSer extends StdSerializer<Type.UnitTypeRef> {
    public UnitTypeRefSer() {
        this(null);
    }
    public UnitTypeRefSer(Class<Type.UnitTypeRef> t) {
        super(t);
    }

    @Override
    public void serialize(Type.UnitTypeRef unitTypeRef, JsonGenerator w, SerializerProvider serializerProvider) throws IOException {
        w.writeStartObject();
        w.writeStringField(JSON_TYPE_FIELD,"UnitTypeRef");
        w.writeObjectField("unit", unitTypeRef.unit().name());
        w.writeObjectField("type", unitTypeRef.type());
        w.writeEndObject();
    }
}
