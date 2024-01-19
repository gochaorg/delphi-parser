package xyz.cofe.delphi.lexer;

public class LexError extends Error {
    public LexError() {
    }

    public LexError(String message) {
        super(message);
    }

    public LexError(String message, Throwable cause) {
        super(message, cause);
    }

    public LexError(Throwable cause) {
        super(cause);
    }

    public LexError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
