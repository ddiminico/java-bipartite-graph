package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.LlistaPlena;
import cat.urv.deim.exceptions.VertexNoTrobat;
import java.util.Iterator;

public class GrafBipartit<K1 extends Comparable<K1>, V1, K2 extends Comparable<K2>, V2, E>
    implements TADGrafBipartit<K1, V1, K2, V2, E> {
    // Nodes de la multillista

    /**
     * Node per a les arestes. Connecta un element de l'esquerra amb un de la dreta.
     * Al ser una multillista, aquest node estarà en dues llistes enllaçades alhora.
     */
    private class NodeAresta {
        K1 idEsquerra;  // Ex: "joan123"
        K2 idDreta;     // Ex: "Bohemian Rhapsody"
        E pes;          // Ex: 5 (la nota)

        // Punters de la multillista
        NodeAresta seguentEsquerra; // Apunta a la següent aresta de l'usuari (K1)
        NodeAresta seguentDreta;    // Apunta a la següent aresta de la cançó (K2)

        public NodeAresta(K1 idEsquerra, K2 idDreta, E pes) {
            this.idEsquerra = idEsquerra;
            this.idDreta = idDreta;
            this.pes = pes;
            this.seguentEsquerra = null;
            this.seguentDreta = null;
        }
    }

    /**
     * Node per als vèrtexs del costat esquerre (ex: Usuaris).
     */
    private class NodeVertexEsquerra {
        V1 valor;
        NodeAresta primeraAresta; // Capçalera de la llista d'arestes d'aquest vèrtex
        int grau; // Comptador d'adjacents per optimitzar les consultes de grau

        public NodeVertexEsquerra(V1 valor) {
            this.valor = valor;     // Aquí es guarda l'objecte Usuari sencer
            this.primeraAresta = null;  // Cap de la llista enllaçada
            this.grau = 0;  // Guarda quantes cançons ha escoltat algú i l'actualitzem quan afegim/esborrem
        }
    }

    /**
     * Node per als vèrtexs del costat dret (ex: Cançons).
     */
    private class NodeVertexDret {
        V2 valor;
        NodeAresta primeraAresta; // Capçalera de la llista d'arestes d'aquest vèrtex
        int grau; // Comptador d'adjacents

        public NodeVertexDret(V2 valor) {
            this.valor = valor;
            this.primeraAresta = null;
            this.grau = 0;
        }
    }

    // Atributs del graf

    // Utilitzem les taules de hashing per accedir ràpidament als vèrtexs a partir de la seva clau
    // Li dones el nom ("joan123") i automàticament et retorna el NodeVertexEsquerra
    private TADHashMap<K1, NodeVertexEsquerra> vertexsEsquerra;
    private TADHashMap<K2, NodeVertexDret> vertexsDret;
    // Comptador global d'arestes
    private int numArestesTotals;

    // Constructors

    /**
     * Constructor per defecte.
     * S'utilitza a GrafUsuarisCancons. Crida al constructor amb mida
     * passant-li un valor inicial estàndard (per exemple, 11).
     */
    public GrafBipartit() {
        this(11);  // Si algú el crea sense dir la mida, li posem una mida per defecte
                        // (11 és un nombre primer petit, ideal per a hash)
    }

    /**
     * Constructor amb mida inicial.
     * Aquest és el que busquen els tests genèrics (new GrafBipartit<>(100)).
     */
    public GrafBipartit(int mida) {
        if (mida <= 0) {
            throw new IllegalArgumentException("La mida ha de ser positiva");
        }

        // Inicialitzem les taules amb la mida demanada
        this.vertexsEsquerra = new HashingHashmap<>(mida);
        this.vertexsDret = new HashingHashmap<>(mida);
        this.numArestesTotals = 0;
    }

    // Operacions sobre els vèrtexs del costat esquerre (K1, V1)

    @Override
    public void inserirVertexEsquerra(K1 key, V1 value) {
        try {
            // Si ja existeix, actualitzem el valor
            NodeVertexEsquerra node = vertexsEsquerra.consultar(key);
            node.valor = value;
        } catch (ElementNoTrobat e) {
            // Si no existeix, el creem de zero i l'inserim al hash
            NodeVertexEsquerra nouNode = new NodeVertexEsquerra(value);
            vertexsEsquerra.inserir(key, nouNode);
        }
    }

    @Override
    public V1 consultarVertexEsquerra(K1 key) throws VertexNoTrobat {
        try {
            return vertexsEsquerra.consultar(key).valor;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public void esborrarVertexEsquerra(K1 key) throws VertexNoTrobat {
        try {
            // Recollim totes les claus de les cançons (K2) adjacents a aquest usuari
            // Ho fem en una llista separada per evitar trencar els punters mentre iterem i esborrem
            TADLlista<K2> adjacents = obtenirAdjacentsEsquerra(key);

            // Esborrem totes les arestes que connecten aquest usuari
            for (int i = 0; i < adjacents.numElem(); i++) {
                esborrarAresta(key, adjacents.consultar(i));
            }

            // Finalment, un cop desconnectat, l'esborrem del hash
            vertexsEsquerra.esborrar(key);

        } catch (ElementNoTrobat | LlistaBuida | ArestaNoTrobada e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int numVertexEsquerra() {
        return vertexsEsquerra.numElements();
    }

    @Override
    public TADLlista<K1> obtenirVertexIDsEsquerra() {
        return vertexsEsquerra.obtenirClaus();
    }

    // Operacions sobre els vèrtexs del costat dret (K2, V2)

    @Override
    public void inserirVertexDret(K2 key, V2 value) {
        try {
            // Actualitzem si existeix
            NodeVertexDret node = vertexsDret.consultar(key);
            node.valor = value;
        } catch (ElementNoTrobat e) {
            // Creem si no existeix
            NodeVertexDret nouNode = new NodeVertexDret(value);
            vertexsDret.inserir(key, nouNode);
        }
    }

    @Override
    public V2 consultarVertexDret(K2 key) throws VertexNoTrobat {
        try {
            return vertexsDret.consultar(key).valor;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public void esborrarVertexDret(K2 key) throws VertexNoTrobat {
        try {
            // Igual que a l'esquerra: primer recol·lectem adjacents
            TADLlista<K1> adjacents = obtenirAdjacentsDret(key);

            // Esborrem les arestes incidentes
            for (int i = 0; i < adjacents.numElem(); i++) {
                esborrarAresta(adjacents.consultar(i), key);
            }

            // Esborrem el node del hash
            vertexsDret.esborrar(key);

        } catch (ElementNoTrobat | LlistaBuida | ArestaNoTrobada e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int numVertexDret() {
        return vertexsDret.numElements();
    }

    @Override
    public TADLlista<K2> obtenirVertexIDsDret() {
        return vertexsDret.obtenirClaus();
    }

    // Operacions sobre les arestes

    @Override
    public void inserirAresta(K1 v1, K2 v2, E pes) throws VertexNoTrobat {
        try {
            // Busquem els dos vèrtexs
            NodeVertexEsquerra nodeEsquerra = vertexsEsquerra.consultar(v1);
            NodeVertexDret nodeDret = vertexsDret.consultar(v2);

            // Primer comprovem si l'aresta ja existeix per actualitzar el pes
            NodeAresta actual = nodeEsquerra.primeraAresta;
            while (actual != null) {
                if (actual.idDreta.equals(v2)) {
                    actual.pes = pes;   // Hem trobat que ja l'havia valorat. Li canviem la nota i sortim
                    return;
                }
                actual = actual.seguentEsquerra;
            }

            // Si l'usuari no l'havia valorat mai, creem el NodeAresta
            NodeAresta novaAresta = new NodeAresta(v1, v2, pes);

            // Inserció per capçalera a la llista del vèrtex esquerre
            novaAresta.seguentEsquerra = nodeEsquerra.primeraAresta;    // El nou pont apunta a l'antic primer
            nodeEsquerra.primeraAresta = novaAresta;                    // L'usuari ara diu que el seu primer pont és aquest
            nodeEsquerra.grau++; // Augmentem el grau

            // Inserció per capçalera a la llista del vèrtex dret
            novaAresta.seguentDreta = nodeDret.primeraAresta;
            nodeDret.primeraAresta = novaAresta;
            nodeDret.grau++; // Augmentem el grau

            // Incrementem el total d'arestes del graf
            numArestesTotals++;

        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat(); // Si algun dels dos vèrtexs no estava al hash
        }
    }

    @Override
    public void inserirAresta(K1 v1, K2 v2) throws VertexNoTrobat {
        inserirAresta(v1, v2, null); // Reutilitzem el mètode anterior
    }

    @Override
    public boolean existeixAresta(K1 v1, K2 v2) throws VertexNoTrobat {
        try {
            NodeVertexEsquerra nodeEsquerra = vertexsEsquerra.consultar(v1);
            if (!vertexsDret.buscar(v2)) {
                throw new VertexNoTrobat(); // Validem que V2 existeixi
            }

            // Recorrem la llista d'arestes només del costat esquerre
            NodeAresta actual = nodeEsquerra.primeraAresta;
            while (actual != null) {
                if (actual.idDreta.equals(v2)) {
                    return true;
                }
                actual = actual.seguentEsquerra;
            }
            return false;

        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public E consultarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada {
        try {
            NodeVertexEsquerra nodeEsquerra = vertexsEsquerra.consultar(v1);
            if (!vertexsDret.buscar(v2)) {
                throw new VertexNoTrobat();
            }

            NodeAresta actual = nodeEsquerra.primeraAresta;
            while (actual != null) {
                if (actual.idDreta.equals(v2)) {
                    return actual.pes;
                }
                actual = actual.seguentEsquerra;
            }
            throw new ArestaNoTrobada();

        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public void esborrarAresta(K1 v1, K2 v2) throws VertexNoTrobat, ArestaNoTrobada {
        try {
            NodeVertexEsquerra nodeEsquerra = vertexsEsquerra.consultar(v1);
            NodeVertexDret nodeDret = vertexsDret.consultar(v2);

            // Desenganxar l'aresta de la llista del vèrtex esquerre
            NodeAresta actualEsq = nodeEsquerra.primeraAresta;
            NodeAresta anteriorEsq = null;
            boolean trobada = false;

            while (actualEsq != null) {
                if (actualEsq.idDreta.equals(v2)) {
                    trobada = true;
                    // Era el primer de la llista
                    if (anteriorEsq == null) {
                        nodeEsquerra.primeraAresta = actualEsq.seguentEsquerra;
                    } else {    // Estava al mig o al final de la llista
                        anteriorEsq.seguentEsquerra = actualEsq.seguentEsquerra;
                    }
                    nodeEsquerra.grau--;
                    break;
                }
                // Avancem els dos punters
                anteriorEsq = actualEsq;
                actualEsq = actualEsq.seguentEsquerra;
            }

            if (!trobada) throw new ArestaNoTrobada();

            // Desenganxar l'aresta de la llista del vèrtex dret
            NodeAresta actualDret = nodeDret.primeraAresta;
            NodeAresta anteriorDret = null;

            while (actualDret != null) {
                if (actualDret.idEsquerra.equals(v1)) {
                    // Saltem el node
                    if (anteriorDret == null) {
                        nodeDret.primeraAresta = actualDret.seguentDreta;
                    } else {
                        anteriorDret.seguentDreta = actualDret.seguentDreta;
                    }
                    nodeDret.grau--;
                    break;
                }
                anteriorDret = actualDret;
                actualDret = actualDret.seguentDreta;
            }

            numArestesTotals--;

        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int numArestes() {
        return this.numArestesTotals;
    }

    // Consultes d'adjacència i grau

    @Override
    public boolean vertexEsquerraAillat(K1 key) throws VertexNoTrobat {
        try {
            return vertexsEsquerra.consultar(key).primeraAresta == null;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public boolean vertexDretAillat(K2 key) throws VertexNoTrobat {
        try {
            return vertexsDret.consultar(key).primeraAresta == null;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int numAdjacentsEsquerra(K1 key) throws VertexNoTrobat {
        try {
            return vertexsEsquerra.consultar(key).grau;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int numAdjacentsDret(K2 key) throws VertexNoTrobat {
        try {
            return vertexsDret.consultar(key).grau;
        } catch (ElementNoTrobat e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public TADLlista<K2> obtenirAdjacentsEsquerra(K1 key) throws VertexNoTrobat {
        try {
            NodeVertexEsquerra node = vertexsEsquerra.consultar(key);
            java.util.List<K2> aux = new java.util.ArrayList<>();

            NodeAresta actual = node.primeraAresta;
            while (actual != null) {
                aux.add(actual.idDreta);
                actual = actual.seguentEsquerra;
            }

            // Ordenem alfabèticament (K2 és Comparable)
            java.util.Collections.sort((java.util.List<Comparable>) aux);

            TADLlista<K2> llista = new LlistaDinamica<>();
            for (K2 id : aux) llista.inserir(id);
            return llista;
        } catch (ElementNoTrobat | LlistaPlena e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public TADLlista<K1> obtenirAdjacentsDret(K2 key) throws VertexNoTrobat {
        try {
            NodeVertexDret node = vertexsDret.consultar(key);
            java.util.List<K1> aux = new java.util.ArrayList<>();

            NodeAresta actual = node.primeraAresta;
            while (actual != null) {
                aux.add(actual.idEsquerra);
                actual = actual.seguentDreta;
            }

            java.util.Collections.sort((java.util.List<Comparable>) aux);

            TADLlista<K1> llista = new LlistaDinamica<>();
            for (K1 id : aux) llista.inserir(id);
            return llista;
        } catch (ElementNoTrobat | LlistaPlena e) {
            throw new VertexNoTrobat();
        }
    }

    // Consultes específiques del problema

    @Override
    public TADLlista<K1> obtenirVertexsEsquerraAmb2AdjacentsCompartits(K1 key) throws VertexNoTrobat {
        try {
            // Llista final a retornar
            TADLlista<K1> resultat = new LlistaDinamica<>();

            // Usem un HashMap intern com a "comptador" d'ocurrències d'altres usuaris (K1 -> freqüència)
            TADHashMap<K1, Integer> comptador = new HashingHashmap<>();

            // Obtenim totes les cançons (K2) valorades pel nostre usuari de referència
            TADLlista<K2> misCancons = obtenirAdjacentsEsquerra(key);

            // Iterem sobre cada cançó
            for (int i = 0; i < misCancons.numElem(); i++) {
                K2 canco = misCancons.consultar(i);

                // Mirem quins altres usuaris han valorat aquesta cançó
                TADLlista<K1> altresUsuaris = obtenirAdjacentsDret(canco);

                for (int j = 0; j < altresUsuaris.numElem(); j++) {
                    K1 usuariCandidat = altresUsuaris.consultar(j);

                    // Ens ignorem a nosaltres mateixos
                    if (!usuariCandidat.equals(key)) {  // No em compto a mi mateix
                        int count = 0;
                        if (comptador.buscar(usuariCandidat)) {
                            count = comptador.consultar(usuariCandidat);    // Quantes vegades l'havia vist ja?
                        }
                        comptador.inserir(usuariCandidat, count + 1);
                    }
                }
            }

            // Repassem el comptador. Qui tingui >= 2, va al resultat
            TADLlista<K1> clausComptades = comptador.obtenirClaus();
            for (int i = 0; i < clausComptades.numElem(); i++) {
                K1 candidat = clausComptades.consultar(i);
                if (comptador.consultar(candidat) >= 2) {
                    resultat.inserir(candidat);
                }
            }

            return resultat;

        } catch (ElementNoTrobat | LlistaBuida | LlistaPlena e) {
            throw new VertexNoTrobat();
        }
    }

    @Override
    public int[] distribucioGrauK1() {
        if (vertexsEsquerra.esBuida()) return new int[0];

        // Primer trobem el grau màxim per saber la mida de l'array
        int grauMax = 0;
        Iterator<NodeVertexEsquerra> itMax = vertexsEsquerra.iterator();
        while (itMax.hasNext()) {
            int grauActual = itMax.next().grau;
            if (grauActual > grauMax) {
                grauMax = grauActual;
            }
        }

        // Creem l'array i el farcim
        int[] distribucio = new int[grauMax + 1];   // +1 perquè existeix el grau 0 (usuaris aïllats)
        Iterator<NodeVertexEsquerra> itFill = vertexsEsquerra.iterator();
        while (itFill.hasNext()) {
            distribucio[itFill.next().grau]++;  // Sumem 1 usuari a la casella corresponent al seu grau
        }

        return distribucio;
    }

    @Override
    public int[] distribucioGrauK2() {
        if (vertexsDret.esBuida()) return new int[0];

        int grauMax = 0;
        Iterator<NodeVertexDret> itMax = vertexsDret.iterator();
        while (itMax.hasNext()) {
            int grauActual = itMax.next().grau;
            if (grauActual > grauMax) {
                grauMax = grauActual;
            }
        }

        int[] distribucio = new int[grauMax + 1];
        Iterator<NodeVertexDret> itFill = vertexsDret.iterator();
        while (itFill.hasNext()) {
            distribucio[itFill.next().grau]++;
        }

        return distribucio;
    }
}
