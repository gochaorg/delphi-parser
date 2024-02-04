package xyz.cofe.delphi.tsys.tm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import xyz.cofe.delphi.ast.PascalFileAst;
import xyz.cofe.delphi.ast.TypeDeclAst;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStr;
import xyz.cofe.delphi.tsys.tm.json.jakson.FlatStrSer;
import xyz.cofe.delphi.tsys.tm.json.jakson.UnitTypeRefSer;

/**
 * <a href="https://docwiki.embarcadero.com/RADStudio/Alexandria/en/About_Data_Types_(Delphi)">Некий тип данных</a>
 *
 * <p>
 * Delphi поставляет следующую таксономию типов
 *
 * <ul>
 * <li><b>simple</b> - "Простые" типы, по настоящему не простые
 *      <ul>
 *          <li><b>ordinal</b> - "Последовательные" - выдумка Delphi/Pascal
 *          <ul>
 *              <li><b>integer</b> - Целочисленные - вот они простые
 *              <li><b>character</b> - Символы строки, - они простые
 *              <li><b>Boolean</b> - Булево - они простые
 *              <li><b>enumerated</b> - Перечисляемые - уже не простые
 *              <li><b>subrange</b> - Поддиапазоны - уже не простые
 *          </ul>
 *          <li>real - Числа плавующие - ПРостые
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
 * <li><b>procedural</b>
 * <li>Variant
 * <li>type identifier
 * </ul>
 */
@SuppressWarnings("UnnecessaryModifier")
public interface Type {
    /**
     * Специальные типы для описания типов
     */
    public sealed interface SpecialType extends Type {}

    /**
     * Тип используемый в интерфейсах/класса, указывает на сам класс
     */
    public static final class THIS implements SpecialType {
        public static final THIS instance = new THIS();
        private THIS(){}
    }

    /**
     * Тип для указания в качестве значения по умолчанию для не инициализированного значения
     */
    public static final class NotAssigned implements SpecialType {
        public static final NotAssigned instance = new NotAssigned();
        private NotAssigned() {}
    }

    /**
     * Тип еще не определен, это только ссылка на тип
     */
    public static sealed interface TypeRef extends Type {}

    /**
     * Ссылка на тип из unit файла, требует resolve через uses секцию
     * @param unit файл
     * @param type тип
     */
    @JsonSerialize(using = UnitTypeRefSer.class)
    //@FlatStr("UnitTypeRef unit=${unit} type=${type}")
    public static record UnitTypeRef(PascalFileAst.Unit unit, TypeDeclAst type) implements TypeRef {
        @Override
        public String toString(){
            var sb = new StringBuilder();
            sb.append("UnitTypeRef {");
            sb.append("unit=").append(unit.name())
                .append(" type=")
                .append(type)
                .append("}");
            return sb.toString();
        }
    }

    /**
     * Тип Void / Unit - для данных которые не могут существовать. В частности если деструктор/процедура - функция, тогда результат void
     */
    @JsonSerialize(using = FlatStrSer.class)
    @FlatStr("Void")
    public final class VoidType implements Type {
        private VoidType(){}
        public static final VoidType instance = new VoidType();
        public String toString(){
            return "()";
        }
    }

    // TODO добавить Unresolved Type implements SpecialType
}
