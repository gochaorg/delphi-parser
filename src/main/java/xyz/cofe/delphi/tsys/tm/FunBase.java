package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.Freeze;

/**
 * Базовый класс для имплементации функции
 */
public sealed class FunBase extends ProcBase implements Freeze, FunSetReturns permits Function {
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
