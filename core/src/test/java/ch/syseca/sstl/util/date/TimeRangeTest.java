package ch.syseca.sstl.util.date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * JUnit Test f&uuml;r <code>ch.syseca.sstl.util.date.TimeRange</code>.
 */
public class TimeRangeTest {

    private static TimeRange timeRange;


    @BeforeClass
    public static void init() {
        TimeRangeTest.timeRange = new TimeRange(100, 200);
    }


    @Test
    public void testContainsTime01() {
        assertTrue(TimeRangeTest.timeRange.containsTime(100));
    }


    @Test
    public void testContainsTime02() {
        assertTrue(TimeRangeTest.timeRange.containsTime(199));
    }


    @Test
    public void testNotContainsTime01() {
        assertFalse(TimeRangeTest.timeRange.containsTime(300));
    }


    @Test
    public void testNotContainsTime02() {
        assertFalse(TimeRangeTest.timeRange.containsTime(50));
    }


    @Test
    public void testNotContainsTime03() {
        assertFalse(TimeRangeTest.timeRange.containsTime(200));
    }


    @Test
    public void testContainsRange01() {
        assertTrue(TimeRangeTest.timeRange.containsRange(new TimeRange(100, 110)));
    }


    @Test
    public void testContainsRange02() {
        assertTrue(TimeRangeTest.timeRange.containsRange(new TimeRange(100, 199)));
    }


    @Test
    public void testNotContainsRange01() {
        assertFalse(TimeRangeTest.timeRange.containsRange(new TimeRange(80, 110)));
    }


    @Test
    public void testNotContainsRange02() {
        assertFalse(TimeRangeTest.timeRange.containsRange(new TimeRange(30, 201)));
    }


    @Test
    public void testNotContainsRange03() {
        assertFalse(TimeRangeTest.timeRange.containsRange(new TimeRange(30, 400)));
    }


    @Test
    public void testMillis() throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        TimeRange r = new TimeRange(df.parse("01.01.2000 13:00"), df.parse("01.01.2000 13:01"));

        assertEquals(60L * 1000L, r.millis());
    }


    @Test
    public void testTimestamp() {
        TimeRange t1 = new TimeRange(new Date(100 * 1000), new Date(200 * 1000));
        TimeRange t2 = new TimeRange(new Timestamp(100 * 1000), new Timestamp(200 * 1000));

        assertTrue(t1.getMinTime() == t2.getMinTime());
        assertTrue(t1.getBeginTime().equals(t2.getBeginTime()));
        assertFalse(t2.getBeginTime().equals(t1.getBeginTime()));
        assertEquals(t1, t2);

    }


    @Test
    public void testIsOverlapping() {
        final TimeRange t1 = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC));
        TimeRange t2;

        assertTrue(t1.isOverlappedBy(t1));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(t1.isOverlappedBy(t2));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 9, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC));
        assertFalse(t1.isOverlappedBy(t2));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 12, 0, 0, 0, 0, ZoneOffset.UTC));
        assertFalse(t1.isOverlappedBy(t2));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 2, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 10, 4, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(t1.isOverlappedBy(t2));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 9, 1, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 10, 1, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(t1.isOverlappedBy(t2));

        t2 = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 23, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 23, 0, 0, 0, ZoneOffset.UTC));
        assertTrue(t1.isOverlappedBy(t2));
    }


    @Test
    public void testIntersection() {
        final TimeRange base = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC));

        assertEquals(base, base.intersect(base));
        assertEquals(base, new TimeRange(base.getBeginTime(), base.getEndTime()));

        assertEquals(
                new TimeRange(
                        ZonedDateTime.of(2015, 1, 10, 6, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 10, 16, 0, 0, 0, ZoneOffset.UTC)),
                base.intersect(new TimeRange(
                        ZonedDateTime.of(2015, 1, 10, 6, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 10, 16, 0, 0, 0, ZoneOffset.UTC))));

        assertEquals(
                new TimeRange(
                        ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 10, 2, 0, 0, 0, ZoneOffset.UTC)),
                base.intersect(new TimeRange(
                        ZonedDateTime.of(2015, 1, 9, 2, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 10, 2, 0, 0, 0, ZoneOffset.UTC))));

        assertEquals(
                new TimeRange(
                        ZonedDateTime.of(2015, 1, 10, 20, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC)),
                base.intersect(new TimeRange(
                        ZonedDateTime.of(2015, 1, 10, 20, 0, 0, 0, ZoneOffset.UTC),
                        ZonedDateTime.of(2015, 1, 11, 20, 0, 0, 0, ZoneOffset.UTC))));
    }


    @Test(expected = IllegalArgumentException.class)
    public void testNoIntersection() {
        new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC))
                        .intersect(new TimeRange(
                                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC),
                                ZonedDateTime.of(2015, 1, 12, 0, 0, 0, 0, ZoneOffset.UTC)));
    }


    @Test
    public void testToString() {
        TimeRange tz = new TimeRange(
                ZonedDateTime.of(2015, 1, 10, 0, 0, 0, 0, ZoneOffset.UTC),
                ZonedDateTime.of(2015, 1, 11, 0, 0, 0, 0, ZoneOffset.UTC));

        assertEquals("2015-01-10T00:00:00Z/2015-01-11T00:00:00Z", tz.toString());
    }
}
