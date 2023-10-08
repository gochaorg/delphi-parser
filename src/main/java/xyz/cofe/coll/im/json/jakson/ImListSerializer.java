package xyz.cofe.coll.im.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.coll.im.ImList;

import java.io.IOException;

public class ImListSerializer extends StdSerializer<ImList> {
    public ImListSerializer(){
        super((Class<ImList>) null);
    }

    public ImListSerializer(Class<ImList> t) {
        super(t);
    }

    @Override
    public void serialize(ImList imList, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartArray();
        imList.forEach(obj -> {
            try {
                gen.writeObject(obj);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        gen.writeEndArray();
    }
}
