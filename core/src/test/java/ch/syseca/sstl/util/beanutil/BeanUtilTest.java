package ch.syseca.sstl.util.beanutil;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * JUnit Test fuer Klasse {@link BeanUtil}.
 */
public class BeanUtilTest {

    /**
     * Test.
     */
    @Test
    public void testCreateBean() {
        Foo f;

        f = BeanUtil.createBean("ch.syseca.sstl.util.beanutil.Foo", "value=12; timezone=Europe/Zurich; fooType=TYPE_1");
        assertEquals(12, f.getValue());
        assertEquals("Europe/Zurich", f.getTimezone().getID());
        assertEquals(Foo.FooType.TYPE_1, f.getFooType());
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testGetSetterName() {
        assertEquals("setName", BeanUtil.getSetterName("name"));
        assertEquals("setX", BeanUtil.getSetterName("x"));
        assertEquals("setY", BeanUtil.getSetterName("Y"));
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testHasMethod() {
        assertTrue(BeanUtil.hasMethod(Foo.class, "setValue"));
        assertTrue(BeanUtil.hasMethod(Foo.class, "getTimezone"));
        assertFalse(BeanUtil.hasMethod(Foo.class, "doesNotExist"));
        assertFalse(BeanUtil.hasMethod(Foo.class, "isPrivate"));
    }
}
