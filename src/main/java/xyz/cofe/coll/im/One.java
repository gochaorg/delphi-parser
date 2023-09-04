package xyz.cofe.coll.im;

/**
 * Создание коллекции с одним элементом
 * @param <SELF> Собственный тип
 * @param <A> элемент списка/коллекции
 */
public interface One<SELF,A> {
    SELF one( A a );
}
