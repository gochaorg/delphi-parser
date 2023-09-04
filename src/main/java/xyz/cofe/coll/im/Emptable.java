package xyz.cofe.coll.im;

/**
 * Возможность создать пустую коллекцию
 * @param <SELF> Собственный тип
 */
public interface Emptable<SELF> {
    /**
     * Создает пустую коллекцию
     * @return пустая коллекция
     */
    SELF empty();
}
