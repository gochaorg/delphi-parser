package xyz.cofe.tsys;

/**
 * некий тип данных
 */
public interface Type {
    /**
     * Проверка, что к значению данного типа можно присвоит значение указанного типа
     * @param type присвояемый тип данных
     * @return true - присвоение допустимо
     */
    boolean isAssignableFrom( Type type );
}
