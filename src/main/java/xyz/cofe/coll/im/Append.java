package xyz.cofe.coll.im;

/**
 * Возможность добавления в конец списка
 * @param <SELF> Собственный тип
 * @param <A> элемент списка
 */
public interface Append<SELF,A> {
    /**
     * Добавить в конец списка
     * @param a добавляемый элемент
     * @return список с добавленным элементом
     */
    SELF append(A a);
}
