package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.delphi.tsys.Freeze;

/**
 * Базовый класс для имплементации функции
 */
public sealed class MethodFunBase extends MethodBase implements Freeze, FunSetReturns permits MethodFunction {
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
