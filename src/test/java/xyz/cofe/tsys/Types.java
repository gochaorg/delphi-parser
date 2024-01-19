package xyz.cofe.tsys;

public class Types {
    public static class VOID implements Type, NamedType {
        private VOID(){}

        @Override
        public boolean isAssignableFrom(Type type) {
            return false;
        }

        @Override
        public String name() {
            return "void";
        }
    }

    public static VOID voidType = new VOID();
}
