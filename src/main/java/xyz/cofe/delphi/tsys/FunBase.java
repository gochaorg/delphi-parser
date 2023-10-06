package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;
import xyz.cofe.delphi.ast.Comment;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.Optional;

/**
 * Базовый класс для имплементации функции
 */
public abstract class FunBase extends ProcBase implements Freeze {
    //region returns : Type - тип результата
    /**
     * Указывает тип результата
     * @param returns Тип результата
     */
    @Override
    public void setReturns(Type returns) {
        if( returns==null ) throw new IllegalArgumentException("returns==null");
        super.setReturns(returns);
    }
    //endregion
}
