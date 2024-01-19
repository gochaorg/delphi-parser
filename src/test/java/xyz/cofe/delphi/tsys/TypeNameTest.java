package xyz.cofe.delphi.tsys;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.cofe.delphi.tsys.tm.TypeName;

public class TypeNameTest {
    @Test
    public void creation(){
        var tn = TypeName.of("a","b","c");
        System.out.println(tn.toString());
        Assertions.assertTrue(tn.toString().equals("a.b.c"));
    }

    @Test
    public void join(){
        var tn = TypeName.of("a","b","c").append(TypeName.of("d","e","f"));
        System.out.println(tn);
        Assertions.assertTrue(tn.toString().equals("a.b.c.d.e.f"));
    }

    @Test
    public void equ(){
        var tn1 = TypeName.of("a","b","c");
        var tn2 = TypeName.of("a","B","c");
        Assertions.assertTrue(tn1.equals(tn2));
    }

    @Test
    public void hash(){
        var tn1 = TypeName.of("a","b","c");
        var tn2 = TypeName.of("a","B","c");
        Assertions.assertTrue(tn1.hashCode()==tn2.hashCode());
    }
}
