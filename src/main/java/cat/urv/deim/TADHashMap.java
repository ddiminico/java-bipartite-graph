package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;
import java.util.Iterator;

/**
 * TAD de taula de hashing generica que associa claus amb valors.
 *
 * Aquesta estructura permet inserir, consultar, esborrar i buscar elements
 * a partir d'una clau. Cada clau identifica com a maxim un sol valor.
 * Si s'insereix una clau que ja existia, el valor anterior queda
 * sobreescrit pel nou.
 *
 * La interficie no fixa cap estrategia concreta d'implementacio: pot estar
 * basada en encadenament, adrecament obert o qualsevol altra tecnica
 * equivalent. A mes, la taula ofereix metodes per obtenir informacio
 * estructural com el factor de carrega, la mida de la part estatica i el
 * conjunt de claus emmagatzemades.
 *
 * @param <K> tipus de la clau
 * @param <V> tipus del valor associat a cada clau
 */
public interface TADHashMap<K, V> extends Iterable<V> {

    /**
     * Insereix una parella clau-valor a la taula.
     *
     * Si la clau no existia, s'afegeix una nova entrada.
     * Si la clau ja existia, el valor associat s'actualitza.
     *
     * @param key clau de l'element
     * @param value valor associat a la clau
     */
    public void inserir(K key, V value);

    /**
     * Consulta el valor associat a una clau.
     *
     * @param key clau que es vol consultar
     * @return valor associat a la clau indicada
     * @throws ElementNoTrobat si la clau no es troba a la taula
     */
    public V consultar(K key) throws ElementNoTrobat;

    /**
     * Esborra l'entrada associada a una clau.
     *
     * @param key clau de l'element a esborrar
     * @throws ElementNoTrobat si la clau no es troba a la taula
     */
    public void esborrar(K key) throws ElementNoTrobat;

    /**
     * Comprova si una clau existeix a la taula.
     *
     * @param key clau a buscar
     * @return true si la clau es troba a la taula, false altrament
     */
    public boolean buscar(K key);

    /**
     * Comprova si la taula no conte cap element.
     *
     * @return true si no hi ha cap entrada emmagatzemada
     */
    public boolean esBuida();

    /**
     * Retorna el nombre total d'entrades emmagatzemades.
     *
     * @return nombre d'elements de la taula
     */
    public int numElements();

    /**
     * Retorna totes les claus emmagatzemades a la taula.
     *
     * Les claus s'han de retornar dins d'una llista. L'ordre concret pot
     * dependre de la implementacio, tot i que en aquest projecte habitualment
     * es treballa amb les claus ordenades.
     *
     * @return llista amb totes les claus presents a la taula
     */
    public TADLlista<K> obtenirClaus();

    /**
     * Retorna el factor de carrega actual de la taula.
     *
     * Habitualment aquest valor correspon al quocient entre el nombre
     * d'elements emmagatzemats i la mida de la part estatica de la taula.
     *
     * @return factor de carrega actual
     */
    public float factorCarrega();

    /**
     * Retorna la mida actual de la part estatica de la taula.
     *
     * En una implementacio basada en cubetes o posicions de hash, aquest
     * metode sol correspondre al nombre de caselles disponibles.
     *
     * @return mida actual de la taula
     */
    public int midaTaula();

    /**
     * Retorna un iterador pels valors emmagatzemats a la taula.
     *
     * @return iterador de valors
     */
    public Iterator<V> iterator();
}
