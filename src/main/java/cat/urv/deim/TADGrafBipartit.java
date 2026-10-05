package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.VertexNoTrobat;

/**
 * TAD de graf bipartit generic no dirigit.
 *
 * El graf mante dos conjunts de vertexs clarament separats:
 *
 * <ul>
 *   <li>un costat esquerre identificat per claus de tipus {@code K1}</li>
 *   <li>un costat dret identificat per claus de tipus {@code K2}</li>
 * </ul>
 *
 * Les arestes nomes poden unir un vertex del costat esquerre amb un vertex
 * del costat dret. No es permeten, per tant, arestes entre dos vertexs del
 * mateix costat.
 *
 * Cada vertex guarda un valor associat ({@code V1} o {@code V2}) i cada
 * aresta pot guardar un pes o dada associada de tipus {@code E}. Com que el
 * graf es no dirigit, una aresta es visible des dels dos costats, pero compta
 * com una unica relacio.
 *
 * @param <K1> tipus de la clau dels vertexs del costat esquerre
 * @param <V1> tipus del valor emmagatzemat als vertexs del costat esquerre
 * @param <K2> tipus de la clau dels vertexs del costat dret
 * @param <V2> tipus del valor emmagatzemat als vertexs del costat dret
 * @param <E> tipus del valor associat a cada aresta
 */
public interface TADGrafBipartit<K1 extends Comparable<K1>, V1, K2 extends Comparable<K2>, V2, E> {

    /* OPERACIONS SOBRE ELS VERTEXS DEL COSTAT ESQUERRE */

    /**
     * Insereix un vertex al costat esquerre del graf.
     *
     * Si la clau no existia, es crea un nou vertex.
     * Si la clau ja existia, se n'actualitza el valor associat.
     *
     * @param key clau del vertex
     * @param value valor a emmagatzemar al vertex
     */
    void inserirVertexEsquerra(K1 key, V1 value);

    /**
     * Consulta el valor associat a un vertex del costat esquerre.
     *
     * @param key clau del vertex a consultar
     * @return valor associat al vertex indicat
     * @throws VertexNoTrobat si no existeix cap vertex amb aquesta clau
     */
    V1 consultarVertexEsquerra(K1 key) throws VertexNoTrobat;

    /**
     * Esborra un vertex del costat esquerre i totes les arestes incidentes.
     *
     * @param key clau del vertex a esborrar
     * @throws VertexNoTrobat si no existeix cap vertex amb aquesta clau
     */
    void esborrarVertexEsquerra(K1 key) throws VertexNoTrobat;

    /**
     * Retorna el nombre total de vertexs del costat esquerre.
     *
     * @return nombre de vertexs del costat esquerre
     */
    int numVertexEsquerra();

    /**
     * Retorna les claus de tots els vertexs del costat esquerre.
     *
     * @return llista de claus del costat esquerre
     */
    TADLlista<K1> obtenirVertexIDsEsquerra();

    /* OPERACIONS SOBRE ELS VERTEXS DEL COSTAT DRET */

    /**
     * Insereix un vertex al costat dret del graf.
     *
     * Si la clau no existia, es crea un nou vertex.
     * Si la clau ja existia, se n'actualitza el valor associat.
     *
     * @param key clau del vertex
     * @param value valor a emmagatzemar al vertex
     */
    void inserirVertexDret(K2 key, V2 value);

    /**
     * Consulta el valor associat a un vertex del costat dret.
     *
     * @param key clau del vertex a consultar
     * @return valor associat al vertex indicat
     * @throws VertexNoTrobat si no existeix cap vertex amb aquesta clau
     */
    V2 consultarVertexDret(K2 key) throws VertexNoTrobat;

    /**
     * Esborra un vertex del costat dret i totes les arestes incidentes.
     *
     * @param key clau del vertex a esborrar
     * @throws VertexNoTrobat si no existeix cap vertex amb aquesta clau
     */
    void esborrarVertexDret(K2 key) throws VertexNoTrobat;

    /**
     * Retorna el nombre total de vertexs del costat dret.
     *
     * @return nombre de vertexs del costat dret
     */
    int numVertexDret();

    /**
     * Retorna les claus de tots els vertexs del costat dret.
     *
     * @return llista de claus del costat dret
     */
    TADLlista<K2> obtenirVertexIDsDret();

    /* OPERACIONS SOBRE LES ARESTES */

    /**
     * Insereix una aresta entre un vertex del costat esquerre i un del dret.
     *
     * Si l'aresta no existia, es crea una nova relacio.
     * Si l'aresta ja existia, se n'actualitza el pes.
     *
     * @param v1 clau del vertex del costat esquerre
     * @param v2 clau del vertex del costat dret
     * @param pes valor associat a l'aresta
     * @throws VertexNoTrobat si algun dels dos vertexs no existeix
     */
    void inserirAresta(K1 v1, K2 v2, E pes) throws VertexNoTrobat;

    /**
     * Insereix una aresta entre un vertex del costat esquerre i un del dret
     * amb pes {@code null}.
     *
     * @param v1 clau del vertex del costat esquerre
     * @param v2 clau del vertex del costat dret
     * @throws VertexNoTrobat si algun dels dos vertexs no existeix
     */
    void inserirAresta(K1 v1, K2 v2) throws VertexNoTrobat;

    /**
     * Comprova si existeix una aresta entre dos vertexs donats.
     *
     * @param v1 clau del vertex del costat esquerre
     * @param v2 clau del vertex del costat dret
     * @return true si existeix la relacio entre els dos vertexs
     * @throws VertexNoTrobat si algun dels dos vertexs no existeix
     */
    boolean existeixAresta(K1 v1, K2 v2) throws VertexNoTrobat;

    /**
     * Consulta el pes associat a una aresta concreta.
     *
     * @param v1 clau del vertex del costat esquerre
     * @param v2 clau del vertex del costat dret
     * @return pes associat a l'aresta
     * @throws VertexNoTrobat si algun dels dos vertexs no existeix
     * @throws ArestaNoTrobada si la relacio no existeix
     */
    E consultarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada;

    /**
     * Esborra una aresta concreta del graf.
     *
     * @param v1 clau del vertex del costat esquerre
     * @param v2 clau del vertex del costat dret
     * @throws VertexNoTrobat si algun dels dos vertexs no existeix
     * @throws ArestaNoTrobada si la relacio no existeix
     */
    void esborrarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada;

    /**
     * Retorna el nombre total d'arestes del graf.
     *
     * @return nombre total d'arestes
     */
    int numArestes();

    /* METODES AUXILIARS */

    /**
     * Indica si un vertex del costat esquerre esta aillat.
     *
     * Un vertex aillat es un vertex sense cap aresta incident.
     *
     * @param key clau del vertex
     * @return true si el vertex no te adjacents
     * @throws VertexNoTrobat si el vertex no existeix
     */
    boolean vertexEsquerraAillat(K1 key) throws VertexNoTrobat;

    /**
     * Indica si un vertex del costat dret esta aillat.
     *
     * Un vertex aillat es un vertex sense cap aresta incident.
     *
     * @param key clau del vertex
     * @return true si el vertex no te adjacents
     * @throws VertexNoTrobat si el vertex no existeix
     */
    boolean vertexDretAillat(K2 key) throws VertexNoTrobat;

    /**
     * Retorna el nombre de vertexs del costat dret adjacents a un vertex
     * del costat esquerre.
     *
     * @param key clau del vertex del costat esquerre
     * @return grau del vertex al costat esquerre
     * @throws VertexNoTrobat si el vertex no existeix
     */
    int numAdjacentsEsquerra(K1 key) throws VertexNoTrobat;

    /**
     * Retorna el nombre de vertexs del costat esquerre adjacents a un vertex
     * del costat dret.
     *
     * @param key clau del vertex del costat dret
     * @return grau del vertex al costat dret
     * @throws VertexNoTrobat si el vertex no existeix
     */
    int numAdjacentsDret(K2 key) throws VertexNoTrobat;

    /**
     * Retorna les claus dels vertexs del costat dret adjacents a un vertex
     * del costat esquerre.
     *
     * @param key clau del vertex del costat esquerre
     * @return llista de claus adjacents del costat dret
     * @throws VertexNoTrobat si el vertex no existeix
     */
    TADLlista<K2> obtenirAdjacentsEsquerra(K1 key) throws VertexNoTrobat;

    /**
     * Retorna les claus dels vertexs del costat esquerre adjacents a un vertex
     * del costat dret.
     *
     * @param key clau del vertex del costat dret
     * @return llista de claus adjacents del costat esquerre
     * @throws VertexNoTrobat si el vertex no existeix
     */
    TADLlista<K1> obtenirAdjacentsDret(K2 key) throws VertexNoTrobat;

    /**
     * Retorna tots els vertexs del costat esquerre que comparteixen almenys
     * DOS adjacents del costat dret amb el vertex indicat.
     *
     * El vertex passat com a parametre no s'ha d'incloure mai dins del resultat.
     *
     * @param key clau del vertex de referencia del costat esquerre
     * @return llista de vertexs del costat esquerre amb almenys dos adjacents compartits
     * @throws VertexNoTrobat si el vertex de referencia no existeix
     */
    TADLlista<K1> obtenirVertexsEsquerraAmb2AdjacentsCompartits(K1 key) throws VertexNoTrobat;

    /**
     * Retorna la distribucio de graus del costat esquerre.
     *
     * A la posicio {@code i} del resultat hi ha el nombre de vertexs del costat
     * esquerre que tenen grau {@code i}. La longitud del vector es el grau maxim
     * existent mes una unitat. Si no hi ha vertexs al costat esquerre, el metode
     * retorna un array buit.
     *
     * @return distribucio de graus del costat esquerre
     */
    int[] distribucioGrauK1();

    /**
     * Retorna la distribucio de graus del costat dret.
     *
     * A la posicio {@code i} del resultat hi ha el nombre de vertexs del costat
     * dret que tenen grau {@code i}. La longitud del vector es el grau maxim
     * existent mes una unitat. Si no hi ha vertexs al costat dret, el metode
     * retorna un array buit.
     *
     * @return distribucio de graus del costat dret
     */
    int[] distribucioGrauK2();
}
