package cat.urv.deim;

import org.junit.jupiter.api.Test;

import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaBuida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGrafBipartitVertexs {

    private Usuari crearUsuari1() {
        return new Usuari(1, "user-test-1", "Usuari Test 1", "Spain", 21);
    }

    private Usuari crearUsuari2() {
        return new Usuari(2, "user-test-2", "Usuari Test 2", "France", 34);
    }

    private Canco crearCanco1() {
        return new Canco(10001, "Song Test 1", "Artist Test", "Rock", 2001, 210, 80);
    }

    private Canco crearCanco2() {
        return new Canco(10002, "Song Test 2", "Artist Test", "Pop", 2005, 180, 70);
    }

    @Test
    public void testGrafBuidaInicialment() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertTrue(graf.esBuida());
    }

    @Test
    public void testInserirUsuariFaCreixerElGraf() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirUsuari(crearUsuari1());
        assertEquals(1, graf.numUsuaris());
        assertFalse(graf.esBuida());
    }

    @Test
    public void testInserirUsuariDuplicatActualitzaValor() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirUsuari(crearUsuari1());
        graf.inserirUsuari(new Usuari(1, "user-test-1", "Usuari Sobrescrit", "Italy", 40));

        Usuari usuari = graf.consultarUsuari("user-test-1");
        assertEquals("Usuari Sobrescrit", usuari.getNom());
        assertEquals(40, usuari.getEdat());
        assertEquals(1, graf.numUsuaris());
    }

    @Test
    public void testInserirCancoDuplicadaActualitzaValor() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirCanco(crearCanco1());
        graf.inserirCanco(new Canco(10001, "Song Test 1", "Another Artist", "Jazz", 2010, 250, 99));

        Canco canco = graf.consultarCanco("Song Test 1");
        assertEquals("Another Artist", canco.getArtista());
        assertEquals(99, canco.getPopularitat());
        assertEquals(1, graf.numCancons());
    }

    @Test
    public void testCarregaMassivaUsuarisICancons() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", null);
        assertEquals(1000, graf.numUsuaris());
        assertEquals(1000, graf.numCancons());
    }

    @Test
    public void testCarregaMassivaAmbValoracionsMantéRecompteDeVertexs() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        assertEquals(1000, graf.numUsuaris());
        assertEquals(1000, graf.numCancons());
        assertFalse(graf.esBuida());
    }

    @Test
    public void testEsborrarUsuariEliminaLesSevesValoracions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirValoracio(usuari, canco1, 4);
        graf.inserirValoracio(usuari, canco2, 5);

        graf.esborrarUsuari("user-test-1");

        assertEquals(0, graf.numUsuaris());
        assertEquals(0, graf.numValoracions());
        assertThrows(ElementNoTrobat.class, () -> graf.consultarUsuari("user-test-1"));
    }

    @Test
    public void testEsborrarCancoEliminaLesSevesValoracions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirCanco(canco);
        graf.inserirValoracio(usuari1, canco, 4);
        graf.inserirValoracio(usuari2, canco, 2);

        graf.esborrarCanco("Song Test 1");

        assertEquals(0, graf.numCancons());
        assertEquals(0, graf.numValoracions());
        assertThrows(ElementNoTrobat.class, () -> graf.consultarCanco("Song Test 1"));
    }

    @Test
    public void testConsultarUsuariInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.consultarUsuari("missing-user"));
    }

    @Test
    public void testConsultarCancoInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.consultarCanco("Missing Song"));
    }

    @Test
    public void testObtenirIdsOrdenats() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirUsuari(crearUsuari2());
        graf.inserirUsuari(crearUsuari1());
        graf.inserirCanco(crearCanco2());
        graf.inserirCanco(crearCanco1());

        TADLlista<String> usernames = graf.obtenirUsernames();
        TADLlista<String> titols = graf.obtenirTitols();

        assertEquals("user-test-1", consultar(usernames, 0));
        assertEquals("user-test-2", consultar(usernames, 1));
        assertEquals("Song Test 1", consultar(titols, 0));
        assertEquals("Song Test 2", consultar(titols, 1));
    }

    @Test
    public void testConsultarUsuariCarregatDesDeFitxer() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", null);
        Usuari usuari = graf.consultarUsuari("user0001");

        assertEquals("Oliver Smith", usuari.getNom());
        assertEquals("Espana", usuari.getPais());
        assertEquals(18, usuari.getEdat());
    }

    @Test
    public void testConsultarCancoCarregadaDesDeFitxer() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", null);
        Canco canco = graf.consultarCanco("Bohemian Rhapsody");

        assertEquals("Queen", canco.getArtista());
        assertEquals("Rock", canco.getGenere());
        assertEquals(1975, canco.getAny());
    }

    @Test
    public void testObtenirUsernamesOrdenatsDesDeFitxer() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", null);
        TADLlista<String> usernames = graf.obtenirUsernames();

        assertEquals("user0001", consultar(usernames, 0));
        assertEquals("user0002", consultar(usernames, 1));
        assertEquals("user1000", consultar(usernames, 999));
        assertEquals(1000, usernames.numElem());
    }

    @Test
    public void testObtenirTitolsOrdenatsDesDeFitxer() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", null);
        TADLlista<String> titols = graf.obtenirTitols();

        assertEquals("Above the Arrow", consultar(titols, 0));
        assertEquals("Above the Forest", consultar(titols, 1));
        assertEquals("Without the Wave", consultar(titols, 999));
        assertEquals(1000, titols.numElem());
    }

    @Test
    public void testEsborrarUsuariInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.esborrarUsuari("missing-user"));
    }

    @Test
    public void testEsborrarCancoInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.esborrarCanco("Missing Song"));
    }

    private <E> E consultar(TADLlista<E> llista, int pos) {
        try {
            return llista.consultar(pos);
        } catch (LlistaBuida e) {
            throw new IllegalStateException(e);
        }
    }
}
