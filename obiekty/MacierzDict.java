package niezdecydowanyWedrowiec.obiekty;

import java.util.Comparator;
import java.util.HashMap;

public class MacierzDict extends HashMap<Tuple, Double> {

    public MacierzDict(Comparator<Tuple> customComparator) {
    }

    public int liczbaKolumn() {
        return szerokość();
    }

    public int liczbaWierszy() {
        return wysokość();
    }

    public double[][] doTablicy() {
        double[][] wynik = new double[wysokość()][];
        for (int i = 0; i < wysokość(); i++) {
            wynik[i] = new double[szerokość()];
            for (int j = 0; j < szerokość(); j++) {
                Tuple klucz = new Tuple(i, j);
                if (this.containsKey(klucz)) {
                    wynik[i][j] = this.get(klucz);
                } else {
                    wynik[i][j] = 0;
                }
            }
        }
        return wynik;
    }

    public int szerokość() {
        int maksimum = 0;
        for (Tuple klucz : this.keySet()) {
            if (klucz.getWartoscB() > maksimum) {
                maksimum = klucz.getWartoscB();
            }
        }
        return maksimum + 1;
    }

    public int wysokość() {
        int maksimum = 0;
        for (Tuple klucz : this.keySet()) {
            if (klucz.getWartoscA() > maksimum) {
                maksimum = klucz.getWartoscA();
            }
        }
        return maksimum + 1;
    }

    public MacierzDict klonujMacierz() {
        Comparator<Tuple> customComparator = Comparator.comparing(Tuple::getWartoscA)
                .thenComparing(Tuple::getWartoscB);
        MacierzDict wynik = new MacierzDict(customComparator);
        for (Tuple klucz : this.keySet()) {
            wynik.put(klucz, this.get(klucz));
        }
        return wynik;
    }
}
