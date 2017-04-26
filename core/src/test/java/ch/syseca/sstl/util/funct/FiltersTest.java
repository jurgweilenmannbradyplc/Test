package ch.syseca.sstl.util.funct;


import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;


public class FiltersTest {

    @Test
    public void testDistinct() {
        List<String> items = Arrays.asList("A", "B", "C", "A", "D", "B", "A");
        List<String> filteredItems;

        filteredItems = items.stream()
                .filter(Filters.distinct())
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("A", "B", "C", "D"), filteredItems);
    }


    @Test
    public void testDistinctWithMapper() {
        List<String> items = new ArrayList<>(Arrays.asList("A1", "B1", "C1", "A2", "D1", "B2", "A3"));
        List<String> filteredItems;

        filteredItems = items.stream()
                .filter(Filters.distinct(o -> o.charAt(0)))
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("A1", "B1", "C1", "D1"), filteredItems);
    }


    @Test
    public void testParallel() {
        List<String> items = Arrays.asList("A", "B", "C", "A", "D", "B", "A");
        List<String> filteredItems;

        filteredItems = items.stream()
                .parallel()
                .filter(Filters.synchronizedFilter(Filters.distinct()))
                .collect(Collectors.toList());
        assertEquals(4, filteredItems.size());
    }
}