package ch.syseca.sstl.util.obfuscation;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Marker-Interface fuer Zelix-Klassmaster, um bestimmte Klassen, Methoden oder Felder von der Obfuskierung auszuschliessen.
 */
@Retention(RetentionPolicy.CLASS)
public @interface ExcludeFromObfuscation {

}
