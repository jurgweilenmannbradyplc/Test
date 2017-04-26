package ch.syseca.sstl.util.text;


import static org.junit.Assert.assertEquals;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Supplier;
import org.junit.Test;
import lombok.AllArgsConstructor;


public class MessageFormatNATest {

    @AllArgsConstructor
    public static class IntSuplier implements Supplier<Integer> {
        private int v;


        @Override
        public Integer get() {
            return ++v;
        }
    }


    @Test
    public void testIndexed() {
        MessageFormatNA f = new MessageFormatNA("Hej {0}, you are {1} years and {2} month old.");

        f.addIndexedArgument("Joe")
                .addIndexedArgument(99)
                .addIndexedArgument(4);

        assertEquals("Hej Joe, you are 99 years and 4 month old.", f.format());
    }


    @Test
    public void testMaxIndexNameToIndex() {
        MessageFormatNA f = new MessageFormatNA("");

        assertEquals(2, f.evaluateMaxArgIndex("Hej {0}, you are {1} years and {2} month old."));
        assertEquals(6, f.evaluateMaxArgIndex("Hej {6}, you are {1} years and {2} month old."));
        assertEquals(1, f.evaluateMaxArgIndex("Hej {name}, you are {0} years and {1} month old."));
    }


    @Test
    public void testNameToIndex() {
        MessageFormatNA f = new MessageFormatNA("");
        String pattern = "Hej {name} {familyName}, you are {1} years and {2} month old.";
        String expectedText = "Hej {3} {4}, you are {1} years and {2} month old.";

        assertEquals(expectedText, f.namesToIndex(pattern));
        assertEquals(2, f.getNameToIndexMap().size());
        assertEquals(3, f.getNameToIndexMap().get("name"));
        assertEquals(4, f.getNameToIndexMap().get("familyName"));
    }


    @Test
    public void testNamed() {
        MessageFormatNA f = new MessageFormatNA("Hej {name} {familyName}, you are {age} years and {month} month old.");

        f.addNamedArgument("name", "Joe")
                .addNamedArgument("familyName", "Doe")
                .addNamedArgument("age", 99)
                .addNamedArgument("month", 4);

        assertEquals("Hej Joe Doe, you are 99 years and 4 month old.", f.format());
    }


    @Test
    public void testMixed() {
        MessageFormatNA f = new MessageFormatNA("Hej {0} {familyName}, you are {3} years and {month} month old.");

        f.addIndexedArgument("Joe")
                .setIndexedArgument(3, 99)
                .addNamedArgument("familyName", "Doe")
                .addNamedArgument("month", 4);

        assertEquals("Hej Joe Doe, you are 99 years and 4 month old.", f.format());
    }


    @Test
    public void testNumberFormat() {
        MessageFormatNA f = new MessageFormatNA("", new Locale("de", "CH"))
                .addIndexedArgument("Joe")
                .addNamedArgument("year", 99);

        f.applyPattern("Hej {0}, you are {year} years old.");
        assertEquals("Hej Joe, you are 99 years old.", f.format());

        f.applyPattern("Hej {0}, you are {year,number,0000} years old.");
        assertEquals("Hej Joe, you are 0099 years old.", f.format());

        f.applyPattern("Hej {0}, you are {year,number,0000.0} years old.");
        assertEquals("Hej Joe, you are 0099.0 years old.", f.format());

        f.applyPattern("Hej {0}, you are {year,number,0,000.0} years old.");
        assertEquals("Hej Joe, you are 0'099.0 years old.", f.format());
    }


    @Test
    public void testDateFormat() {
        MessageFormatNA f = new MessageFormatNA("", new Locale("de", "CH"))
                .addNamedArgument("name", "Joe")
                .addNamedArgument("birth",
                        Date.from(ZonedDateTime.of(1964, 9, 27, 0, 0, 0, 0, ZoneId.of("Europe/Zurich"))
                                .toInstant()));

        f.applyPattern("Hej {name}, your birthday is at {birth,date,dd.MM.yyyy}.");
        assertEquals("Hej Joe, your birthday is at 27.09.1964.", f.format());

        f.applyPattern("Hej {name}, your birthday is at {birth,date,dd''MM''yyyy}.");
        assertEquals("Hej Joe, your birthday is at 27'09'1964.", f.format());
    }


    @Test
    public void testSpecialChar() {
        MessageFormatNA f = new MessageFormatNA("")
                .addNamedArgument("name", "Joe")
                .addNamedArgument("id", 123)
                .addNamedArgument("familyName", "Doe");

        f.applyPattern("Hej {name} {familyName}, you''r id is {id}.");
        assertEquals("Hej Joe Doe, you'r id is 123.", f.format());

        f.applyPattern("Hej ''{name}'' ''{familyName}'', you''r id is {id,number,0000}.");
        assertEquals("Hej 'Joe' 'Doe', you'r id is 0123.", f.format());
    }


    @Test
    public void testCompatibility() {
        final Date now = new Date();

        assertEquals(
                MessageFormat.format("Hej ''{0}'' ''{1}'', you''r id is {2,number,0000}.",
                        new Object[] { "Joe", "Doe", 123 }),
                new MessageFormatNA("Hej ''{name}'' ''{familyName}'', you''r id is {id,number,0000}.")
                        .addNamedArgument("name", "Joe")
                        .addNamedArgument("id", 123)
                        .addNamedArgument("familyName", "Doe")
                        .format());

        assertEquals(
                MessageFormat.format("{2} {1} {0}",
                        new Object[] { 3, 2, 1 }),
                new MessageFormatNA("{first} {second} {third}")
                        .addNamedArgument("third", 3)
                        .addNamedArgument("second", 2)
                        .addNamedArgument("first", 1)
                        .format());

        assertEquals(
                MessageFormat.format("{2} {1} {0}",
                        new Object[] { 3, 2, 1 }),
                new MessageFormatNA("{2} {1} {0}")
                        .addIndexedArgument(3)
                        .addIndexedArgument(2)
                        .addIndexedArgument(1)
                        .format());

        assertEquals(
                MessageFormat.format("Current time is {0,date,HH''mm}. Time zone is {1}.",
                        new Object[] { now, TimeZone.getDefault().getID() }),
                new MessageFormatNA("Current time is {dateTime,date,HH''mm}. Time zone is {timeZone}.")
                        .addNamedArgument("dateTime", now)
                        .addNamedArgument("timeZone", TimeZone.getDefault().getID())
                        .format());
    }


    @Test
    public void testNoneExistingArg() {
        MessageFormatNA mf;

        mf = new MessageFormatNA("Hej {name}.")
                .addNamedArgument("name", "Joe");
        assertEquals("Hej Joe.", mf.format());

        mf = new MessageFormatNA("Hej {name}.");
        assertEquals("Hej null.", mf.format());

    }


    @Test
    public void testTooManyIndexedArgs() {
        MessageFormatNA mf;

        mf = new MessageFormatNA("Hej {name} {0}.")
                .addIndexedArgument("Doe")
                .addIndexedArgument("not used 1")
                .addIndexedArgument("not used 2")
                .addIndexedArgument("not used 3")
                .addIndexedArgument("not used 4")
                .addNamedArgument("name", "Joe");
        assertEquals("Hej Joe Doe.", mf.format());
    }


    @Test
    public void testSameArgMultipleTimes() {
        MessageFormatNA mf;

        mf = new MessageFormatNA("{v}={v}")
                .addNamedArgumentSuplier("v", new IntSuplier(2));
        assertEquals("3=3", mf.format());
    }
}
