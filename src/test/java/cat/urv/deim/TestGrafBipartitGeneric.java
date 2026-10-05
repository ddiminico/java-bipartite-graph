package cat.urv.deim;

import org.junit.jupiter.api.Test;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.VertexNoTrobat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGrafBipartitGeneric {

    // -------------------------------------------------------------------------
    // constructor
    // -------------------------------------------------------------------------

    @Test
    public void testConstructorMidaZeroLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class, () -> new GrafBipartit<>(0));
    }

    @Test
    public void testConstructorMidaNegativaLlancaExcepcio() {
        assertThrows(IllegalArgumentException.class, () -> new GrafBipartit<>(-1));
    }

    // -------------------------------------------------------------------------
    // inserirVertexEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testInserirVertexEsquerraNou() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");

        assertEquals(1, graf.numVertexEsquerra());
        assertEquals("Usuari 1", graf.consultarVertexEsquerra("u1"));
    }

    @Test
    public void testInserirVertexEsquerraActualitza() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u1", "Usuari 1 actualitzat");

        assertEquals(1, graf.numVertexEsquerra());
        assertEquals("Usuari 1 actualitzat", graf.consultarVertexEsquerra("u1"));
    }

    // -------------------------------------------------------------------------
    // consultarVertexEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testConsultarVertexEsquerraExistent() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");

        assertEquals("Usuari 1", graf.consultarVertexEsquerra("u1"));
    }

    @Test
    public void testConsultarVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.consultarVertexEsquerra("missing"));
    }

    // -------------------------------------------------------------------------
    // esborrarVertexEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testEsborrarVertexEsquerraExistent() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.esborrarVertexEsquerra("u1");

        assertEquals(0, graf.numVertexEsquerra());
        assertThrows(VertexNoTrobat.class, () -> graf.consultarVertexEsquerra("u1"));
    }

    @Test
    public void testEsborrarVertexEsquerraEnCascada() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 4);
        graf.inserirAresta("u1", "s2", 5);
        graf.inserirAresta("u2", "s1", 3);

        graf.esborrarVertexEsquerra("u1");

        assertEquals(1, graf.numArestes());
        assertEquals(1, graf.numAdjacentsDret("s1"));
        assertTrue(graf.vertexDretAillat("s2"));
    }

    @Test
    public void testEsborrarVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.esborrarVertexEsquerra("missing"));
    }

    // -------------------------------------------------------------------------
    // numVertexEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testNumVertexEsquerraGrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertEquals(0, graf.numVertexEsquerra());
    }

    @Test
    public void testNumVertexEsquerraAmbElements() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        assertEquals(2, graf.numVertexEsquerra());
    }

    // -------------------------------------------------------------------------
    // obtenirVertexIDsEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testObtenirVertexIDsEsquerraGrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertEquals(0, graf.obtenirVertexIDsEsquerra().numElem());
    }

    @Test
    public void testObtenirVertexIDsEsquerraOrdenats() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexEsquerra("u1", "Usuari 1");

        assertEquals("u1", consultar(graf.obtenirVertexIDsEsquerra(), 0));
        assertEquals("u2", consultar(graf.obtenirVertexIDsEsquerra(), 1));
    }

    @Test
    public void testObtenirVertexIDsEsquerraNoDuplicaQuanSactualitza() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u1", "Usuari 1 actualitzat");

        assertEquals(1, graf.obtenirVertexIDsEsquerra().numElem());
        assertEquals("u1", consultar(graf.obtenirVertexIDsEsquerra(), 0));
    }

    // -------------------------------------------------------------------------
    // inserirVertexDret
    // -------------------------------------------------------------------------

    @Test
    public void testInserirVertexDretNou() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");

        assertEquals(1, graf.numVertexDret());
        assertEquals("Song 1", graf.consultarVertexDret("s1"));
    }

    @Test
    public void testInserirVertexDretActualitza() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s1", "Song 1 actualitzada");

        assertEquals(1, graf.numVertexDret());
        assertEquals("Song 1 actualitzada", graf.consultarVertexDret("s1"));
    }

    // -------------------------------------------------------------------------
    // consultarVertexDret
    // -------------------------------------------------------------------------

    @Test
    public void testConsultarVertexDretExistent() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");

        assertEquals("Song 1", graf.consultarVertexDret("s1"));
    }

    @Test
    public void testConsultarVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.consultarVertexDret("missing"));
    }

    // -------------------------------------------------------------------------
    // esborrarVertexDret
    // -------------------------------------------------------------------------

    @Test
    public void testEsborrarVertexDretExistent() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        graf.esborrarVertexDret("s1");

        assertEquals(0, graf.numVertexDret());
        assertThrows(VertexNoTrobat.class, () -> graf.consultarVertexDret("s1"));
    }

    @Test
    public void testEsborrarVertexDretEnCascada() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 4);
        graf.inserirAresta("u2", "s1", 3);
        graf.inserirAresta("u2", "s2", 5);

        graf.esborrarVertexDret("s1");

        assertEquals(1, graf.numArestes());
        assertTrue(graf.vertexEsquerraAillat("u1"));
        assertEquals(1, graf.numAdjacentsEsquerra("u2"));
    }

    @Test
    public void testEsborrarVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.esborrarVertexDret("missing"));
    }

    // -------------------------------------------------------------------------
    // numVertexDret
    // -------------------------------------------------------------------------

    @Test
    public void testNumVertexDretGrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertEquals(0, graf.numVertexDret());
    }

    @Test
    public void testNumVertexDretAmbElements() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        assertEquals(2, graf.numVertexDret());
    }

    // -------------------------------------------------------------------------
    // obtenirVertexIDsDret
    // -------------------------------------------------------------------------

    @Test
    public void testObtenirVertexIDsDretGrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertEquals(0, graf.obtenirVertexIDsDret().numElem());
    }

    @Test
    public void testObtenirVertexIDsDretOrdenats() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirVertexDret("s1", "Song 1");

        assertEquals("s1", consultar(graf.obtenirVertexIDsDret(), 0));
        assertEquals("s2", consultar(graf.obtenirVertexIDsDret(), 1));
    }

    @Test
    public void testObtenirVertexIDsDretNoDuplicaQuanSactualitza() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s1", "Song 1 actualitzada");

        assertEquals(1, graf.obtenirVertexIDsDret().numElem());
        assertEquals("s1", consultar(graf.obtenirVertexIDsDret(), 0));
    }

    // -------------------------------------------------------------------------
    // inserirAresta(K1, K2, E)
    // -------------------------------------------------------------------------

    @Test
    public void testInserirArestaAmbPesNou() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");

        graf.inserirAresta("u1", "s1", 4);

        assertEquals(1, graf.numArestes());
        assertEquals(4, graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaAmbPesActualitza() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 2);
        graf.inserirAresta("u1", "s1", 5);

        assertEquals(1, graf.numArestes());
        assertEquals(5, graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaAmbPesVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(VertexNoTrobat.class, () -> graf.inserirAresta("u1", "s1", 4));
    }

    @Test
    public void testInserirArestaAmbPesVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertThrows(VertexNoTrobat.class, () -> graf.inserirAresta("u1", "s1", 4));
    }

    // -------------------------------------------------------------------------
    // inserirAresta(K1, K2)
    // -------------------------------------------------------------------------

    @Test
    public void testInserirArestaSensePes() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");

        graf.inserirAresta("u1", "s1");

        assertEquals(1, graf.numArestes());
        assertNull(graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaSensePesActualitzaAValorNull() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 4);

        graf.inserirAresta("u1", "s1");

        assertEquals(1, graf.numArestes());
        assertNull(graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaSensePesVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(VertexNoTrobat.class, () -> graf.inserirAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaSensePesVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertThrows(VertexNoTrobat.class, () -> graf.inserirAresta("u1", "s1"));
    }

    @Test
    public void testInserirArestaSensePesDosVertexsInexistents() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.inserirAresta("u1", "s1"));
    }

    // -------------------------------------------------------------------------
    // existeixAresta
    // -------------------------------------------------------------------------

    @Test
    public void testExisteixArestaTrue() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 4);

        assertTrue(graf.existeixAresta("u1", "s1"));
    }

    @Test
    public void testExisteixArestaFalse() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");

        assertFalse(graf.existeixAresta("u1", "s1"));
    }

    @Test
    public void testExisteixArestaVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(VertexNoTrobat.class, () -> graf.existeixAresta("u1", "s1"));
    }

    @Test
    public void testExisteixArestaVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertThrows(VertexNoTrobat.class, () -> graf.existeixAresta("u1", "s1"));
    }

    @Test
    public void testExisteixArestaDosVertexsInexistents() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.existeixAresta("u1", "s1"));
    }

    // -------------------------------------------------------------------------
    // consultarAresta
    // -------------------------------------------------------------------------

    @Test
    public void testConsultarArestaExistent() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 4);

        assertEquals(4, graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testConsultarArestaNoTrobada() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(ArestaNoTrobada.class, () -> graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testConsultarArestaVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(VertexNoTrobat.class, () -> graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testConsultarArestaVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertThrows(VertexNoTrobat.class, () -> graf.consultarAresta("u1", "s1"));
    }

    @Test
    public void testConsultarArestaDosVertexsInexistents() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.consultarAresta("u1", "s1"));
    }

    // -------------------------------------------------------------------------
    // esborrarAresta
    // -------------------------------------------------------------------------

    @Test
    public void testEsborrarArestaExistent() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 4);

        graf.esborrarAresta("u1", "s1");

        assertEquals(0, graf.numArestes());
        assertTrue(graf.vertexEsquerraAillat("u1"));
        assertTrue(graf.vertexDretAillat("s1"));
    }

    @Test
    public void testEsborrarArestaNoTrobada() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(ArestaNoTrobada.class, () -> graf.esborrarAresta("u1", "s1"));
    }

    @Test
    public void testEsborrarArestaVertexEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertThrows(VertexNoTrobat.class, () -> graf.esborrarAresta("u1", "s1"));
    }

    @Test
    public void testEsborrarArestaVertexDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertThrows(VertexNoTrobat.class, () -> graf.esborrarAresta("u1", "s1"));
    }

    @Test
    public void testEsborrarArestaDosVertexsInexistents() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.esborrarAresta("u1", "s1"));
    }

    // -------------------------------------------------------------------------
    // numArestes
    // -------------------------------------------------------------------------

    @Test
    public void testNumArestesGrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertEquals(0, graf.numArestes());
    }

    @Test
    public void testNumArestesAmbDiversesArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s2", 1);
        graf.inserirAresta("u2", "s1", 1);

        assertEquals(3, graf.numArestes());
    }

    @Test
    public void testNumArestesNoCanviaQuanSactualitzaUnaAresta() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s1", 5);

        assertEquals(1, graf.numArestes());
    }

    // -------------------------------------------------------------------------
    // vertexEsquerraAillat
    // -------------------------------------------------------------------------

    @Test
    public void testVertexEsquerraAillatTrue() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertTrue(graf.vertexEsquerraAillat("u1"));
    }

    @Test
    public void testVertexEsquerraAillatFalse() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);
        assertFalse(graf.vertexEsquerraAillat("u1"));
    }

    @Test
    public void testVertexEsquerraAillatInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.vertexEsquerraAillat("missing"));
    }

    // -------------------------------------------------------------------------
    // vertexDretAillat
    // -------------------------------------------------------------------------

    @Test
    public void testVertexDretAillatTrue() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertTrue(graf.vertexDretAillat("s1"));
    }

    @Test
    public void testVertexDretAillatFalse() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);
        assertFalse(graf.vertexDretAillat("s1"));
    }

    @Test
    public void testVertexDretAillatInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.vertexDretAillat("missing"));
    }

    // -------------------------------------------------------------------------
    // numAdjacentsEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testNumAdjacentsEsquerraGrafSenseArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertEquals(0, graf.numAdjacentsEsquerra("u1"));
    }

    @Test
    public void testNumAdjacentsEsquerraAmbArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s2", 1);
        assertEquals(2, graf.numAdjacentsEsquerra("u1"));
    }

    @Test
    public void testNumAdjacentsEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.numAdjacentsEsquerra("missing"));
    }

    // -------------------------------------------------------------------------
    // numAdjacentsDret
    // -------------------------------------------------------------------------

    @Test
    public void testNumAdjacentsDretGrafSenseArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertEquals(0, graf.numAdjacentsDret("s1"));
    }

    @Test
    public void testNumAdjacentsDretAmbArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u2", "s1", 1);
        assertEquals(2, graf.numAdjacentsDret("s1"));
    }

    @Test
    public void testNumAdjacentsDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.numAdjacentsDret("missing"));
    }

    // -------------------------------------------------------------------------
    // obtenirAdjacentsEsquerra
    // -------------------------------------------------------------------------

    @Test
    public void testObtenirAdjacentsEsquerraGrafSenseArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        assertEquals(0, graf.obtenirAdjacentsEsquerra("u1").numElem());
    }

    @Test
    public void testObtenirAdjacentsEsquerraOrdenats() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s2", 1);
        graf.inserirAresta("u1", "s1", 1);

        assertEquals("s1", consultar(graf.obtenirAdjacentsEsquerra("u1"), 0));
        assertEquals("s2", consultar(graf.obtenirAdjacentsEsquerra("u1"), 1));
    }

    @Test
    public void testObtenirAdjacentsEsquerraDespresDesborrarAresta() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);

        graf.esborrarAresta("u1", "s1");

        assertEquals(0, graf.obtenirAdjacentsEsquerra("u1").numElem());
    }

    @Test
    public void testObtenirAdjacentsEsquerraInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.obtenirAdjacentsEsquerra("missing"));
    }

    // -------------------------------------------------------------------------
    // obtenirAdjacentsDret
    // -------------------------------------------------------------------------

    @Test
    public void testObtenirAdjacentsDretGrafSenseArestes() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        assertEquals(0, graf.obtenirAdjacentsDret("s1").numElem());
    }

    @Test
    public void testObtenirAdjacentsDretOrdenats() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u2", "s1", 1);
        graf.inserirAresta("u1", "s1", 1);

        assertEquals("u1", consultar(graf.obtenirAdjacentsDret("s1"), 0));
        assertEquals("u2", consultar(graf.obtenirAdjacentsDret("s1"), 1));
    }

    @Test
    public void testObtenirAdjacentsDretDespresDesborrarAresta() throws VertexNoTrobat, ArestaNoTrobada {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirAresta("u1", "s1", 1);

        graf.esborrarAresta("u1", "s1");

        assertEquals(0, graf.obtenirAdjacentsDret("s1").numElem());
    }

    @Test
    public void testObtenirAdjacentsDretInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.obtenirAdjacentsDret("missing"));
    }

    // -------------------------------------------------------------------------
    // obtenirVertexsEsquerraAmb2AdjacentsCompartits
    // -------------------------------------------------------------------------

    @Test
    public void testObtenirVertexsEsquerraAmb2AdjacentsCompartitsDiversosResultats() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexEsquerra("u3", "Usuari 3");
        graf.inserirVertexEsquerra("u4", "Usuari 4");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirVertexDret("s3", "Song 3");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s2", 1);
        graf.inserirAresta("u1", "s3", 1);
        graf.inserirAresta("u2", "s1", 1);
        graf.inserirAresta("u2", "s2", 1);
        graf.inserirAresta("u3", "s2", 1);
        graf.inserirAresta("u3", "s3", 1);
        graf.inserirAresta("u4", "s1", 1);

        TADLlista<String> compartits = graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits("u1");

        assertEquals(2, compartits.numElem());
        assertEquals("u2", consultar(compartits, 0));
        assertEquals("u3", consultar(compartits, 1));
    }

    @Test
    public void testObtenirVertexsEsquerraAmb2AdjacentsCompartitsSenseResultats() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u2", "s2", 1);

        assertEquals(0, graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits("u1").numElem());
    }

    @Test
    public void testObtenirVertexsEsquerraAmb2AdjacentsCompartitsAmbUnaSolaCoincidenciaNoCompta() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s2", 1);
        graf.inserirAresta("u2", "s1", 1);

        assertEquals(0, graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits("u1").numElem());
    }

    @Test
    public void testObtenirVertexsEsquerraAmb2AdjacentsCompartitsNoInclouElMateixVertex() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 1);
        graf.inserirAresta("u1", "s2", 1);
        graf.inserirAresta("u2", "s1", 1);
        graf.inserirAresta("u2", "s2", 1);

        TADLlista<String> compartits = graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits("u1");

        assertEquals(1, compartits.numElem());
        assertEquals("u2", consultar(compartits, 0));
    }

    @Test
    public void testObtenirVertexsEsquerraAmb2AdjacentsCompartitsVertexInexistent() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertThrows(VertexNoTrobat.class, () -> graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits("missing"));
    }

    // -------------------------------------------------------------------------
    // distribucioGrauK1
    // -------------------------------------------------------------------------

    @Test
    public void testDistribucioGrauK1GrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertArrayEquals(new int[0], graf.distribucioGrauK1());
    }

    @Test
    public void testDistribucioGrauK1AmbGrauZero() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexEsquerra("u3", "Usuari 3");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirAresta("u1", "s1", 4);
        graf.inserirAresta("u1", "s2", 5);
        graf.inserirAresta("u2", "s1", 3);

        assertArrayEquals(new int[]{1, 1, 1}, graf.distribucioGrauK1());
    }

    @Test
    public void testDistribucioGrauK1AmbTotsElsVertexsAillats() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexEsquerra("u3", "Usuari 3");

        assertArrayEquals(new int[]{3}, graf.distribucioGrauK1());
    }

    // -------------------------------------------------------------------------
    // distribucioGrauK2
    // -------------------------------------------------------------------------

    @Test
    public void testDistribucioGrauK2GrafBuit() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        assertArrayEquals(new int[0], graf.distribucioGrauK2());
    }

    @Test
    public void testDistribucioGrauK2AmbGrauZero() throws VertexNoTrobat {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexEsquerra("u1", "Usuari 1");
        graf.inserirVertexEsquerra("u2", "Usuari 2");
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");
        graf.inserirVertexDret("s3", "Song 3");
        graf.inserirAresta("u1", "s1", 4);
        graf.inserirAresta("u1", "s2", 5);
        graf.inserirAresta("u2", "s1", 3);

        assertArrayEquals(new int[]{1, 1, 1}, graf.distribucioGrauK2());
    }

    @Test
    public void testDistribucioGrauK2AmbTotsElsVertexsAillats() {
        GrafBipartit<String, String, String, String, Integer> graf = new GrafBipartit<>(100);
        graf.inserirVertexDret("s1", "Song 1");
        graf.inserirVertexDret("s2", "Song 2");

        assertArrayEquals(new int[]{2}, graf.distribucioGrauK2());
    }

    private <E> E consultar(TADLlista<E> llista, int pos) {
        try {
            return llista.consultar(pos);
        } catch (LlistaBuida e) {
            throw new IllegalStateException(e);
        }
    }
}
