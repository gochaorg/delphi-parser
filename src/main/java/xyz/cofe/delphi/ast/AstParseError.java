package xyz.cofe.delphi.ast;

public class AstParseError extends Error {
    public AstParseError(String message) {
        super(message);
    }

    public AstParseError(String message, Throwable cause) {
        super(message, cause);
    }

    public AstParseError(Throwable cause) {
        super(cause);
    }
}
