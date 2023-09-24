package xyz.cofe.delphi.tsys;

/**
 * <a href="https://docwiki.embarcadero.com/RADStudio/Alexandria/en/About_Data_Types_(Delphi)">Некий тип данных</a>
 *
 * <p/>
 * Delphi поставляет следующую таксономию типов
 *
 * <ul>
 * <li>simple
 *
 *      <ul>
 *          <li>ordinal
 *
 *          <ul>
 *              <li>integer
 *              <li>character
 *              <li>Boolean
 *              <li>enumerated
 *              <li>subrange
 *          </ul>
 *
 *          <li>real
 *      </ul>
 *
 * <li>string
 *
 * <ul>
 *      <li>structured
 *      <li>set
 *      <li>array
 *      <li>record
 *      <li>file
 *      <li>class
 *      <li>class reference
 *      <li>interface
 * </ul>
 *
 * <li>pointer
 * <li>procedural
 * <li>Variant
 * <li>type identifier
 * </ul>
 */
public interface Type {
    /**
     * Специальные типы для описания типов
     */
    public sealed interface SpecialType {}

    /**
     * Тип используемый в интерфейсах/класса, указывает на сам класс
     */
    public static final class THIS implements SpecialType {
        public static final THIS instance = new THIS();
        private THIS(){}
    }

    // TODO добавить Unresolved Type implements SpecialType
}
