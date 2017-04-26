package ch.syseca.sstl.util.text;


import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


public class PropertiesFormat extends AbstractPropertiesFormat<Properties> {

    @Override
    protected Properties createNewPropertySet() {
        return new Properties();
    }


    @Override
    protected Map<String, String> convertToExportPropertySet(Properties source) {
        return new TreeMap(source);
    }

}
