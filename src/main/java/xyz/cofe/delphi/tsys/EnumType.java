package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;

import java.util.Optional;

/**
 * Перечисляемый тип
 * @param body содержание
 */
public record EnumType(
    ImList<Entry, ?> body
)
implements SimpleType
{
    /**
     * Элемент перечисляемого типа
     * @param ident идентификатор
     * @param expression значение
     */
    public static record Entry( String ident, Optional<String> expression ) {}
}