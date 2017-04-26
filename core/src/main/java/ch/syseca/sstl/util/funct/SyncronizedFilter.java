package ch.syseca.sstl.util.funct;


import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class SyncronizedFilter<T> implements Predicate<T> {

    @Getter
    private final Predicate<T> target;


    @Override
    public boolean test(T t) {
        synchronized (this) {
            return target.test(t);
        }
    }

}
