package xyz.cofe.delphi.ast;

import xyz.cofe.coll.im.ImList;

import java.lang.reflect.InvocationTargetException;

/**
 * Реализация {@link Commented} через рефлексию, использовать только для Record !
 * @param <SELF> Возвращаемый тип
 */
@SuppressWarnings("unchecked")
public interface WithComments<SELF> extends Commented<SELF> {
    @Override
    default SELF withComments(ImList<Comment> comments){
        if( comments==null ) throw new IllegalArgumentException("comments==null");

        Class<?> klass = this.getClass();
        if( !klass.isRecord() )
            throw new IllegalStateException("BUG!! Using WithComments<SELF> allow only with record (java)");

        var ctrs = klass.getDeclaredConstructors();
        if( ctrs.length!=1 )throw new AstParseError("BUG!! expect one constructor");

        var ctr = ctrs[0];

        var recordComponents = klass.getRecordComponents();
        var values = new Object[recordComponents.length];

        var rcIdx = -1;
        for( var rc: recordComponents ){
            rcIdx += 1;
            try {
                var df = klass.getDeclaredField(rc.getName());
                if( df.trySetAccessible() ){
                    values[rcIdx] = df.get(this);

                    if( df.getName().equals("comments") ){
                        values[rcIdx] = comments;
                    }
                }else{
                    throw new AstParseError("can't reflect access to field "+df);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new AstParseError(e);
            }
        }

        try {
            return (SELF) ctr.newInstance(values);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AstParseError(e);
        }
    }
}
