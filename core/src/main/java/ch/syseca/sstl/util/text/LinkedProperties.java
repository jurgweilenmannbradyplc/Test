package ch.syseca.sstl.util.text;


import java.util.LinkedHashMap;
import java.util.Map;


public class LinkedProperties extends LinkedHashMap<String, String> {

    public LinkedProperties() {
        super();
    }


    public LinkedProperties(int initialCapacity, float loadFactor, boolean accessOrder) {
        super(initialCapacity, loadFactor, accessOrder);
    }


    public LinkedProperties(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }


    public LinkedProperties(int initialCapacity) {
        super(initialCapacity);
    }


    public LinkedProperties(Map<? extends String, ? extends String> m) {
        super(m);
    }


    public void setProperty(String key, String value) {
        put(key, value);
    }


    public String getProperty(String key) {
        return get(key);
    }


    public String getProperty(String key, String defaultValue) {
        return containsKey(key) ? get(key) : defaultValue;
    }

}
