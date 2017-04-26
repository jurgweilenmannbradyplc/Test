package ch.syseca.sstl.util.funct;


import java.util.function.Function;
import java.util.function.Predicate;


public class Filters {

    private Filters() {
    }


    public static <T> DistinctFilter<T> distinct() {
        return new DistinctFilter<>();
    }


    public static <T> DistinctFilter<T> distinct(Function<? super T, Object> mapper) {
        return new DistinctFilter<>(mapper);
    }


    public static <T> SyncronizedFilter<T> synchronizedFilter(Predicate<T> target) {
        return new SyncronizedFilter<>(target);
    }

}
