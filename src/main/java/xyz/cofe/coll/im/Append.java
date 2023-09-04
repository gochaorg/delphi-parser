package xyz.cofe.coll.im;

/**
 * Возможность добавления в конец списка
 * @param <SELF> Собственный тип
 * @param <A> элемент списка
 */
public interface Append<SELF,A> {
    SELF append(A a);
}
