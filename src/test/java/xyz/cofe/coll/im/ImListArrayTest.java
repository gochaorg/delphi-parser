package xyz.cofe.coll.im;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

@SuppressWarnings("SimplifiableAssertion")
public class ImListArrayTest {
    @Test
    public void size_test(){
        var lst = ImListArray.of(1,2,3);
        assertTrue( lst.size()==3 );
    }

    @Test
    public void append_test(){
        var lst = ImListArray.of(1);
        lst = lst.append(2);
        assertTrue(lst.size() == 2);
        assertTrue(lst.get(0).map(v -> v==1).orElse(false));
        assertTrue(lst.get(1).map(v -> v==2).orElse(false));
    }

    @Test
    public void get_test(){
        var lst = ImListArray.of(1,2,3);
        assertTrue(lst.get(-1).isEmpty());
        assertTrue(lst.get(3).isEmpty());
        assertTrue(lst.get(0).isPresent());
        assertTrue(lst.get(1).isPresent());
        assertTrue(lst.get(2).isPresent());
    }

    @Test
    public void map_test(){
        var lst = ImListArray.of(1,2,3);
        var ls2 = lst.map(Object::toString);
        assertTrue(lst.size() == ls2.size());
        assertTrue(ls2.get(0).map(v -> v.equals("1")).orElse(false));
        assertTrue(ls2.get(1).map(v -> v.equals("2")).orElse(false));
        assertTrue(ls2.get(2).map(v -> v.equals("3")).orElse(false));
    }

    @Test
    public void fmap_test(){
        var lst = ImListArray.of(1,2,3);
        var ls2 = lst.fmap(v -> ImList.of(v).append(v));

    }
}
