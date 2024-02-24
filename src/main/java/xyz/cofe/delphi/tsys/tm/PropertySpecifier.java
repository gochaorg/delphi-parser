package xyz.cofe.delphi.tsys.tm;

import xyz.cofe.coll.im.ImList;
import xyz.cofe.delphi.ast.ExpressionAst;

import java.util.Optional;

public sealed interface PropertySpecifier {
    record Read(
        // TODO необходимо resolve на метод/поле
        ImList<String> name // ref to method
    ) implements PropertySpecifier {
    }

    record Write(
        // TODO необходимо resolve на метод/поле
        ImList<String> name // ref to method
    ) implements PropertySpecifier {
    }

    record ReadOnly() implements PropertySpecifier {
    }

    record WriteOnly() implements PropertySpecifier {
    }

    record DispID(ExpressionAst expression) implements PropertySpecifier {
    }

    record Stored(ExpressionAst expression) implements PropertySpecifier {
    }

    record Default(Optional<ExpressionAst> expression) implements PropertySpecifier {
    }

    record NoDefault() implements PropertySpecifier {
    }

    record Implements(Type type) implements PropertySpecifier {
    }
}
