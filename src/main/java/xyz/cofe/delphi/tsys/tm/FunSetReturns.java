package xyz.cofe.delphi.tsys.tm;

/**
 * Установка свойства returns : Type для метода/функции
 */
public sealed interface FunSetReturns permits Function, MethodFunBase, Operator {
    /**
     * Указывает тип результата
     * @param returns Тип результата
     */
    void setReturns(Type returns);

    /**
     * Возвращает тип результата
     * @return Тип результата
     */
    Type getReturns();
}
