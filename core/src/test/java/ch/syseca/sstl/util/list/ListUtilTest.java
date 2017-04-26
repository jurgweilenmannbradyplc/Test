package ch.syseca.sstl.util.list;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;


/**
 * JUnit-Test fuer {@link ListUtil}.
 */
public class ListUtilTest {

    @Test
    public void testTruncate() {
        final List<Integer> l1 = new ArrayList<Integer>();
        List<Integer> l2;

        l1.add(1);
        l1.add(2);
        l1.add(3);
        l1.add(4);
        l1.add(5);

        l2 = ListUtil.truncate(l1, 3);

        assertEquals(3, l1.size());
        assertEquals(1, l1.get(0));
        assertEquals(2, l1.get(1));
        assertEquals(3, l1.get(2));

        assertEquals(2, l2.size());
        assertEquals(4, l2.get(0));
        assertEquals(5, l2.get(1));

        l2 = ListUtil.truncate(l1, 5);
        assertEquals(3, l1.size());
        assertEquals(0, l2.size());

        l2 = ListUtil.truncate(null, 5);
        assertEquals(0, l2.size());

    }


    @Test
    public void testFill() {
        final List<Integer> list = new ArrayList<Integer>();

        ListUtil.fill(list, 4, l -> 99);
        assertEquals(4, list.size());

    }


    private static Integer sizePlusOne(List<Integer> list) {
        return list.size() + 1;
    }


    @Test
    public void testFill2() {
        final List<Integer> list = new ArrayList<Integer>();

        ListUtil.fill(list, 4, ListUtilTest::sizePlusOne);

        assertEquals(4, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(4, list.get(3));

        ListUtil.fill(list, 3, ListUtilTest::sizePlusOne);
        assertEquals(4, list.size());
    }


    @Test
    public void testFirstLast() {
        final List<Integer> list = new ArrayList<Integer>();

        assertNull(ListUtil.firstOrNull(list));
        assertNull(ListUtil.firstOrNull(list));

        ListUtil.fill(list, 4, ListUtilTest::sizePlusOne);
        assertEquals(1, ListUtil.firstOrNull(list));
        assertEquals(4, ListUtil.lastOrNull(list));
    }
}
