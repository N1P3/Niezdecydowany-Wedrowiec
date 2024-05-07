package niezdecydowanyWedrowiec;

import niezdecydowanyWedrowiec.algorytmy.Algorytmy;
import niezdecydowanyWedrowiec.algorytmy.WynikSeidela;
import niezdecydowanyWedrowiec.obiekty.MacierzDict;
import niezdecydowanyWedrowiec.obiekty.Park;
import niezdecydowanyWedrowiec.obiekty.Tuple;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void testH3() throws IOException {
        List<Integer> rozmiaryMacierzy = new ArrayList<>();
        List<Double> czasyGaussa = new ArrayList<>();
        List<Double> czasyGaussaZWyborem = new ArrayList<>();
        List<Double> czasySeidela = new ArrayList<>();

        for (int i = 10; i <= 100; i += 5) {
            Park park = Park.wczytajZPliku("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\parki\\park1.txt");
            if (park == null)
                return;
            MacierzDict macierz = park.pobierzRzadkaMacierz();
            rozmiaryMacierzy.add(macierz.liczbaWierszy());
            System.out.println(i);

            long czas = System.nanoTime();
            double[] rozwiązanie = Algorytmy.eliminacjaGaussa(macierz.klonujMacierz(), park.pobierzWektorRozwiazania());
            czasyGaussa.add((double) (System.nanoTime() - czas));
            double błąd = obliczBłąd(rozwiązanie, park, true);
            System.out.println("Czas eliminacji Gaussa: " + (System.nanoTime() - czas) + " nanosekund");
            System.out.println("Błąd eliminacji Gaussa: " + błąd);

            czas = System.nanoTime();
            double[] rozwiązanie2 = Algorytmy.GaussZWyborem(macierz.klonujMacierz(), park.pobierzWektorRozwiazania(), park);
            czasyGaussaZWyborem.add((double) (System.nanoTime() - czas));
            double błąd2 = obliczBłąd(rozwiązanie2, park, true);
            System.out.println("Czas eliminacji Gaussa z wyborem: " + (System.nanoTime() - czas) + " nanosekund");
            System.out.println("Błąd eliminacji Gaussa z wyborem: " + błąd2);

            czas = System.nanoTime();
            WynikSeidela wynikSeidela = Algorytmy.seidel(macierz.klonujMacierz(), park.pobierzWektorRozwiazania(), park, 1e-10, 100000);
            czasySeidela.add((double) (System.nanoTime() - czas));
            double błąd3 = obliczBłąd(wynikSeidela.wynik, park, true);
            System.out.println("Czas metody Seidela: " + (System.nanoTime() - czas) + " nanosekund");
            System.out.println("Błąd metody Seidela: " + błąd3);

            System.out.println("\n");
        }
        zapiszWynik(czasyGaussa, rozmiaryMacierzy, "CzasyGaussa", "C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki");
        zapiszWynik(czasyGaussaZWyborem, rozmiaryMacierzy, "CzasyGaussaZWyborem", "C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki");
        zapiszWynik(czasySeidela, rozmiaryMacierzy, "CzasySeidela", "C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki");
    }

    public static void zapiszWynik(List<Double> czasy, List<Integer> wierszRozmiarów, String nazwa, String katalogProjektu) {
        try {
            FileWriter writer = new FileWriter(katalogProjektu + "/" + nazwa + ".csv");
            StringBuilder sb = new StringBuilder();
            for (Double czas : czasy) {
                sb.append(czas.toString());
                sb.append(" , ");
            }
            sb.append("\nRozmiar wiersza macierzy :, ");
            for (Integer rozmiar : wierszRozmiarów) {
                sb.append(rozmiar.toString());
                sb.append(" , ");
            }
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double obliczBłąd(double[] rozwiązanie, Park park, boolean kumulacyjny) {
        double[] b = park.pobierzWektorRozwiazania();
        double[] x = pomnóżMacierzPrzezWektor(park.pobierzRzadkaMacierz(), rozwiązanie);

        System.out.println("Tablica:");
        for (int i = 0; i < b.length; i++) {
            System.out.print(b[i] + " ");
        }
        System.out.println();

        System.out.println("Tablica:");
        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();

        List<Double> błędy = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            błędy.add(Math.abs(x[i] - b[i]));
        }

        if (kumulacyjny) {
            return błędy.stream().mapToDouble(Double::doubleValue).sum();
        }
        return błędy.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    public static double[] pomnóżMacierzPrzezWektor(MacierzDict matrix, double[] vector) {
        int n = matrix.liczbaKolumn();
        int m = vector.length;
        if (m != n) {
            throw new IllegalArgumentException("Rozmiary macierzy i wektora nie pasują");
        }
        double[] wynik = new double[n];

        for (int i = 0; i < n; i++) {
            List<Double> wiersz = new ArrayList<>();
            for (var wpis : matrix.entrySet()) {
                Tuple klucz = wpis.getKey();
                if (klucz.getWartoscA() == (i)) {
                    wiersz.add(wpis.getValue() * vector[klucz.getWartoscB()]);
                }
            }
            wynik[i] = wiersz.stream().mapToDouble(Double::doubleValue).sum();
        }

        return wynik;
    }

    public static void zapiszTestH2(String nazwa, String s) {
        try {
            FileWriter writer = new FileWriter(nazwa);
            writer.write(s);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testH2_1() throws IOException {
        List<Integer> rozmiaryMacierzy = new ArrayList<>();
        List<Tuple> wynikiSeidelaKonwergencja = new ArrayList<>();

        for (int i = 10; i < 50; i++) {
            for (int j = 10; j < 50; j++) {
                String s = Park.generujLosowaMape(i, j, 10, false, false);
                zapiszTestH2("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki\\test21.csv", s);
                Park park = Park.wczytajZPliku("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki\\test21.csv");



                if (park == null)
                    return;
                MacierzDict macierz = park.pobierzRzadkaMacierz();
                rozmiaryMacierzy.add(macierz.liczbaWierszy());
                System.out.println(i);

                long czas = System.nanoTime();
                WynikSeidela wynikSeidela = Algorytmy.seidel(macierz.klonujMacierz(), park.pobierzWektorRozwiazania(), park, 1e-5, 500);
                System.out.println("Czas metody Seidela: " + (System.nanoTime() - czas) + " nanosekund");
                if (wynikSeidela.czyZbiezne) {
                    double błąd = obliczBłąd(wynikSeidela.wynik, park, true);
                    wynikiSeidelaKonwergencja.add(new Tuple(i, j));
                    System.out.println("Błąd: " + błąd);
                    System.out.println("Iteracje: " + wynikSeidela.iteracje);
                } else {
                    System.out.println("Metoda Seidela nie jest zbieżna do rozwiązania");
                    //tutaj dodajemy macierz do pliku dla której seidel się wywalił
                    zapiszTestH2("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki\\MacierzDlaKtorejSeidelNieDziala.csv", s);
                }
            }
            System.out.println("\n");
        }
//        zapiszWynikSeidelaKonwergencja(wynikiSeidelaKonwergencja, rozmiaryMacierzy);
    }

    public static void zapiszWynikSeidelaKonwergencja(List<Integer> alley, List<Integer> intersection) {
        try {
            FileWriter writer = new FileWriter("Wyniki/SeidelConvergence.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("alley,");
            for (Integer a : alley) {
                sb.append(a.toString());
                sb.append(",");
            }
            sb.append("\nintersection,");
            for (Integer i : intersection) {
                sb.append(i.toString());
                sb.append(",");
            }
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testH2_2() throws IOException {
        List<Integer> rozmiaryMacierzy = new ArrayList<>();
        List<WynikSeidela> wynikiSeidela = new ArrayList<>();

        for (int i = 10; i <= 50; i += 10) {
            Park park = Park.wczytajZPliku("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\parki\\park1.txt");

            if (park == null)
                return;
            MacierzDict macierz = park.pobierzRzadkaMacierz();
            rozmiaryMacierzy.add(i);

            System.out.println(i);

            long czas = System.nanoTime();
            WynikSeidela wynikSeidela = Algorytmy.seidel(macierz.klonujMacierz(), park.pobierzWektorRozwiazania(), park, 1e-17, i * 1000);
            System.out.println("Czas metody Seidela: " + (System.nanoTime() - czas) + " nanosekund");
            if (wynikSeidela.czyZbiezne) {
                wynikiSeidela.add(wynikSeidela);
                System.out.println("Zbieżność po iteracjach: " + wynikSeidela.iteracje);
            } else {
                System.out.println("Metoda Seidela nie zbiega");
            }
            System.out.println("\n");
        }
        zapiszWynikIteracjiSeidela(wynikiSeidela, rozmiaryMacierzy);
    }

    public static void zapiszWynikIteracjiSeidela(List<WynikSeidela> wynikiSeidela, List<Integer> rozmiaryMacierzy) {
        try {
            FileWriter writer = new FileWriter("Wyniki/SeidelIterations.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("size,");
            for (Integer rozmiar : rozmiaryMacierzy) {
                sb.append(rozmiar.toString());
                sb.append(",");
            }
            sb.append("\niterations,");
            for (WynikSeidela wynikSeidela : wynikiSeidela) {
                sb.append(wynikSeidela.wynik.toString());
                sb.append(",");
            }
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void testH1() {
        List<Integer> rozmiaryMacierzy = new ArrayList<>();
        List<Double> wynikiEliminacjiGaussa = new ArrayList<>();
        List<Double> wynikiGaussaZWyborem = new ArrayList<>();

        for (int i = 10; i < 2000; i *= 2) {
            Park park = Park.wczytajZPliku("C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\parki\\park1.txt");
            if (park == null)
                return;
            MacierzDict macierz = park.pobierzRzadkaMacierz();
            rozmiaryMacierzy.add(macierz.liczbaWierszy());
            System.out.println(i);

            long czas = System.currentTimeMillis();
            double[] rozwiązanie = Algorytmy.eliminacjaGaussa(macierz.klonujMacierz(), park.pobierzWektorRozwiazania());
            double błąd = obliczBłąd(rozwiązanie, park, true);
            wynikiEliminacjiGaussa.add(błąd);
            System.out.println("Eliminacja Gaussa: " + rozwiązanie[park.lokalizacjeGraczy.get(0)]);
            System.out.println("Błąd: " + błąd);

            czas = System.currentTimeMillis();
            double[] rozwiązanie2 = Algorytmy.GaussZWyborem(macierz.klonujMacierz(), park.pobierzWektorRozwiazania(), park);
            double błąd2 = obliczBłąd(rozwiązanie2, park, true);
            wynikiGaussaZWyborem.add(błąd2);
            System.out.println("Eliminacja Gaussa z wyborem: " + rozwiązanie2[park.lokalizacjeGraczy.get(0)]);
            System.out.println("Błąd: " + błąd2);

            System.out.println("\n");
        }
        zapiszWynik(wynikiEliminacjiGaussa, rozmiaryMacierzy, "WynikiEliminacjiGaussa", "C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki");
        zapiszWynik(wynikiGaussaZWyborem, rozmiaryMacierzy, "WynikiGaussaZWyborem", "C:\\Users\\c4xpl\\IdeaProjects\\algoDoKusziegoProjekt2\\src\\niezdecydowanyWedrowiec\\wyniki");
    }

    public static void main(String[] args) throws IOException {


        testH1();
        testH2_1();
        testH2_2();
        testH3();
    }
}
