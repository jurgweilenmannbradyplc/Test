package ch.syseca.sstl.util.text;


import java.util.Map;


public class LinkedPropertiesFormat extends AbstractPropertiesFormat<LinkedProperties> {

    @Override
    protected LinkedProperties createNewPropertySet() {
        return new LinkedProperties();
    }


    @Override
    protected Map<String, String> convertToExportPropertySet(LinkedProperties source) {
        return source;
    }

}
