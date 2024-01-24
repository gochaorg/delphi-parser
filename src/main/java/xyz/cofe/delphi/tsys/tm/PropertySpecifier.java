package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;

import java.util.Optional;

public sealed interface PropertySpecifier {
    record Read(
        // TODO необходимо resolve на метод/поле
        String name // ref to method
    ) implements PropertySpecifier {
    }

    record Write(
        // TODO необходимо resolve на метод/поле
        String name // ref to method
    ) implements PropertySpecifier {
    }

    record ReadOnly() implements PropertySpecifier {
    }

    record WriteOnly() implements PropertySpecifier {
    }

    record DispID(String expression) implements PropertySpecifier {
    }

    record Stored(String expression) implements PropertySpecifier {
    }

    record Default(Optional<String> expression) implements PropertySpecifier {
    }

    record NoDefault() implements PropertySpecifier {
    }

    record Implements(Type type) implements PropertySpecifier {
    }
}
