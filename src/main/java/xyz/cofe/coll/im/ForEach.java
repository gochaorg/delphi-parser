package xyz.cofe.coll.im;

import java.util.function.Consumer;

public interface ForEach<A> {
    void forEach(Consumer<A> consumer);
}
