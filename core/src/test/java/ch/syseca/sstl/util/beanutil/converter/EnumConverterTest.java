package ch.syseca.sstl.util.beanutil.converter;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import lombok.Getter;
import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import ch.syseca.sstl.util.beanutil.BeanUtilsBeanSingelton;


/**
 * JUnit Test fuer <code>EnumConverter</code>.
 *
 * @see  EnumConverter
 */
public class EnumConverterTest {

    static enum TestEnum {
        TEST_1, TEST_2
    }

    @Getter
    @Setter
    public static class Foo {
        private TestEnum singleValue;
        private TestEnum[] array;
    }

    private EnumConverter converter;


    @Before
    public void init() {
        converter = new EnumConverter();
    }


    @Test
    public void testDirectConvertSingleValue() throws Exception {
        TestEnum t;

        t = (TestEnum) converter.convert(TestEnum.class, "TEST_2");
        assertEquals(TestEnum.TEST_2, t);
    }


    @Test
    public void testSetSingleValue() throws Exception {
        Foo foo = new Foo();

        BeanUtilsBeanSingelton.get().setProperty(foo, "singleValue", "TEST_1");
        assertEquals(TestEnum.TEST_1, foo.getSingleValue());
    }


    @Test
    public void testConvertArrayToArray() throws Exception {
        TestEnum[] t;

        t = (TestEnum[]) converter.convert(TestEnum[].class, "TEST_2,TEST_1");
        assertEquals(2, t.length);
        assertEquals(TestEnum.TEST_2, t[0]);
        assertEquals(TestEnum.TEST_1, t[1]);
    }


    @Test
    public void testConvertArrayToArrayWithNull() throws Exception {
        TestEnum[] t;

        t = (TestEnum[]) converter.convert(TestEnum[].class, "TEST_2, ,TEST_1");
        assertEquals(3, t.length);
        assertEquals(TestEnum.TEST_2, t[0]);
        assertNull(t[1]);
        assertEquals(TestEnum.TEST_1, t[2]);

        t = (TestEnum[]) converter.convert(TestEnum[].class, "TEST_2,,TEST_1");
        assertEquals(3, t.length);
    }

}
