package xyz.cofe.tsys;

/**
 * Поле класса/структуры
 */
public interface Field {
    /**
     * Имя поля
     * @return имя
     */
    String getName();

    /**
     * Тип поля
     * @return тип
     */
    Type getType();
}
