package xyz.cofe.coll.im;

/**
 * Создание коллекции с одним элементом
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface One<SELF,A> {
    /**
     * Создание коллекции с один элементом
     * @param a элемент коллекции
     * @return коллекция с одним элементом
     */
    SELF one( A a );
}
