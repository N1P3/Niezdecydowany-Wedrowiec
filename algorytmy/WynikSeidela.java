package niezdecydowanyWedrowiec.algorytmy;

public class WynikSeidela {
    public double[] wynik;
    public int iteracje;
    public boolean czyZbiezne;

    public WynikSeidela(double[] wynik, int iteracje, boolean czyZbiezne) {
        this.wynik = wynik;
        this.iteracje = iteracje;
        this.czyZbiezne = czyZbiezne;
    }
}