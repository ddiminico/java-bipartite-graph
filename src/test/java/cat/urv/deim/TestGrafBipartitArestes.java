package cat.urv.deim;

import org.junit.jupiter.api.Test;

import cat.urv.deim.exceptions.ElementNoTrobat;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGrafBipartitArestes {

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
    public void testInserirValoracioCreaAresta() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        graf.inserirValoracio(usuari, canco, 4);

        assertEquals(1, graf.numValoracions());
        assertTrue(graf.existeixValoracio(usuari, canco));
        assertEquals(4, graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testInserirValoracioActualitzaPes() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        graf.inserirValoracio(usuari, canco, 2);
        graf.inserirValoracio(usuari, canco, 5);

        assertEquals(1, graf.numValoracions());
        assertEquals(5, graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testInserirValoracioSenseVertexsLlançaExcepcio() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.inserirValoracio(crearUsuari1(), crearCanco1(), 3));
    }

    @Test
    public void testExisteixValoracioAmbVertexInexistentRetornaFalse() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirCanco(crearCanco1());
        assertFalse(graf.existeixValoracio(crearUsuari1(), crearCanco1()));
    }

    @Test
    public void testConsultarValoracioInexistent() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        assertThrows(ElementNoTrobat.class, () -> graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testAdjacentsDesDUsuariIDesDCanco() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();
        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari1, canco2, 5);
        graf.inserirValoracio(usuari2, canco1, 3);

        TADLlista<String> canconsUsuari1 = graf.obtenirCanconsValorades(usuari1);
        TADLlista<String> usuarisCanco1 = graf.obtenirUsuarisQueHanValorat(canco1);

        assertEquals(2, graf.numCanconsValorades(usuari1));
        assertEquals(2, graf.numUsuarisQueHanValorat(canco1));
        assertTrue(canconsUsuari1.existeix("Song Test 1"));
        assertTrue(canconsUsuari1.existeix("Song Test 2"));
        assertTrue(usuarisCanco1.existeix("user-test-1"));
        assertTrue(usuarisCanco1.existeix("user-test-2"));
    }

    @Test
    public void testTeValoracionsUsuariITeValoracionsCanco() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        assertFalse(graf.teValoracionsUsuari(usuari));
        assertFalse(graf.teValoracionsCanco(canco));

        graf.inserirValoracio(usuari, canco, 4);

        assertTrue(graf.teValoracionsUsuari(usuari));
        assertTrue(graf.teValoracionsCanco(canco));
    }

    @Test
    public void testTeValoracionsAmbFitxers() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Usuari usuari = graf.consultarUsuari("user0001");
        Canco canco = graf.consultarCanco("Bohemian Rhapsody");

        assertTrue(graf.teValoracionsUsuari(usuari));
        assertTrue(graf.teValoracionsCanco(canco));
        assertTrue(graf.existeixValoracio(usuari, canco));
    }

    @Test
    public void testEsborrarValoracio() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);
        graf.inserirValoracio(usuari, canco, 4);

        graf.esborrarValoracio(usuari, canco);

        assertEquals(0, graf.numValoracions());
        assertFalse(graf.existeixValoracio(usuari, canco));
    }

    @Test
    public void testValoracioForaDeRang() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        assertThrows(IllegalArgumentException.class, () -> graf.inserirValoracio(usuari, canco, 0));
        assertThrows(IllegalArgumentException.class, () -> graf.inserirValoracio(usuari, canco, 6));
    }

    @Test
    public void testCarregaMassivaValoracionsDense() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        assertEquals(10000, graf.numValoracions());
    }

    @Test
    public void testCarregaMassivaValoracionsDenseAmbValorConcret() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Usuari usuari = graf.consultarUsuari("user0001");
        Canco canco = graf.consultarCanco("Bohemian Rhapsody");

        assertTrue(graf.existeixValoracio(usuari, canco));
        assertEquals(3, graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testFitxerCadaUsuariTe10Valoracions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Usuari usuari = graf.consultarUsuari("user0001");

        assertEquals(10, graf.numCanconsValorades(usuari));
        assertTrue(graf.teValoracionsUsuari(usuari));
    }

    @Test
    public void testFitxerCadaCancoTe10Usuaris() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Canco canco = graf.consultarCanco("Bohemian Rhapsody");

        assertEquals(10, graf.numUsuarisQueHanValorat(canco));
        assertTrue(graf.teValoracionsCanco(canco));
    }

    @Test
    public void testObtenirCanconsValoradesDesDeFitxer() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Usuari usuari = graf.consultarUsuari("user0001");
        TADLlista<String> cancons = graf.obtenirCanconsValorades(usuari);

        assertEquals(10, cancons.numElem());
        assertTrue(cancons.existeix("Bohemian Rhapsody"));
        assertTrue(cancons.existeix("Imagine"));
        assertTrue(cancons.existeix("Ancient Symphony"));
        assertTrue(cancons.existeix("Southern Dawn"));
    }

    @Test
    public void testObtenirUsuarisQueHanValoratDesDeFitxer() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        Canco canco = graf.consultarCanco("Bohemian Rhapsody");
        TADLlista<String> usuaris = graf.obtenirUsuarisQueHanValorat(canco);

        assertEquals(10, usuaris.numElem());
        assertTrue(usuaris.existeix("user0001"));
        assertTrue(usuaris.existeix("user0999"));
        assertTrue(usuaris.existeix("user1000"));
    }

    @Test
    public void testUsuarisAmb2CanconsCompartides() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Usuari usuari3 = new Usuari(3, "user-test-3", "Usuari Test 3", "Italy", 29);
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();
        Canco canco3 = new Canco(10003, "Song Test 3", "Artist Test", "Indie", 2009, 201, 65);

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirUsuari(usuari3);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirCanco(canco3);

        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari1, canco2, 5);
        graf.inserirValoracio(usuari1, canco3, 3);
        graf.inserirValoracio(usuari2, canco1, 2);
        graf.inserirValoracio(usuari2, canco2, 1);
        graf.inserirValoracio(usuari3, canco1, 5);

        TADLlista<String> compatibles = graf.obtenirUsuarisAmb2CanconsCompartides(usuari1);

        assertEquals(1, compatibles.numElem());
        assertTrue(compatibles.existeix("user-test-2"));
        assertFalse(compatibles.existeix("user-test-1"));
        assertFalse(compatibles.existeix("user-test-3"));
    }

    @Test
    public void testUsuarisAmb2CanconsCompartidesSenseCoincidencies() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari2, canco2, 5);

        TADLlista<String> compatibles = graf.obtenirUsuarisAmb2CanconsCompartides(usuari1);

        assertEquals(0, compatibles.numElem());
    }

    @Test
    public void testDistribucioGrausUsuaris() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Usuari usuari3 = new Usuari(3, "user-test-3", "Usuari Test 3", "Italy", 29);
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirUsuari(usuari3);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);

        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari1, canco2, 5);
        graf.inserirValoracio(usuari2, canco1, 3);

        assertArrayEquals(new int[]{1, 1, 1}, graf.distribucioGrauUsuaris());
    }

    @Test
    public void testDistribucioGrausUsuarisGrafBuit() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertArrayEquals(new int[0], graf.distribucioGrauUsuaris());
    }

    @Test
    public void testDistribucioGrausUsuarisAmbFitxerMantéInvariants() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        int[] distribucio = graf.distribucioGrauUsuaris();

        assertEquals(1000, suma(distribucio));
        assertEquals(10000, sumaPonderada(distribucio));
    }

    @Test
    public void testDistribucioGrausUsuarisAmbFitxerEsExacta() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        int[] distribucio = graf.distribucioGrauUsuaris();

        assertEquals(11, distribucio.length);
        for (int i = 0; i < 10; i++) {
            assertEquals(0, distribucio[i]);
        }
        assertEquals(1000, distribucio[10]);
    }

    @Test
    public void testDistribucioGrausCancons() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Usuari usuari3 = new Usuari(3, "user-test-3", "Usuari Test 3", "Italy", 29);
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirUsuari(usuari3);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);

        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari1, canco2, 5);
        graf.inserirValoracio(usuari2, canco1, 3);

        assertArrayEquals(new int[]{0, 1, 1}, graf.distribucioGrauCancons());
    }

    @Test
    public void testDistribucioGrausCanconsAmbGrauZero() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();
        Canco canco3 = new Canco(10003, "Song Test 3", "Artist Test", "Indie", 2009, 201, 65);

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirCanco(canco3);

        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari2, canco1, 3);
        graf.inserirValoracio(usuari1, canco2, 5);

        assertArrayEquals(new int[]{1, 1, 1}, graf.distribucioGrauCancons());
    }

    @Test
    public void testDistribucioGrausCanconsGrafBuit() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertArrayEquals(new int[0], graf.distribucioGrauCancons());
    }

    @Test
    public void testDistribucioGrausCanconsAmbFitxerMantéInvariants() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        int[] distribucio = graf.distribucioGrauCancons();

        assertEquals(1000, suma(distribucio));
        assertEquals(10000, sumaPonderada(distribucio));
    }

    @Test
    public void testDistribucioGrausCanconsAmbFitxerEsExacta() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        int[] distribucio = graf.distribucioGrauCancons();

        assertEquals(11, distribucio.length);
        for (int i = 0; i < 10; i++) {
            assertEquals(0, distribucio[i]);
        }
        assertEquals(1000, distribucio[10]);
    }

    @Test
    public void testComptarCanconsAmbValoracioMitjaSuperiorA() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Canco canco1 = crearCanco1();
        Canco canco2 = crearCanco2();
        Canco canco3 = new Canco(10003, "Song Test 3", "Artist Test", "Indie", 2009, 201, 65);

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirCanco(canco1);
        graf.inserirCanco(canco2);
        graf.inserirCanco(canco3);

        graf.inserirValoracio(usuari1, canco1, 4);
        graf.inserirValoracio(usuari2, canco1, 5);
        graf.inserirValoracio(usuari1, canco2, 2);
        graf.inserirValoracio(usuari2, canco2, 3);

        assertEquals(1, graf.comptarCanconsAmbValoracioMitjaSuperiorA(4.0));
        assertEquals(2, graf.comptarCanconsAmbValoracioMitjaSuperiorA(2.0));
        assertEquals(0, graf.comptarCanconsAmbValoracioMitjaSuperiorA(4.5));
    }

    @Test
    public void testComptarCanconsAmbValoracioMitjaSuperiorAIgnoraSenseValoracions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        graf.inserirCanco(crearCanco1());
        graf.inserirCanco(crearCanco2());

        assertEquals(0, graf.comptarCanconsAmbValoracioMitjaSuperiorA(0.0));
    }

    @Test
    public void testObtenirUsuarisQueHanEscoltatGenere() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari1 = crearUsuari1();
        Usuari usuari2 = crearUsuari2();
        Usuari usuari3 = new Usuari(3, "user-test-3", "Usuari Test 3", "Italy", 29);
        Canco rock1 = crearCanco1();
        Canco rock2 = new Canco(10003, "Song Test 3", "Artist Test", "Rock", 2009, 201, 65);
        Canco pop = crearCanco2();

        graf.inserirUsuari(usuari1);
        graf.inserirUsuari(usuari2);
        graf.inserirUsuari(usuari3);
        graf.inserirCanco(rock1);
        graf.inserirCanco(rock2);
        graf.inserirCanco(pop);

        graf.inserirValoracio(usuari1, rock1, 4);
        graf.inserirValoracio(usuari1, rock2, 5);
        graf.inserirValoracio(usuari2, rock2, 3);
        graf.inserirValoracio(usuari3, pop, 5);

        TADLlista<String> usuarisRock = graf.obtenirUsuarisQueHanEscoltatGenere("Rock");

        assertEquals(2, usuarisRock.numElem());
        assertTrue(usuarisRock.existeix("user-test-1"));
        assertTrue(usuarisRock.existeix("user-test-2"));
        assertFalse(usuarisRock.existeix("user-test-3"));
    }

    @Test
    public void testObtenirUsuarisQueHanEscoltatGenereSenseCoincidencies() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        TADLlista<String> usuarisJazz = graf.obtenirUsuarisQueHanEscoltatGenere("Jazz");
        assertEquals(0, usuarisJazz.numElem());
    }

    @Test
    public void testObtenirUsuarisQueHanEscoltatGenereAmbFitxers() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        TADLlista<String> usuarisRock = graf.obtenirUsuarisQueHanEscoltatGenere("Rock");

        assertTrue(usuarisRock.numElem() > 0);
        assertTrue(usuarisRock.existeix("user0001"));
    }

    @Test
    public void testObtenirCanconsValoradesUsuariInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.obtenirCanconsValorades(crearUsuari1()));
    }

    @Test
    public void testObtenirUsuarisQueHanValoratCancoInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.obtenirUsuarisQueHanValorat(crearCanco1()));
    }

    @Test
    public void testObtenirUsuarisAmb2CanconsCompartidesUsuariInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.obtenirUsuarisAmb2CanconsCompartides(crearUsuari1()));
    }

    @Test
    public void testEsborrarValoracioInexistent() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        assertThrows(ElementNoTrobat.class, () -> graf.esborrarValoracio(usuari, canco));
    }

    @Test
    public void testNumCanconsValoradesUsuariInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.numCanconsValorades(crearUsuari1()));
    }

    @Test
    public void testNumUsuarisQueHanValoratCancoInexistent() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        assertThrows(ElementNoTrobat.class, () -> graf.numUsuarisQueHanValorat(crearCanco1()));
    }

    @Test
    public void testComptarCanconsAmbValoracioMitjaSuperiorAAmbFitxers() {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000, "songs1000.csv", "users1000.csv", "ratings.csv");
        int resultat = graf.comptarCanconsAmbValoracioMitjaSuperiorA(4.0);

        assertTrue(resultat >= 0);
        assertTrue(resultat <= 1000);
    }

    @Test
    public void testObtenirUsuarisQueHanEscoltatGenereExistentSenseValoracions() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Canco canco = crearCanco1();
        graf.inserirCanco(canco);

        TADLlista<String> usuaris = graf.obtenirUsuarisQueHanEscoltatGenere("Rock");
        assertEquals(0, usuaris.numElem());
    }

    @Test
    public void testValoracioLimitInferior() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        graf.inserirValoracio(usuari, canco, 1);
        assertEquals(1, graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testValoracioLimitSuperior() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);

        graf.inserirValoracio(usuari, canco, 5);
        assertEquals(5, graf.consultarValoracio(usuari, canco));
    }

    @Test
    public void testEsBuidaDesprésInserirIEsborrarTot() throws ElementNoTrobat {
        GrafUsuarisCancons graf = new GrafUsuarisCancons(2000);
        Usuari usuari = crearUsuari1();
        Canco canco = crearCanco1();
        graf.inserirUsuari(usuari);
        graf.inserirCanco(canco);
        graf.inserirValoracio(usuari, canco, 3);

        graf.esborrarUsuari("user-test-1");
        graf.esborrarCanco("Song Test 1");

        assertTrue(graf.esBuida());
        assertEquals(0, graf.numUsuaris());
        assertEquals(0, graf.numCancons());
        assertEquals(0, graf.numValoracions());
    }

    private int suma(int[] array) {
        int total = 0;
        for (int valor : array) {
            total += valor;
        }
        return total;
    }

    private int sumaPonderada(int[] array) {
        int total = 0;
        for (int i = 0; i < array.length; i++) {
            total += i * array[i];
        }
        return total;
    }
}
