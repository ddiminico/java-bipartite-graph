package cat.urv.deim;

import java.util.ArrayList;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;

/**
 * Aquesta classe implementa la interfície TADLlista utilitzant internament
 * un ArrayList de la llibreria estàndard de Java.
 * La <E> indica que és una classe GENÈRICA, és a dir, pot guardar qualsevol
 * tipus d'objecte (Canco, Integer, String...) depenent de com s'instanciï.
 * No està lligada a guardar només Canco o només String; s'adaptarà al tipus de dada que se li digui al Main quan es crei.
 */
public class LlistaArrayList<E> implements TADLlista<E> {

    private ArrayList<E> elements;  // L'estructura de dades real on es guarden els elements
    private int max;                // Un ArrayList a Java no té límit real (creix automàticament). Però com que
                                    // l'enunciat ens demana simular un límit per ser compatibles
                                    // amb les altres llistes, guardem aquesta variable per fer el control nosaltres.

    /**
    * Constructor. Inicialitza el límit màxim i crea l'ArrayList buit.
    */
    LlistaArrayList(int max) {
        this.max = max;
        // Instanciem l'ArrayList. No cal posar la <E> a la dreta perquè Java ja ho dedueix de la part esquerra
        elements = new ArrayList<>();
    }

    /* METODES PER A GESTIONAR LA LLISTA */

    /**
     * Insereix un element al final de la llista.
     * @param elem element a inserir
     * @throws LlistaPlena si la llista ha arribat a la capacitat maxima
     */
    public void inserir(E elem) throws LlistaPlena {
        if (esPlena()) {
            throw new LlistaPlena();
        }
        elements.add(elem);  // El mètode .add(elem) de l'ArrayList afegeix l'element al final de la llista
    }

    /**
     * Insereix un element a la posicio indicada (0-indexed).
     * Els elements existents a partir d'aquesta posicio es desplacen una posicio cap a la dreta.
     * @param elem element a inserir
     * @param pos posicio on inserir (0 <= pos <= numElem())
     * @throws LlistaPlena si la llista ha arribat a la capacitat maxima
     */
    public void inserir(E elem, int pos) throws LlistaPlena {
        if (esPlena()) {
            throw new LlistaPlena();
        }
        // Validem que la posició sigui correcta. Permetem pos == numElem() perquè equivaldria a afegir al final de tot.
        if (pos < 0 || pos > numElem()) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }
        // El mètode add(pos, elem) de l'ArrayList ja s'encarrega de desplaçar els elements existents
        // a partir de pos cap a la dreta
        elements.add(pos, elem);
    }

    /**
     * Esborra l'element a la posicio indicada (0-indexed).
     * Els elements posteriors es desplacen una posicio cap a l'esquerra.
     * @param pos posicio de l'element a esborrar (0 <= pos < numElem())
     * @throws LlistaBuida si la llista es buida
     */
    public void esborrar(int pos) throws LlistaBuida {
        if (esBuida()) {
            throw new LlistaBuida();
        }
        // No permetem pos == numElem() perquè l'últim element vàlid es troba a numElem() - 1 (comença a comptar des de 0)
        if (pos < 0 || pos >= numElem()) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }
        // El mètode .remove(posicio) elimina i mou els elements automàticament cap a l'esquerra
        elements.remove(pos);
    }

    /**
     * Retorna l'element a la posicio indicada sense esborrar-lo.
     * @param pos posicio de l'element (0 <= pos < numElem())
     * @return l'element a la posicio indicada
     * @throws LlistaBuida si la llista es buida
     */
    public E consultar(int pos) throws LlistaBuida {
        if (esBuida()) {
            throw new LlistaBuida();
        }
        if (pos < 0 || pos >= numElem()) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }
        // El mètode .get(posicio) retorna l'element.
        // Fem un cast (E) tot i que generalment no caldria, però assegura tipatge.
        return (E) elements.get(pos);
    }

    /**
     * Retorna el nombre d'elements de la llista.
     * @return nombre d'elements
     */
    public int numElem() {
        return elements.size(); // El mètode .size() de l'ArrayList retorna el nombre d'elements actuals a la llista
    }

    /**
     * Comprova si la llista es buida.
     * @return true si no conte cap element
     */
    public boolean esBuida() {
        return (numElem() == 0);
    }

    /**
     * Comprova si la llista es plena (ha arribat a la capacitat maxima).
     * @return true si numElem() == capacitat maxima
     */
    public boolean esPlena() {
        return (numElem() == max);
    }

    /* METODES PER A VALIDAR L'ESTRUCTURA DE LA LLISTA */

    /**
     * Comprova si un element existeix a la llista (utilitza equals()).
     * @param elem element a buscar
     * @return true si l'element es troba a la llista
     */
    public boolean existeix(E elem) {
        return elements.contains(elem); // El mètode .contains(elem) de l'ArrayList retorna true si l'element es troba
                                        // a la llista (utilitza equals() internament)
    }

    /**
     * Retorna la posicio de la primera ocurrencia de l'element (utilitza equals()).
     * La primera posicio es la posicio 0.
     * @param elem element a buscar
     * @return posicio de la primera ocurrencia (0-indexed)
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public int posicio(E elem) throws ElementNoTrobat {
        int pos = elements.indexOf(elem); // El mètode .indexOf(elem) de l'ArrayList retorna la posició de la
                                          // primera ocurrencia de elem, o -1 si no es troba
        if (pos == -1) {  // Si no es troba, indexOf retorna -1. En aquest cas, llancem l'excepció ElementNoTrobat
            throw new ElementNoTrobat();
        }
        return pos;
    }

    /**
     * Retorna l'element anterior a la primera ocurrencia de l'element donat.
     * Si l'element es troba a la posicio 0, retorna null.
     * @param elem element de referencia
     * @return l'element anterior, o null si l'element es el primer
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public E anterior(E elem) throws ElementNoTrobat {
        int pos = posicio(elem);

        // Si és la posició 0 (el primer), no hi ha ningú abans
        if (pos == 0) {
            return null;
        }
        return (E) elements.get(pos-1);
    }
    /**
     * Retorna l'element seguent a la primera ocurrencia de l'element donat.
     * Si l'element es troba a l'ultima posicio, retorna null.
     * @param elem element de referencia
     * @return l'element seguent, o null si l'element es l'ultim
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public E seguent(E elem) throws ElementNoTrobat {
        int pos = posicio(elem);

        // Si és l'última posició (nombre d'elements menys 1), no hi ha ningú després
        if (pos == numElem() - 1) {
            return null;
        }
        return (E) elements.get(pos+1);
    }
}
