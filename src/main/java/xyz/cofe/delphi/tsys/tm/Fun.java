package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;

/**
 * Некая функция
 */
public sealed interface Fun permits Constructor, Destructor, Function, Operator, Procedure {
    /**
     * Возвращает аргументы функции
     * @return аргументы функции
     */
    ImList<Argument> getArguments();

    /**
     * Указывает аргументы функции
     * @param arguments аргументы функции
     */
    void setArguments(ImList<Argument> arguments);
}
