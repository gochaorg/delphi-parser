package xyz.cofe.delphi.lexer;

/**
 * Ошибка вычисления
 */
public class EvalError extends Error {
    public EvalError() {
    }

    public EvalError(String message) {
        super(message);
    }

    public EvalError(String message, Throwable cause) {
        super(message, cause);
    }

    public EvalError(Throwable cause) {
        super(cause);
    }
}
