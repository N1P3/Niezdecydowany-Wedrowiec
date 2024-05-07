package niezdecydowanyWedrowiec.obiekty;

import java.util.List;

public class Alejka {

    public Alejka(int odSkrzyzowania, int doSkrzyzowania, int przesuniecie, int dlugosc, List<Integer> pozycje) {
        this.odSkrzyzowania = odSkrzyzowania;
        this.doSkrzyzowania = doSkrzyzowania;
        this.przesuniecie = przesuniecie;
        this.dlugosc = dlugosc;
        this.pozycje = pozycje;
    }

    public int getOdSkrzyzowania() {
        return odSkrzyzowania;
    }

    public void setOdSkrzyzowania(int odSkrzyzowania) {
        this.odSkrzyzowania = odSkrzyzowania;
    }

    public int odSkrzyzowania;

    public int getDoSkrzyzowania() {
        return doSkrzyzowania;
    }

    public void setDoSkrzyzowania(int doSkrzyzowania) {
        this.doSkrzyzowania = doSkrzyzowania;
    }

    public int doSkrzyzowania;

    public int getPrzesuniecie() {
        return przesuniecie;
    }

    public void setPrzesuniecie(int przesuniecie) {
        this.przesuniecie = przesuniecie;
    }

    public int przesuniecie;

    public int getDlugosc() {
        return dlugosc;
    }

    public void setDlugosc(int dlugosc) {
        this.dlugosc = dlugosc;
    }

    public int dlugosc;

    public List<Integer> getPozycje() {
        return pozycje;
    }

    public void setPozycje(List<Integer> pozycje) {
        this.pozycje = pozycje;
    }

    public List<Integer> pozycje;
}