package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.Optional;

public record Method(
    String name,
    ImList<Param,?> params,
    Optional<Type> returns,
    ImList<MethodDirective,?> directives,
    Visibility visibility
) {
    public static record Param(
        String name,
        Type type,
        Optional<String> expression,
        Direction direction,
        Visibility visibility
    ) {}

    /**
     * Направление параметра
     */
    public static enum Direction {
        /**
         * Для входных параметров
         */
        Input,

        /**
         * Для входных параметров, переменные данного типа нельзя модифицировать
         */
        ConstInput, // TODO пометить тип как не модифицируемый

        /**
         * Параметр для выходных данных
         */
        Output,

        /**
         * Как входной, так и выходной
         */
        InputOutput
    }
}
