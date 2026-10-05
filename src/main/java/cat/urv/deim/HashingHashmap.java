package cat.urv.deim;

import cat.urv.deim.exceptions.ElementNoTrobat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Implementació de la interfície IHashMap utilitzant la classe estàndard java.util.HashMap
 * Funciona com un adaptador (wrapper) per complir amb els requeriments de la interfície.
 * @param <K> Tipus de dada de la clau (ha de ser única)
 * @param <V> Tipus de dada del valor emmagatzemat
 */
public class HashingHashmap<K, V> implements TADHashMap<K, V> {

    // Estructura de dades interna que gestionarà l'emmagatzematge real.
    // Totes les operacions complexes les deleguem a aquest 'map'.
    private final HashMap<K, V> map;

    // El HashMap de Java ja es redimensiona ell sol automàticament.
    // Guardem aquesta variable només per poder complir matemàticament amb els
    // mètodes midaTaula() i factorCarrega() que ens demana la interfície.
    private final int midaInicial;

    public HashingHashmap() {
        this(16); // 16 és la mida per defecte que fa servir Java internament
    }

    /**
     * Constructor de la classe
     * @param mida Mida inicial lògica de la taula de hash
     */
    public HashingHashmap(int mida) {
        this.map = new HashMap<>();
        this.midaInicial = mida;
    }

    @Override
    /**
     * Insereix un parell clau-valor a la taula
     * Si la clau ja existeix, se sobreescriu el valor anterior amb el nou
     */
    public void inserir(K key, V value) {
        map.put(key, value);
    }

    @Override
    /**
     * Retorna el valor associat a una clau específica
     * @throws ElementNoTrobat Si la clau no es troba dins la taula
     */
    public V consultar(K key) throws ElementNoTrobat {
        if (!map.containsKey(key)) {
            throw new ElementNoTrobat();
        }
        return map.get(key);    // 'get' retorna el valor associat a la clau en O(1)
    }

    @Override
    /**
     * Elimina de la taula el parell clau-valor associat a la clau indicada
     * @throws ElementNoTrobat Si la clau no existeix prèviament a la taula
     */
    public void esborrar(K key) throws ElementNoTrobat {
        if (!map.containsKey(key)) {
            throw new ElementNoTrobat();
        }
        map.remove(key);
    }

    @Override
    /**
     * Comprova si una clau determinada existeix dins de l'estructura
     */
    public boolean buscar(K key) {
        return map.containsKey(key);
    }

    @Override
    public boolean esBuida() {
        return map.isEmpty();
    }

    @Override
    public int numElements() {
        return map.size();
    }

    @Override
    /**
     * Retorna un array que conté totes les claus actualment emmagatzemades a la taula.
     * PUNT D'ESTUDI: Java donarà un avís ("Unchecked cast") a la línia de sota.
     * Això passa per culpa del "Type Erasure": Java perd la informació del tipus genèric <K>
     * en temps d'execució i toArray() retorna un Object[]. Nosaltres el forcem a K[].
     */
    public TADLlista<K> obtenirClaus() {
        TADLlista<K> llistaClaus = new LlistaDinamica<>();

        // Passem les claus a una llista estàndard de Java per poder-les ordenar fàcilment
        java.util.List<K> clausAux = new java.util.ArrayList<>(map.keySet());

        // Ordenem les claus (K ha de ser Comparable, com String)
        java.util.Collections.sort((java.util.List<Comparable>) clausAux);

        try {
            for (K clau : clausAux) {
                llistaClaus.inserir(clau);
            }
        } catch (cat.urv.deim.exceptions.LlistaPlena e) {
            // No passarà
        }
        return llistaClaus;
    }

    @Override
    /**
     * Calcula i retorna el factor de càrrega actual de la taula
     * El càlcul es fa basant-se en la mida inicial especificada al constructor.
     * Com que midaInicial i map.size() són Enters, cal fer un càsting a (float)
     * perquè la divisió no perdi els decimals!
     */
    public float factorCarrega() {
        return (float) map.size() / (float) midaInicial;
    }

    @Override
    public int midaTaula() {
        return midaInicial;
    }

    @Override
    /**
     * Retorna un iterador per recórrer els valors de la taula de forma ORDENADA.
     * PUNT D'ESTUDI: Els HashMaps per naturalesa NO tenen cap ordre. Per això estem obligats
     * a extreure'n les dades a una Llista i ordenar-les abans de crear l'iterador.
     */
    public Iterator<V> iterator() {
        // Extracció de tots els valors del mapa directament cap a una llista dinàmica
        List<V> elements = new ArrayList<>(map.values());

        // Ordenació dels elements utilitzant el seu mètode compareTo natural.
        // Aquí s'utilitza una expressió Lambda: (v1, v2) -> ...
        // És una funció anònima que actua com a "Comparator". Compara dos elements de la llista
        // de dos en dos utilitzant el compareTo() que vas programar a la classe Canco.
        try {
            java.util.Comparator<V> comparador = (v1, v2) -> ((Comparable<V>) v1).compareTo(v2);
            elements.sort(comparador);
        } catch (ClassCastException e) {
            // Si V fos un tipus que no implementa Comparable (per exemple, un objecte qualsevol sense ordre lògic), fallaria.
            System.err.println("Els elements no es poden ordenar perque no implementen Comparable.");
        }

        // Retornem l'iterador propi de l'ArrayList, que ja conté els elements ordenats.
        return elements.iterator();
    }
}
