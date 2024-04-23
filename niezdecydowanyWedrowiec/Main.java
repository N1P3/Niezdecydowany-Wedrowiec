package niezdecydowanyWedrowiec;

import niezdecydowanyWedrowiec.algorytmy.MonteCarlo;
import niezdecydowanyWedrowiec.obiekty.Alejka;
import niezdecydowanyWedrowiec.obiekty.Park;
import niezdecydowanyWedrowiec.obiekty.Skrzyzowanie;

import java.util.*;

import static niezdecydowanyWedrowiec.algorytmy.GaussEliminacja.gausjanskaElimincja;
import static niezdecydowanyWedrowiec.algorytmy.GaussEliminacjaZWyborem.gausjanskaEliminacjaZCzesciowymWyboremElemPodst;
import static niezdecydowanyWedrowiec.algorytmy.GaussSeidel.gausjanskiSeidel;
import static niezdecydowanyWedrowiec.macierze.zbudujMacierz.zbudujMacierz;


public class Main {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        Park park = new Park();
        for (int i = 1; i <= n; i++) {
            Skrzyzowanie skrzyzowanie = new Skrzyzowanie();
            skrzyzowanie.nr = i;
            park.skrzyzowania.put(i, skrzyzowanie);
        }
        List<Alejka> alejki = new ArrayList<>();
        for (int i = 1; i <= m; i++) {
            Alejka alejka = new Alejka();
            alejka.skrzyzowanie1 = scanner.nextInt();
            alejka.skrzyzowanie2 = scanner.nextInt();
            alejka.dlugosc = scanner.nextInt();
            for (Skrzyzowanie skrzyzowanie : park.skrzyzowania.values()) {
                if (alejka.skrzyzowanie1 == skrzyzowanie.nr) {
                    skrzyzowanie.alejkiGraniczace.add(alejka);
                }
                if (alejka.skrzyzowanie2 == skrzyzowanie.nr) {
                    skrzyzowanie.alejkiGraniczace.add(alejka);
                }
            }
            alejki.add(alejka);
        }
        park.alejki = alejki;
        int warunekPetli = scanner.nextInt();
        for (int i = 0; i < warunekPetli; i++) {
            park.studnie.add(scanner.nextInt());
        }
        warunekPetli = scanner.nextInt();
        for (int i = 0; i < warunekPetli; i++) {
            park.wyjscia.add(scanner.nextInt());
        }
        warunekPetli = scanner.nextInt();
        for (int i = 0; i < warunekPetli; i++) {
            park.wedrowcy.put(i, scanner.nextInt());
        }
        double[][] macierz = zbudujMacierz(park);

        for (double[] doubles : macierz) {
            for (double aDouble : doubles) {
                System.out.printf("%.2f", aDouble);
                System.out.print(" ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println("tu macierz wyników: ");

        double[] wyniki = new double[n];
        for (int i = 0; i < wyniki.length; i++) {
            wyniki[i] = 0;
            for (int j : park.wyjscia) {
                if (j - 1 == i) {
                    wyniki[i] = 1;
                    break;
                }
            }
        }
        for (double v : wyniki) {
            System.out.printf("%.2f", v);
            System.out.println();
        }

        System.out.println();
        System.out.println("tu macierz prawdopodobieństw 1: ");

        int iterator = 1;
        double[] prawdopodobienstwoA1 = gausjanskaElimincja(macierz, wyniki);
        for (double v : prawdopodobienstwoA1) {
            System.out.print(iterator + " ");
            System.out.println(v);
            iterator++;
        }
        System.out.println();
        System.out.println("tu macierz prawdopodobieństw 2: ");

        iterator = 1;
        double[] prawdopodobienstwoA2 = gausjanskaEliminacjaZCzesciowymWyboremElemPodst(macierz, wyniki);
        for (double v : prawdopodobienstwoA2) {
            System.out.print(iterator + " ");
            System.out.println(v);
            iterator++;
        }
        System.out.println();
        System.out.println("tu macierz prawdopodobieństw 3: ");

        iterator = 1;
        double[] prawdopodobienstwoA3 = gausjanskiSeidel(macierz, wyniki, 100, 1e-6);
        for (double v : prawdopodobienstwoA3) {
            System.out.print(iterator + " ");
            System.out.println(v);
            iterator++;
        }


        double sukcesy = 0;
        int liczbaProb = 1000000;
        Map<Integer, Integer> wedrowcy = park.wedrowcy;
        List<Integer> klucze = new ArrayList<>(wedrowcy.keySet());
        List<Integer> wartosci = new ArrayList<>();
        for (int i : klucze) {
            wartosci.add(wedrowcy.get(i));
        }
        System.out.println(park.wedrowcy.keySet());
        for (int j : klucze) {
            System.out.println("klucz: " + j + " wartosc: " + wartosci.get(j));

            for (int i = 1; i <= liczbaProb; i++) {
                List<Boolean> zwrot = MonteCarlo.wedrowcaIdzie(park);
                park.wedrowcy = new HashMap<>();
                park.wedrowcy.put(j, wartosci.get(j));
                for (Boolean b : zwrot) {
                    if (b) {
                        sukcesy++;
                    }
                }
            }
            System.out.println("stosunek udanych do nieudanych dla wędrowcy " + j + " : " + sukcesy / (liczbaProb));
            sukcesy = 0;
        }

    }

}
