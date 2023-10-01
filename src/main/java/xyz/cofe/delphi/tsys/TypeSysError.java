package xyz.cofe.delphi.tsys;

public class TypeSysError extends Error {
    public TypeSysError() {
    }

    public TypeSysError(String message) {
        super(message);
    }

    public TypeSysError(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeSysError(Throwable cause) {
        super(cause);
    }

    public static class Frozen extends TypeSysError {
        private static final String DEFAULT_MESSAGE = "Type is frozen";

        public Frozen() {
            super(DEFAULT_MESSAGE);
        }

        public Frozen(String message) {
            super(message);
        }

        public Frozen(String message, Throwable cause) {
            super(message, cause);
        }

        public Frozen(Throwable cause) {
            super(DEFAULT_MESSAGE, cause);
        }
    }
}
