package xyz.cofe.delphi.tsys;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.ast.SourcePosition;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс
 */
public record InterfaceType(
    //ImList<Generic,?> params,
    ImList<Type,?> parents,
    ImList<Method,?> methods,
    Optional<String> guid,
    Optional<SourcePosition> declaration,
    Optional<SourcePosition> implementation
) {}
