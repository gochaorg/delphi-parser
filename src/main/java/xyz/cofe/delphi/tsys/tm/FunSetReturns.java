package xyz.cofe.delphi.tsys.tm;

/**
 * Установка свойства returns : Type для метода/функции
 */
public sealed interface FunSetReturns permits FunBase, Operator {
    /**
     * Указывает тип результата
     * @param returns Тип результата
     */
    public void setReturns(Type returns);

    /**
     * Возвращает тип результата
     * @return Тип результата
     */
    public Type getReturns();
}
