package xyz.cofe.delphi.ast;

import org.junit.jupiter.api.Test;
import xyz.cofe.coll.im.ImList;
import xyz.cofe.coll.im.ImListLinked;

public class AstTreeTest {
    public static class Node implements AstNode {
        private ImList<Node> children;

        public Node(Node ... children){
            this.children = ImListLinked.of(children);
        }

        @Override
        public ImList<? extends AstNode> nestedAstNodes() {
            return children;
        }

        private String name = null;
        public Node name(String name){
            this.name = name;
            return this;
        }

        @Override
        public String toString() {
            return name!=null ? name : super.toString();
        }
    }

    @Test
    public void test01(){
        var a = new Node().name("a");
        var b = new Node().name("b");
        var c = new Node(a,b).name("c");
        var d = new Node(c).name("d");
        var e = new Node().name("e");
        var f = new Node(e,d).name("f");

        for( var t : f.tree() ){
            System.out.println(t);
        }
    }
}
