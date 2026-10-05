package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;

/**
 * Llista Dinàmica basada en nodes encadenats.
 * A diferència d'un array, els elements no estan junts a la memòria,
 * sinó que cada element (Node) sap on és el següent.
 */
public class LlistaDinamica<E> implements TADLlista<E> {

    private Node<E> head;   // Cap de la llista
    private Node<E> tail;   // Afegim un punter al final de la llista (tail).
                            // Això ens permetrà afegir elements al final immediatament sense haver
                            // de recórrer tota la llista amb un bucle while cada vegada.
    private int numElem;    // Nombre d'elements actuals
    private int max;        // Capacitat maxima de la llista

    /**
     * Classe interna privada.
     * És 'static' perquè un Node no necessita accedir als mètodes de la llista
     * per existir, només guarda la dada i la fletxa (referència) al següent.
     */
    private static class Node<E> {
        E dada;             // El contingut (la cançó)
        Node<E> next;       // El punter cap al següent Node

        Node(E dada) {
            this.dada = dada;
            this.next = null; // Per defecte, un node nou no apunta a ningú
        }
    }

    LlistaDinamica(int max) {
        // Llista buida inicialment
        this.max = max;
        head = null;
        tail = null;
        numElem = 0;
    }

    public LlistaDinamica() {
        this.max = Integer.MAX_VALUE; // Cap límit pràctic
        head = null;
        tail = null;
        numElem = 0;
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
        Node<E> nodeNou = new Node<>(elem);
        if (esBuida()) {
            // Si està buida, el nou node és alhora el cap i la cua
            head = nodeNou;
            tail = nodeNou;
        } else {
            // L'actual últim node (tail) passa a apuntar al nou,
            // i actualitzem tail perquè ara el nou és l'últim.
            tail.next = nodeNou;
            tail = nodeNou;
        }
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
        Node<E> nodeNou = new Node<>(elem);
        if (pos == 0) {
            // Inserir al principi: el nou node apunta a l'antic head, i ell passa a ser el head
            nodeNou.next = head;
            head = nodeNou;
            // Si la llista estava buida, també és la cua
            if (numElem == 0) {
                tail = nodeNou;
            }
        } else {
            // Recorrem fins al node ANTERIOR a la posició on volem inserir (pos - 1)
            Node<E> nodeActual = head;
            for (int i = 0; i < pos-1; i++) {
                nodeActual = nodeActual.next;
            }
            // Lògica d'enllaços (l'ordre importa molt per no perdre la resta de la llista):
            // El nou node s'agafa a la resta de la llista
            nodeNou.next = nodeActual.next;
            // El node anterior s'agafa al nou node
            nodeActual.next = nodeNou;
            // Si hem inserit just al final, actualitzem el tail
            if (nodeNou.next == null) {
                tail = nodeNou;
            }
        }
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
        if (pos == 0) {
            // Per esborrar el primer, simplement fem que el cap sigui el segon
            head = head.next;
            // Si la llista ha quedat buida, el tail també ha de ser null
            if (head == null) {
                tail = null;
            }
        } else {
            // Busquem el node ANTERIOR al que volem esborrar
            Node<E> nodeActual = head;
            for (int i = 0; i < pos-1; i++) {
                nodeActual = nodeActual.next;
            }
            // Ens saltem el node que volem esborrar, apuntant directament al següent del següent
            nodeActual.next = nodeActual.next.next;
            // Si hem esborrat l'últim, l'anterior passa a ser el nou tail
            if (nodeActual.next == null) {
                tail = nodeActual;
            }
        }
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
        Node<E> nodeActual = head;
        for (int i = 0; i < pos; i++) {
            nodeActual = nodeActual.next;
        }
        return nodeActual.dada;
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
        return (numElem() == max);
    }

    /* METODES PER A VALIDAR L'ESTRUCTURA DE LA LLISTA */

    /**
     * Comprova si un element existeix a la llista (utilitza equals()).
     * @param elem element a buscar
     * @return true si l'element es troba a la llista
     */
    public boolean existeix(E elem) {
        boolean trobat = false;
        Node<E> nodeActual = head;

        // Bucle clàssic per recórrer una llista dinàmica sencera:
        // Mentres no arribem al final (null) i no l'haguem trobat...
        while (nodeActual != null && !trobat) {
            if (nodeActual.dada.equals(elem)) {
                trobat = true;
            } else {
                nodeActual = nodeActual.next; // Avancem un pas
            }
        }
        return trobat;
    }

    /**
     * Retorna la posicio de la primera ocurrencia de l'element (utilitza equals()).
     * La primera posicio es la posicio 0.
     * @param elem element a buscar
     * @return posicio de la primera ocurrencia (0-indexed)
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public int posicio(E elem) throws ElementNoTrobat {
        int pos = 0;
        boolean trobat = false;
        Node<E> nodeActual = head;

        while (nodeActual != null && !trobat) {
            if (nodeActual.dada.equals(elem)) {
                trobat = true;
            } else {
                nodeActual = nodeActual.next;
                pos++; // Comptem quants salts portem per saber la posició
            }
        }
        if (!trobat) {
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
        Node<E> nodeActual = head;
        Node<E> nodeAnterior = head;
        boolean trobat = false;

        // Tècnica del doble punter:
        // Portem un node actual i un altre que sempre va un pas per darrere.
        while (nodeActual != null && !trobat) {
            if (nodeActual.dada.equals(elem)) {
                trobat = true;
            } else {
                nodeAnterior = nodeActual;  // Guardem la posició abans d'avançar
                nodeActual = nodeActual.next;   // Avancem l'actual
            }
        }
        if (!trobat) {
            throw new ElementNoTrobat();
        }
        // Si el node trobat era el capçal, no té anterior
        if (nodeActual == head) {
            return null;
        }
        return nodeAnterior.dada;
    }

    /**
     * Retorna l'element seguent a la primera ocurrencia de l'element donat.
     * Si l'element es troba a l'ultima posicio, retorna null.
     * @param elem element de referencia
     * @return l'element seguent, o null si l'element es l'ultim
     * @throws ElementNoTrobat si l'element no es troba a la llista
     */
    public E seguent(E elem) throws ElementNoTrobat {
        Node<E> nodeActual = head;
        boolean trobat = false;

        while (nodeActual != null && !trobat) {
            if (nodeActual.dada.equals(elem)) {
                trobat = true;
            } else {
                nodeActual = nodeActual.next;
            }
        }
        if (!trobat) {
            throw new ElementNoTrobat();
        }
        // Si el node trobat era l'últim (el seu següent no existeix)
        if (nodeActual.next == null) {
            return null;
        }
        return nodeActual.next.dada;
    }
}
