package xyz.cofe.delphi.tsys;

/**
 * Заморозка объекта от изменений
 */
public interface Freeze {
    /**
     * Возвращает заморожен объект или нет
     * @return true - заморожен
     */
    public boolean isFrozen();

    /**
     * Заморозка
     */
    public void freeze();
}
