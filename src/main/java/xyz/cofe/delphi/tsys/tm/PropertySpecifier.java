package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;

import java.util.Optional;

public sealed interface PropertySpecifier {
    record Read(
        ImList<String> name, // ref to method
        Optional<String> expression
    ) implements PropertySpecifier {
    }

    record Write(
        ImList<String> name, // ref to method
        Optional<String> expression
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

    record DefaultExp(String expression) implements PropertySpecifier {
    }

    record Default() implements PropertySpecifier {
    }

    record NoDefault() implements PropertySpecifier {
    }

    record Implements(Type type) implements PropertySpecifier {
    }
}
