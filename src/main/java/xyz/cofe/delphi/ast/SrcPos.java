package xyz.cofe.delphi.ast;

/**
 * Свойство - расположение в исходниках
 */
public interface SrcPos {
    /**
     * Возвращает расположение в исходниках
     *
     * @return расположение
     */
    SourcePosition position();
}
