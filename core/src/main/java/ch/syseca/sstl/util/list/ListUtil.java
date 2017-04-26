package ch.syseca.sstl.util.list;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


/**
 * Hilfsfunktionen auf {@link List}-Klassen.
 */
public final class ListUtil {

    /**
     * Klasse wird nicht instanziert.
     */
    private ListUtil() {
    }


    /**
     * Liefert den ersten Eintrag in der Liste. Ist die Liste leer, wird <code>null</code> zur&uuml;ckgeliefert.
     * 
     * @param <T> Typ.
     * @param list Liste, aus der der letzte Eintrag entfernt werden soll.
     * 
     * @return Entfernter Eintrag.
     */
    public static <T> T firstOrNull(List<T> list) {
        return list.isEmpty() ? null : list.get(0);
    }


    /**
     * Liefert den letzten Eintrag in der Liste. Ist die Liste leer, wird <code>null</code> zur&uuml;ckgeliefert.
     * 
     * @param <T> Typ.
     * @param list Liste, aus der der letzte Eintrag entfernt werden soll.
     * 
     * @return Entfernter Eintrag.
     */
    public static <T> T lastOrNull(List<T> list) {
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }


    /**
     * Entfernt den letzten Eintrag in der Liste. Ist die Liste leer, wird <code>null</code> zur&uuml;ckgeliefert.
     * 
     * @param <T> Typ.
     * @param list Liste, aus der der letzte Eintrag entfernt werden soll.
     * 
     * @return Entfernter Eintrag.
     */
    public static <T> T removeLast(List<T> list) {
        T result = null;

        if (!list.isEmpty()) {
            result = list.remove(list.size() - 1);
        }

        return result;
    }


    /**
     * Loescht alle Elemente ab Position <code>newSize</code>. Nach Aufruf dieser Methode hat die Liste maximal <code>
     * newSize</code> Eintraege.
     * 
     * @param <T> Typ.
     * @param list Liste, die verkuerzt werden soll.
     * @param newSize Maximale Anzahl der Eintraege.
     * 
     * @return Liste mit den entfernten Eintraegen. Gibt eine leere Liste zurueck, wenn keine Eintraege entfernt wurden.
     */
    public static <T> List<T> truncate(List<T> list, int newSize) {
        List<T> result;
        int itemsToRemove = list != null
                ? list.size() - newSize
                : 0;

        if (itemsToRemove > 0) {
            result = new ArrayList<T>(itemsToRemove);

            while (itemsToRemove > 0) {
                result.add(removeLast(list));
                itemsToRemove--;
            }
            Collections.reverse(result);
        } else {
            result = new ArrayList<T>(0);
        }

        return result;
    }


    /**
     * Fuegt <code>null</code>s am Ende der Liste ein, bis die gewuenschte Laenge erreicht ist.
     * 
     * @param <T> Typ.
     * @param list Liste.
     * @param newSize Gewuenschte Laenge.
     * @param generator Generator, der die Objekte erzeugt.
     */
    public static <T> void fill(List<T> list, int newSize) {
        fill(list, newSize, (l) -> null);
    }


    /**
     * Fuegt Objekte am Ende der Liste ein, bis die gewuenschte Laenge erreicht ist.
     * 
     * @param <T> Typ.
     * @param list Liste.
     * @param newSize Gewuenschte Laenge.
     * @param generator Generator, der die Objekte erzeugt.
     */
    public static <T> void fill(List<T> list, int newSize, Function<List<T>, T> generator) {
        while (list.size() < newSize) {
            list.add(generator.apply(list));
        }
    }


    /**
     * Verlaengert oder verkuerzt die Liste, bis die gewuenschte Laenge erreicht ist.
     * 
     * @param <T> Typ.
     * @param list Liste.
     * @param newSize Gewuenschte Laenge.
     * @param generator Generator, der die Objekte erzeugt, falls die Liste verlaengert werden muss.
     * 
     * @see ListUtil#fill(List, int, ItemGenerator)
     * @see ListUtil#truncate(List, int)
     */
    public static <T> void resize(List<T> list, int newSize, Function<List<T>, T> generator) {
        if (list.size() > newSize) {
            truncate(list, newSize);
        } else if (list.size() < newSize) {
            fill(list, newSize, generator);
        }
    }

}
