package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.tsys.tm.Type;

/**
 * Свойство Parents для указания родительский типов
 */
public sealed interface ParentsProperty extends Type permits ClassType, InterfaceType {
    /**
     * Возвращает список родительских типов
     * @return типы
     */
    ImList<Type> getParents();

    /**
     * Указывает список родительских типов
     * @param parents типы
     */
    void setParents(ImList<Type> parents);
}
