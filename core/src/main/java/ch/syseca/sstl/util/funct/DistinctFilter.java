package ch.syseca.sstl.util.funct;


import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This filter does NOT work for parallel streams!
 * @see Filters#synchronizedFilter(Predicate)
 */
public class DistinctFilter<T> implements Predicate<T> {
    private final Set<Object> objects = new HashSet<>();
    private final Function<? super T, Object> mapper;


    public DistinctFilter(Function<? super T, Object> mapper) {
        this.mapper = mapper;
    }


    public DistinctFilter<T> withMapper(Function<? super T, Object> mapper) {
        return new DistinctFilter<>(mapper);
    }


    public DistinctFilter() {
        mapper = o -> o;
    }


    @Override
    public boolean test(T o) {
        return objects.add(mapper.apply(o));
    }

}
