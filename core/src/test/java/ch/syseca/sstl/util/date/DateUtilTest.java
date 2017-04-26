package ch.syseca.sstl.util.date;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.junit.Ignore;
import org.junit.Test;


/**
 * JUnit Test fuer {@link DateUtil}.
 */
public class DateUtilTest {

    @Test
    public void testEqual() {
        Date d1 = new Date(1111);
        Date d2 = new Date(1111);
        Date d3 = new Date(2222);

        assertTrue(DateUtil.equal(d1, d1));
        assertTrue(DateUtil.equal(d1, d2));
        assertTrue(DateUtil.equal(d2, d1));
        assertTrue(DateUtil.notEqual(d1, d3));
        assertTrue(DateUtil.notEqual(d3, d1));

        assertTrue(DateUtil.equal(d1, new Timestamp(d1.getTime())));
        assertTrue(DateUtil.equal(new Timestamp(d2.getTime()), d2));

    }


    @Test
    public void testDayOfDate() throws Exception {
        Date t;
        Date begin;
        Date end;
        TimeRange r;
        final SimpleDateFormat f = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

        t = f.parse("17.03.2008 12:13:14.983");
        begin = f.parse("17.03.2008 00:00:00.00");
        end = f.parse("18.03.2008 00:00:00.000");

        r = DateUtil.dayOf(t);

        assertEquals(begin, r.getMinDate());
        assertEquals(end, r.getMaxDate());
    }


    @Test
    public void testMin() {
        Date d1 = new Date(System.currentTimeMillis());
        Date d2 = new Date(d1.getTime() + 1);

        assertEquals(d1, DateUtil.min(d1, d2));
        assertEquals(d1, DateUtil.min(d2, d1));
    }


    @Test
    public void testMax() {
        Date d1 = new Date(System.currentTimeMillis());
        Date d2 = new Date(d1.getTime() + 1);

        assertEquals(d2, DateUtil.max(d1, d2));
        assertEquals(d2, DateUtil.max(d2, d1));
    }


    @Test
    public void testAdd() throws Exception {
        final TimeZone tz = TimeZone.getDefault();
        final SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        Date d;

        f.setTimeZone(tz);
        d = f.parse("20090201");
        d = DateUtil.add(d, Calendar.DATE, 1, tz);
        assertEquals("20090202", f.format(d));

        d = DateUtil.add(d, Calendar.DATE, 20, tz);
        assertEquals("20090222", f.format(d));
    }


    @Test
    public void testBefore() {
        long baseTime = 10 * 10 * 1000 + 123;

        assertFalse(DateUtil.after(new Date(baseTime), new Date(baseTime)));
        assertFalse(DateUtil.after(new Date(baseTime), new Timestamp(baseTime)));
        assertFalse(DateUtil.after(new Timestamp(baseTime), new Date(baseTime)));
        assertFalse(DateUtil.after(new Timestamp(baseTime), new Timestamp(baseTime)));

        assertFalse(DateUtil.before(new Date(baseTime), new Date(baseTime)));
        assertFalse(DateUtil.before(new Date(baseTime), new Timestamp(baseTime)));
        assertFalse(DateUtil.before(new Timestamp(baseTime), new Date(baseTime)));
        assertFalse(DateUtil.before(new Timestamp(baseTime), new Timestamp(baseTime)));

        assertTrue(DateUtil.after(new Date(baseTime + 1), new Date(baseTime)));
        assertTrue(DateUtil.after(new Date(baseTime + 1), new Timestamp(baseTime)));
        assertTrue(DateUtil.after(new Timestamp(baseTime + 1), new Date(baseTime)));
        assertTrue(DateUtil.after(new Timestamp(baseTime + 1), new Timestamp(baseTime)));

        assertTrue(DateUtil.after(new Date(baseTime + 1), new Date(baseTime)));
        assertTrue(DateUtil.after(new Date(baseTime + 1), new Timestamp(baseTime)));
        assertTrue(DateUtil.after(new Timestamp(baseTime + 1), new Date(baseTime)));
        assertTrue(DateUtil.after(new Timestamp(baseTime + 1), new Timestamp(baseTime)));
    }


    @Test
    public void testCompare() {
        long baseTime = 10 * 10 * 1000 + 123;

        assertEquals(0, DateUtil.compare(new Date(baseTime), new Date(baseTime)));
        assertEquals(0, DateUtil.compare(new Date(baseTime), new Timestamp(baseTime)));
        assertEquals(0, DateUtil.compare(new Timestamp(baseTime), new Date(baseTime)));
        assertEquals(0, DateUtil.compare(new Timestamp(baseTime), new Timestamp(baseTime)));

        assertEquals(1, DateUtil.compare(new Date(baseTime + 1), new Date(baseTime)));
        assertEquals(1, DateUtil.compare(new Date(baseTime + 1), new Timestamp(baseTime)));
        assertEquals(1, DateUtil.compare(new Timestamp(baseTime + 1), new Date(baseTime)));
        assertEquals(1, DateUtil.compare(new Timestamp(baseTime + 1), new Timestamp(baseTime)));

        assertEquals(-1, DateUtil.compare(new Date(baseTime - 1), new Date(baseTime)));
        assertEquals(-1, DateUtil.compare(new Date(baseTime - 1), new Timestamp(baseTime)));
        assertEquals(-1, DateUtil.compare(new Timestamp(baseTime - 1), new Date(baseTime)));
        assertEquals(-1, DateUtil.compare(new Timestamp(baseTime - 1), new Timestamp(baseTime)));
    }


    @Test
    public void testOlder() {
        Date now = new Date();

        // test minutes
        assertFalse(DateUtil.isOlder(now, 1, Calendar.MINUTE, now));
        assertTrue(DateUtil.isOlder(new Date(now.getTime() - 61 * 1000), 1, Calendar.MINUTE, now));
        assertFalse(DateUtil.isOlder(new Date(now.getTime() - 59 * 1000), 1, Calendar.MINUTE, now));

        // test hours
        assertTrue(DateUtil.isOlder(new Date(now.getTime() - 61 * 60 * 1000), 1, Calendar.HOUR, now));
        assertFalse(DateUtil.isOlder(new Date(now.getTime() - 59 * 60 * 1000), 1, Calendar.HOUR_OF_DAY, now));

        // test days
        assertTrue(DateUtil.isOlder(new Date(now.getTime() - 25 * 60 * 60 * 1000), 1, Calendar.DATE, now));
        assertFalse(DateUtil.isOlder(new Date(now.getTime() - 23 * 60 * 60 * 1000), 1, Calendar.DAY_OF_YEAR, now));
    }


    @Test
    public void testNotOlder() {
        Date now = new Date();

        assertTrue(DateUtil.isNotOlder(now, 1, Calendar.MINUTE));
        assertTrue(DateUtil.isNotOlder(now, 1, Calendar.MINUTE, now));
        assertFalse(DateUtil.isNotOlder(new Date(now.getTime() - 61 * 1000), 1, Calendar.MINUTE, now));
        assertTrue(DateUtil.isNotOlder(new Date(now.getTime() - 59 * 1000), 1, Calendar.MINUTE, now));

    }


    @Test
    public void testToInstant() {
        final long now = System.currentTimeMillis();

        assertEquals(now, DateUtil.toInstant(new Date(now)).toEpochMilli());
        assertNull(DateUtil.toInstant(null));
    }


    @Test
    @Ignore
    public void testOut() {
        final long now = System.currentTimeMillis();
        Date d = new Date(now);
        Instant i = d.toInstant();
        ZonedDateTime zdt = ZonedDateTime.ofInstant(i, TimeZone.getDefault().toZoneId());
        LocalDateTime ldt = LocalDateTime.from(zdt);
        LocalDate ld = LocalDate.from(zdt);
        LocalTime lt = LocalTime.from(zdt);

        System.out.println("Date          : " + d);
        System.out.println("Temporal      : " + i);
        System.out.println("Zoned         : " + zdt);
        System.out.println("LocalDateTime : " + ldt);
        System.out.println("LocalDate     : " + ld);
        System.out.println("LocalTime     : " + lt);
    }

}
