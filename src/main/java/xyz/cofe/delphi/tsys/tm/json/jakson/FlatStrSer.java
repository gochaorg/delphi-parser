package xyz.cofe.delphi.tsys.tm.json.jakson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import xyz.cofe.text.Template;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class FlatStrSer extends StdSerializer<Object> {
    public FlatStrSer(Class<Object> t) {
        super(t);
    }

    public FlatStrSer(){
        this(null);
    }

    private Optional<Function> compile( Class cls, String code ){
        var methOpt = Arrays.stream(cls.getMethods()).filter(m -> m.getName().equals(code) && m.getReturnType()!=Void.class).findFirst();
        if( methOpt.isPresent() ){
            var meth = methOpt.get();
            Function f = obj -> {
                Object res = null;
                try {
                    res = meth.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return ""+ e;
                }
                return res.toString();
            };
            return Optional.of(f);
        }
        return Optional.empty();
    }

    @Override
    public void serialize(Object o, JsonGenerator w, SerializerProvider serializerProvider) throws IOException {
        if( o==null )return;

        var ann = o.getClass().getAnnotation(FlatStr.class);
        if( ann==null ){
            w.writeString(o.toString());
            return;
        }

        Template tmpl = Template.parse(ann.value());
        Function d = (a) -> {
            return "dummy";
        };

        tmpl = tmpl.binder( code -> {
            return compile(o.getClass(), code.toString());
        });

        var str = tmpl.eval(o);
        w.writeString(str);
    }
}
