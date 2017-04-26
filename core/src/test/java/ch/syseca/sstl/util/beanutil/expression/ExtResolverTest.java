package ch.syseca.sstl.util.beanutil.expression;


import static org.junit.Assert.assertEquals;
import org.apache.commons.beanutils.expression.DefaultResolver;
import org.junit.Before;
import org.junit.Test;


public class ExtResolverTest {

    private DefaultResolver defaultResolver;
    private ExtResolver extResolver;


    @Before
    public void init() {
        extResolver = new ExtResolver();
        defaultResolver = new DefaultResolver();
    }


    @Test
    public void testGetSimpleKey() {
        assertEquals("key1", extResolver.getKey("map(key1)"));
        assertEquals("key1", defaultResolver.getKey("map(key1)"));
    }


    @Test
    public void testGetUncuotedKey() {
        assertEquals("[abc]|[123]|[xyz]", extResolver.getKey("map([abc]|[123]|[xyz])"));
        assertEquals("[abc]|[123]|[xyz]", defaultResolver.getKey("map([abc]|[123]|[xyz])"));
    }


    @Test
    public void testGetQuotedKey() {
        assertEquals("(abc)|(123)|(')|[xyz]", extResolver.getKey("map('(abc)|(123)|('')|[xyz]')"));
        assertEquals("(abc)|(123)|(')|[xyz]", extResolver.getKey("map(  '(abc)|(123)|('')|[xyz]')  "));

        // this is the fault corrected by ExtResolver
        assertEquals("(abc", defaultResolver.getKey("map((abc)|(123)|(')|[xyz])"));
    }


    @Test
    public void testNextSimple() {
        assertEquals("a", extResolver.next("a.b"));
        assertEquals("a", defaultResolver.next("a.b"));

        assertEquals("map(key1)", extResolver.next("map(key1)"));
        assertEquals("map(key1)", defaultResolver.next("map(key1)"));
    }


    @Test
    public void testNextUnquoted() {
        assertEquals("map([abc]|[123]|[xyz])", extResolver.next("map([abc]|[123]|[xyz])"));
        assertEquals("map([abc]|[123]|[xyz])", defaultResolver.next("map([abc]|[123]|[xyz])"));
    }


    @Test
    public void testNextQuoted() {
        assertEquals("map('(abc)|(123)|('')|[xyz]')", extResolver.next("map('(abc)|(123)|('')|[xyz]')"));
        assertEquals("map(  '(abc)|(123)|('')|[xyz]'  )", extResolver.next("map(  '(abc)|(123)|('')|[xyz]'  )"));

        // this is the fault corrected by ExtResolver
        assertEquals("map((abc)", defaultResolver.next("map((abc)|(123)|(')|[xyz])"));
    }
}
