package xyz.cofe.coll.im;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("SimplifiableAssertion")
public class ImListLinkedTest {
    @Test
    public void size_test(){
        var lst = ImListLinked.of(1,2,3);
        assertTrue( lst.size()==3 );
    }

    @Test
    public void append_test(){
        var lst = ImListLinked.of(1);
        lst = lst.append(2);
        assertTrue(lst.size() == 2);
        assertTrue(lst.get(0).map(v -> v==1).orElse(false));
        assertTrue(lst.get(1).map(v -> v==2).orElse(false));
    }

    @Test
    public void append_test2(){
        var lst = ImListLinked.of(1);
        lst = lst.append(2);
        lst = lst.append(3);
        assertTrue(lst.size() == 3);
        assertTrue(lst.get(0).map(v -> v==1).orElse(false));
        assertTrue(lst.get(1).map(v -> v==2).orElse(false));
        assertTrue(lst.get(2).map(v -> v==3).orElse(false));
    }

    @Test
    public void append_test3(){
        var lst = ImListLinked.of(1);
        lst = lst.append(2);
        lst = lst.append(3);
        lst = lst.append(4);
        assertTrue(lst.size() == 4);
        assertTrue(lst.get(0).map(v -> v==1).orElse(false));
        assertTrue(lst.get(1).map(v -> v==2).orElse(false));
        assertTrue(lst.get(2).map(v -> v==3).orElse(false));
        assertTrue(lst.get(3).map(v -> v==4).orElse(false));
    }

    @Test
    public void prepend_test(){
        var lst = ImListLinked.of(1);
        lst = lst.prepend(2);
        assertTrue(lst.size() == 2);
        assertTrue(lst.get(0).map(v -> v==2).orElse(false));
        assertTrue(lst.get(1).map(v -> v==1).orElse(false));
    }

    @Test
    public void get_test(){
        var lst = ImListLinked.of(1,2,3);
        assertTrue(lst.get(-1).isEmpty());
        assertTrue(lst.get(3).isEmpty());
        assertTrue(lst.get(0).isPresent());
        assertTrue(lst.get(1).isPresent());
        assertTrue(lst.get(2).isPresent());
    }

    @Test
    public void map_test(){
        var lst = ImListLinked.of(1,2,3);
        var ls2 = lst.map(Object::toString);
        assertTrue(lst.size() == ls2.size());
        assertTrue(ls2.get(0).map(v -> v.equals("1")).orElse(false));
        assertTrue(ls2.get(1).map(v -> v.equals("2")).orElse(false));
        assertTrue(ls2.get(2).map(v -> v.equals("3")).orElse(false));
    }

//    @Test
//    public void fmap_test(){
//        var lst = ImListLinked.of(1,2);
//        var ls2 = lst.fmap(v -> ImListArray.of(v).append(v));
//        assertTrue(ls2.size()==4);
//        assertTrue(ls2.get(0).map(v->v==1).orElse(false));
//        assertTrue(ls2.get(1).map(v->v==1).orElse(false));
//        assertTrue(ls2.get(2).map(v->v==2).orElse(false));
//        assertTrue(ls2.get(3).map(v->v==2).orElse(false));
//    }

    @Test
    public void foldLeft_test(){
        var lst = ImListLinked.of(1,2);
        var res = lst.foldLeft(0, Integer::sum);
        assertTrue(res==3);
    }

    @Test
    public void tail_test(){
        var lst = ImListLinked.of(1,2,3);
        var res = lst.tail();
        assertTrue(res.size()==2);
        assertTrue(res.get(0).map(v->v==2).orElse(false));
        assertTrue(res.get(1).map(v->v==3).orElse(false));
    }

    @Test
    public void filter_test(){
        var lst = ImListLinked.of(1,2,3);
        var res = lst.filter( v -> v > 2 );
        assertTrue(res.size()==1);
        assertTrue(res.get(0).map(v->v==3).orElse(false));
    }

    @Test
    public void rebuild_test(){
        var l1 = ImListLinked.of(1,2,3,4);
        var res = l1.foldRight(ImListLinked.<Integer>of(), ImListLinked::prepend);
        assertTrue(res.size()==4);
        assertTrue(res.get(0).map(v->v==1).orElse(false));
        assertTrue(res.get(1).map(v->v==2).orElse(false));
        assertTrue(res.get(2).map(v->v==3).orElse(false));
        assertTrue(res.get(3).map(v->v==4).orElse(false));
    }
}
