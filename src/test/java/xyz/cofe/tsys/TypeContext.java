package xyz.cofe.tsys;

/**
 * Контекст типов, например область видимости
 */
public interface TypeContext {
    /**
     * Пустой контекст
     * @return Пустой контекст
     */
    static TypeContext empty() {
        return new TypeContext() {
        };
    }
}
