package ch.syseca.sstl.util.serviceloader;


import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Predicate;


public final class ServiceLoaderUtil {

    public static <T> ThreadLocal<ServiceLoader<T>> createThreadLocal(final Class<T> serviceClass) {
        ThreadLocal<ServiceLoader<T>> loader;

        loader = new ThreadLocal<ServiceLoader<T>>() {
            @Override
            protected ServiceLoader<T> initialValue() {
                return ServiceLoader.load(serviceClass);
            }
        };

        return loader;
    }


    public static <T> Collection<T> listServices(ServiceLoader<T> loader) {
        final Collection<T> result = new ArrayList<>();

        loader.forEach(result::add);

        return result;
    }


    public static <T> Collection<T> listServicesSynchronized(ServiceLoader<T> loader) {
        final Collection<T> result;

        synchronized (loader) {
            result = listServices(loader);
        }

        return result;
    }


    public static <T> T getService(ServiceLoader<T> loader, Predicate<T> filter) {
        T result = null;

        for (T s : loader) {
            if (filter.test(s)) {
                result = s;
                break;
            }
        }

        return result;
    }


    public static <T> T getServiceSynchronized(ServiceLoader<T> loader, Predicate<T> filter) {
        T result = null;

        synchronized (loader) {
            result = getService(loader, filter);
        }

        return result;
    }


    public static <T> T getService(ThreadLocal<ServiceLoader<T>> loader, Predicate<T> filter) {
        return getService(loader.get(), filter);
    }

}
