package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;

/**
 * Llista Estàtica basada en un Array clàssic.
 * Les dades es guarden de forma CONTIGUA a la memòria.
 * L'inconvenient principal és que la mida és fixa (es defineix al crear-la)
 * i inserir/esborrar al mig obliga a desplaçar molts elements.
 */
public class LlistaEstatica<E> implements TADLlista<E> {
    private E[] elements;   // Array on guardarem les dades
    private int numElem;    // Comptador de quants elements reals tenim guardats


    /**
     * Constructor.
     * A Java no es pot fer "new E[numElem]" directament per culpa de
     * la "Type Erasure" (esborrat de tipus a temps de compilació).
     * La solució és crear un array d'Object i fer un "cast" a (E[]).
     * Això genera un "Warning" al compilar, però és la manera correcta de fer-ho.
     */
    LlistaEstatica(int numElem) {
        this.elements = (E[]) new Object[numElem];
        this.numElem = 0;  // Llista buida inicialment
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
        // numElem sempre apunta a la primera casella buida al final
        elements[numElem] = elem;
        numElem++;
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
        if (pos < 0 || pos > numElem) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }

        // Per desplaçar cap a la dreta sense sobreescriure dades,
        // el bucle s'ha de fer endarrere (de dreta a esquerra).
        // Comencem a l'últim forat buit i anem copiant el del costat.
        for (int i = numElem; i > pos; i--) {
            elements[i] = elements[i-1];
        }
        // Un cop fet el forat a pos, hi posem l'element
        elements[pos] = elem;
        numElem++;
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
        if (pos < 0 || pos >= numElem) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }

        // Per desplaçar cap a l'esquerra, el bucle es fa endavant
        // Tapem la posició esborrada amb el del costat dret
        for (int i = pos; i < numElem-1; i++) {
            elements[i] = elements[i+1];
        }
        // Esborrem l'última referència duplicada per alliberar memòria i ajudar al Garbage Collector de Java
        elements[numElem-1] = null;
        numElem--;
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
        if (pos < 0 || pos >= numElem) {
            throw new IndexOutOfBoundsException("Error: posicio fora de rang");
        }
        return (E) elements[pos];
    }

    /**
     * Retorna el nombre d'elements de la llista.
     * @return nombre d'elements
     */
    public int numElem() {
        return numElem;
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
        // Està plena quan el número d'elements ocupats iguala la capacitat total de l'array
        return (numElem() == elements.length);
    }

    /* METODES PER A VALIDAR L'ESTRUCTURA DE LA LLISTA */

    /**
     * Comprova si un element existeix a la llista (utilitza equals()).
     * @param elem element a buscar
     * @return true si l'element es troba a la llista
     */
    public boolean existeix(E elem) {
        // Recorrem només fins a numElem, no fins a elements.length
        // ja que o volem comprovar caselles buides (nulls)
        for (int i = 0; i < numElem; i++) {
            if (elements[i].equals(elem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna la posicio de la primera ocurrencia de l'element (utilitza equals()).
     * La primera posicio es la posicio 0.
     * @param elem element a buscar
     * @return posicio de la primera ocurrencia (0-indexed)
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public int posicio(E elem) throws ElementNoTrobat {
        for (int i = 0; i < numElem; i++) {
            // Quan el trobem, fem un return immediat.
            // Això fa que sigui més eficient perquè no seguim buscant innecessàriament
            if (elements[i].equals(elem)) {
                return i;
            }
        }
        // Si el bucle acaba i no hem fet cap return, és que no hi és
        throw new ElementNoTrobat();
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
        if (pos == 0) {
            return null; // // Si és el de la posició 0, no hi ha res abans
        }
        return (E) elements[pos-1];
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
        if (pos == numElem-1) {     // Si és l'últim element actualment guardat, no hi ha res després
            return null;
        }
        return (E) elements[pos+1];
    }
}
