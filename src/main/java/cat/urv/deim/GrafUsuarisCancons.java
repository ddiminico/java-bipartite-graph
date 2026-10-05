package cat.urv.deim;

import cat.urv.deim.exceptions.ArestaNoTrobada;
import cat.urv.deim.exceptions.ElementNoTrobat;
import cat.urv.deim.exceptions.LlistaPlena;
import cat.urv.deim.exceptions.LlistaBuida;
import cat.urv.deim.exceptions.VertexNoTrobat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GrafUsuarisCancons {

    // K1 (Clau Esquerra): Serà un String (el username)
    // V1 (Valor Esquerra): Serà un objecte Usuari sencer
    // K2 (Clau Dreta): Serà un String (el títol de la cançó)
    // V2 (Valor Dret): Serà un objecte Canco sencer
    // E (Aresta): Serà un Integer (la nota de l'1 al 5)
    private TADGrafBipartit<String, Usuari, String, Canco, Integer> graf;

    /**
     * Constructor bàsic. Inicialitza el graf buit.
     * La 'mida' en aquesta implementació dinàmica pot ser ignorada o utilitzada
     * per inicialitzar els HashMaps interns si es volgués, però per defecte creem el graf genèric.
     */
    public GrafUsuarisCancons(int mida) {
        this.graf = new GrafBipartit<>();
    }

    /**
     * Constructor avançat. Inicialitza el graf i carrega les dades des dels fitxers CSV.
     */
    public GrafUsuarisCancons(int mida, String fitxerCancons, String fitxerUsuaris, String fitxerValoracions) {
        this.graf = new GrafBipartit<>();
        // Per si algú crida el mètode sense passar-li algun dels tres fitxers
        if (fitxerCancons != null) carregarCancons(fitxerCancons);
        if (fitxerUsuaris != null) carregarUsuaris(fitxerUsuaris);
        if (fitxerValoracions != null) carregarValoracions(fitxerValoracions);
    }

    // Mètodes privats per carregar les dades des dels fitxers CSV. S'espera que els fitxers
    // tinguin un format específic i que les dades siguin vàlides.
    // carregarUsuaris fa el mateix que carregarCancons però construint l'objecte Usuari,
    // i carregarValoracions crida directament a inserirAresta unint els dos

    private void carregarCancons(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linia;
            br.readLine(); // Saltem la primera línia (capçaleres)
            while ((linia = br.readLine()) != null) {
                // format: id,titol,artista,genere,any,durada_segons,popularitat
                String[] dades = linia.split(",");
                if (dades.length >= 7) {
                    Canco c = new Canco(
                        Integer.parseInt(dades[0]), dades[1], dades[2], dades[3],
                        Integer.parseInt(dades[4]), Integer.parseInt(dades[5]), Integer.parseInt(dades[6])
                    );
                    inserirCanco(c);
                }
            }
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer de cançons: " + e.getMessage());
        }
    }

    private void carregarUsuaris(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linia;
            br.readLine(); // Saltem la primera línia (capçaleres)
            while ((linia = br.readLine()) != null) {
                // format: id,username,name,country,age
                String[] dades = linia.split(",");
                if (dades.length >= 5) {
                    Usuari u = new Usuari(
                        Integer.parseInt(dades[0]), dades[1], dades[2],
                        dades[3], Integer.parseInt(dades[4])
                    );
                    inserirUsuari(u);
                }
            }
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer d'usuaris: " + e.getMessage());
        }
    }

    private void carregarValoracions(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linia;
            br.readLine(); // Saltem capçalera
            while ((linia = br.readLine()) != null) {
                // format: username,song_title,rating
                String[] dades = linia.split(",");
                if (dades.length >= 3) {
                    try {
                        String username = dades[0];
                        String titol = dades[1];
                        int rating = Integer.parseInt(dades[2]);
                        graf.inserirAresta(username, titol, rating);
                    } catch (VertexNoTrobat ex) {
                        // Ignorem la valoració si l'usuari o la cançó no existeixen prèviament
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error llegint el fitxer de valoracions: " + e.getMessage());
        }
    }

    // Gestió d'usuaris (Costat Esquerre)

    public void inserirUsuari(Usuari usuari) {
        // Rebem un objecte Usuari sencer, però necessitem dues coses:
        // una Clau (per buscar ràpidament al HashMap) i un Valor (l'objecte a guardar)
        // Convertim el mètode en dues peces cridant a .getNomUsuari() com a clau K1,
        // i l'objecte sencer com a V1
        graf.inserirVertexEsquerra(usuari.getNomUsuari(), usuari);
    }

    public Usuari consultarUsuari(String nomUsuari) throws ElementNoTrobat {
        // Agafa l'excepció genèrica que llança GrafBipartit i la disfressa de l'excepció que espera el sistema exterior
        try {
            return graf.consultarVertexEsquerra(nomUsuari);
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public void esborrarUsuari(String nomUsuari) throws ElementNoTrobat {
        try {
            graf.esborrarVertexEsquerra(nomUsuari);
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    // Per un humà és molt més intuïtiu cridar a numUsuaris() que haver d'entendre què és un VertexEsquerra
    public int numUsuaris() {
        return graf.numVertexEsquerra();
    }

    public TADLlista<String> obtenirUsernames() {
        return graf.obtenirVertexIDsEsquerra();
    }

    // Gestió de cançons (Costat Dret)

    public void inserirCanco(Canco canco) {
        graf.inserirVertexDret(canco.getTitol(), canco);
    }

    public Canco consultarCanco(String titol) throws ElementNoTrobat {
        try {
            return graf.consultarVertexDret(titol);
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public void esborrarCanco(String titol) throws ElementNoTrobat {
        try {
            graf.esborrarVertexDret(titol);
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int numCancons() {
        return graf.numVertexDret();
    }

    public TADLlista<String> obtenirTitols() {
        return graf.obtenirVertexIDsDret();
    }

    // Gestió de valoracions (Arestes)

    public void inserirValoracio(Usuari usuari, Canco canco, int valoracio) throws ElementNoTrobat {
        // No pots valorar una cançó amb un -8 o un 12
        if (valoracio < 1 || valoracio > 5) {
            throw new IllegalArgumentException("Valoració fora de rang (1-5)");
        }
        try {
            graf.inserirAresta(usuari.getNomUsuari(), canco.getTitol(), valoracio);
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public boolean existeixValoracio(Usuari usuari, Canco canco) {
        try {
            return graf.existeixAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch (VertexNoTrobat e) {
            return false;
        }
    }

    public int consultarValoracio(Usuari usuari, Canco canco) throws ElementNoTrobat {
        try {
            return graf.consultarAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch (VertexNoTrobat | ArestaNoTrobada e) {
            throw new ElementNoTrobat();
        }
    }

    public void esborrarValoracio(Usuari usuari, Canco canco) throws ElementNoTrobat {
        try {
            graf.esborrarAresta(usuari.getNomUsuari(), canco.getTitol());
        } catch (VertexNoTrobat | ArestaNoTrobada e) {
            throw new ElementNoTrobat();
        }
    }

    public int numValoracions() {
        return graf.numArestes();
    }

    // Consultes d'adjacència i grau

    public int numCanconsValorades(Usuari usuari) throws ElementNoTrobat {
        try {
            return graf.numAdjacentsEsquerra(usuari.getNomUsuari());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int numUsuarisQueHanValorat(Canco canco) throws ElementNoTrobat {
        try {
            return graf.numAdjacentsDret(canco.getTitol());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public boolean teValoracionsUsuari(Usuari usuari) {
        try {
            return !graf.vertexEsquerraAillat(usuari.getNomUsuari());
        } catch (VertexNoTrobat e) {
            return false;
        }
    }

    public boolean teValoracionsCanco(Canco canco) {
        try {
            return !graf.vertexDretAillat(canco.getTitol());
        } catch (VertexNoTrobat e) {
            return false;
        }
    }

    public TADLlista<String> obtenirCanconsValorades(Usuari usuari) throws ElementNoTrobat {
        try {
            return graf.obtenirAdjacentsEsquerra(usuari.getNomUsuari());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public TADLlista<String> obtenirUsuarisQueHanValorat(Canco canco) throws ElementNoTrobat {
        try {
            return graf.obtenirAdjacentsDret(canco.getTitol());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public TADLlista<String> obtenirUsuarisAmb2CanconsCompartides(Usuari usuari) throws ElementNoTrobat {
        try {
            return graf.obtenirVertexsEsquerraAmb2AdjacentsCompartits(usuari.getNomUsuari());
        } catch (VertexNoTrobat e) {
            throw new ElementNoTrobat();
        }
    }

    public int[] distribucioGrauUsuaris() {
        return graf.distribucioGrauK1();
    }

    public int[] distribucioGrauCancons() {
        return graf.distribucioGrauK2();
    }

    // Consultes específiques del problema

    /**
     * Compta quantes cançons tenen una valoració mitjana estrictament superior al llindar.
     */
    public int comptarCanconsAmbValoracioMitjaSuperiorA(double llindar) {
        int comptador = 0;
        TADLlista<String> titols = graf.obtenirVertexIDsDret(); // Totes les cançons

        try {
            // Per cada cançó, obtenim els usuaris que l'han valorada
            for (int i = 0; i < titols.numElem(); i++) {
                String titol = titols.consultar(i);
                TADLlista<String> usuarisQueHanValorat = graf.obtenirAdjacentsDret(titol);
                // Si una cançó no l'ha escoltada ningú, ni entrem
                if (usuarisQueHanValorat.esBuida()) continue;   // // Evitem dividir per zero

                // Calcular la mitjana
                double suma = 0;
                for (int j = 0; j < usuarisQueHanValorat.numElem(); j++) {
                    String username = usuarisQueHanValorat.consultar(j);
                    suma += graf.consultarAresta(username, titol);  // Recollim la nota (el pes)
                }

                double mitjana = suma / usuarisQueHanValorat.numElem();
                if (mitjana > llindar) {    // Supera el tall exigit pel client?
                    comptador++;
                }
            }
        } catch (LlistaBuida | VertexNoTrobat | ArestaNoTrobada e) {
            // Silenciem les excepcions internes durant el recorregut segur
        }

        return comptador;
    }

    /**
     * Retorna els usernames d'aquells usuaris que han escoltat com a mínim
     * una cançó del gènere indicat.
     */
    public TADLlista<String> obtenirUsuarisQueHanEscoltatGenere(String genere) {
        TADLlista<String> resultat = new LlistaDinamica<>();
        TADLlista<String> usuaris = graf.obtenirVertexIDsEsquerra();    // Tots els usuaris

        try {
            for (int i = 0; i < usuaris.numElem(); i++) {
                String username = usuaris.consultar(i);
                TADLlista<String> canconsEscoltes = graf.obtenirAdjacentsEsquerra(username);    // Què ha escoltat?

                boolean teGenere = false;
                for (int j = 0; j < canconsEscoltes.numElem(); j++) {
                    String titol = canconsEscoltes.consultar(j);
                    Canco c = graf.consultarVertexDret(titol);  // Recuperem l'objecte Cançó complet per llegir-ne els detalls

                    if (c.getGenere().equalsIgnoreCase(genere)) {   // Coincideix?
                        teGenere = true;
                        break; // Si ja n'ha escoltat una d'aquest gènere, no cal mirar les restants
                    }
                }

                if (teGenere) {
                    resultat.inserir(username); // L'afegim a la llista d'escollits
                }
            }
        } catch (LlistaBuida | LlistaPlena | VertexNoTrobat e) {
            // Excepcions d'estructures
        }

        return resultat;
    }

    public boolean esBuida() {
        return graf.numVertexEsquerra() == 0 && graf.numVertexDret() == 0;
    }
}
