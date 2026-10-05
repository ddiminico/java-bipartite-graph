package cat.urv.deim;

import java.util.Objects;

public class Usuari implements Comparable<Usuari> {
    private final int id;
    private final String nomUsuari;
    private final String nom;
    private final String pais;
    private int edat;

    public Usuari(int id, String nomUsuari, String nom, String pais, int edat) {
        this.id = id;
        this.nomUsuari = nomUsuari;
        this.nom = nom;
        this.pais = pais;
        this.edat = edat;
    }

    public int getId() {
        return id;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public String getNom() {
        return nom;
    }

    public String getPais() {
        return pais;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    @Override
    public int compareTo(Usuari altre) {
        int comparacio = nomUsuari.compareTo(altre.nomUsuari);
        if (comparacio != 0) {
            return comparacio;
        }
        return Integer.compare(id, altre.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuari usuari)) {
            return false;
        }
        return id == usuari.id && Objects.equals(nomUsuari, usuari.nomUsuari);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomUsuari);
    }

    @Override
    public String toString() {
        return "Usuari{id=" + id +
                ", nomUsuari='" + nomUsuari + '\'' +
                ", nom='" + nom + '\'' +
                ", pais='" + pais + '\'' +
                ", edat=" + edat +
                '}';
    }
}
