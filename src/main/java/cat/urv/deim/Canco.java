package cat.urv.deim;

import java.util.Objects;

public class Canco implements Comparable<Canco> {
    private final int id;
    private final String titol;
    private final String artista;
    private final String genere;
    private final int any;
    private int duradaSegons;
    private int popularitat;

    public Canco(int id, String titol, String artista, String genere, int any, int duradaSegons, int popularitat) {
        this.id = id;
        this.titol = titol;
        this.artista = artista;
        this.genere = genere;
        this.any = any;
        this.duradaSegons = duradaSegons;
        this.popularitat = popularitat;
    }

    public int getId() {
        return id;
    }

    public String getTitol() {
        return titol;
    }

    public String getArtista() {
        return artista;
    }

    public String getGenere() {
        return genere;
    }

    public int getAny() {
        return any;
    }

    public int getDuradaSegons() {
        return duradaSegons;
    }

    public int getPopularitat() {
        return popularitat;
    }

    public void setDuradaSegons(int duradaSegons) {
        this.duradaSegons = duradaSegons;
    }

    public void setPopularitat(int popularitat) {
        this.popularitat = popularitat;
    }

    @Override
    public int compareTo(Canco altra) {
        int comparacio = titol.compareTo(altra.titol);
        if (comparacio != 0) {
            return comparacio;
        }
        return Integer.compare(id, altra.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Canco canco)) {
            return false;
        }
        return id == canco.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Canco{id=" + id +
                ", titol='" + titol + '\'' +
                ", artista='" + artista + '\'' +
                ", genere='" + genere + '\'' +
                ", any=" + any +
                ", duradaSegons=" + duradaSegons +
                ", popularitat=" + popularitat +
                '}';
    }
}
