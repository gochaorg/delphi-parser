package xyz.cofe.coll.im;

public sealed interface SomeNode permits NodeC,
                                         SomeNode.NodeA,
                                         SomeNode.NodeB {
    record NodeA() implements SomeNode {
    }

    record NodeB(String b) implements SomeNode {
    }
}
