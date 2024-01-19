package xyz.cofe.delphi.tsys.tm;

public final class Function extends ProcBase implements Proc, FunSetReturns {
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
