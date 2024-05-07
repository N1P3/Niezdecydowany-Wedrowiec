package niezdecydowanyWedrowiec.obiekty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Park {
    public int skrzyzowania;
    public int liczbaAlejek;
    public List<Alejka> alejki;
    public Map<String, Integer> lokalizacjeSkrzyzowan;
    public List<Integer> lokalizacjeOSK;
    public List<Integer> lokalizacjeWyjsc;
    public List<Integer> lokalizacjeGraczy;
    public List<Integer> lokalizacjeKoszyNasmieci;
    public Map<Integer, List<Integer>> pamiecRuchow = new HashMap<>();

    public static Park wczytajZPliku(String nazwaPliku) {
        try (BufferedReader br = new BufferedReader(new FileReader(nazwaPliku))) {
            String[] pierwszaLinia = br.readLine().split(" ");
            int skrzyzowania = Integer.parseInt(pierwszaLinia[0]);
            int liczbaAlejek = Integer.parseInt(pierwszaLinia[1]);
            int przesuniecie = 0;
            Map<String, Integer> lokalizacjeSkrzyzowan = new HashMap<>();
            List<Alejka> alejki = new ArrayList<>();
            for (int i = 0; i < liczbaAlejek; i++) {
                String[] infoAlejki = br.readLine().split(" ");
                int od = Integer.parseInt(infoAlejki[0]);
                int doo = Integer.parseInt(infoAlejki[1]);
                int dlugosc = Integer.parseInt(infoAlejki[2]);
                List<Integer> pozycjeAlejki = new ArrayList<>();
                if (!lokalizacjeSkrzyzowan.containsKey(Integer.toString(od))) {
                    przesuniecie++;
                    lokalizacjeSkrzyzowan.put(Integer.toString(od), przesuniecie);
                    pozycjeAlejki.add(przesuniecie);
                } else {
                    pozycjeAlejki.add(lokalizacjeSkrzyzowan.get(Integer.toString(od)));
                }

                for (int j = przesuniecie + 1; j < dlugosc + przesuniecie - 1; j++) {
                    pozycjeAlejki.add(j);
                }

                przesuniecie += dlugosc;
                if (!lokalizacjeSkrzyzowan.containsKey(Integer.toString(doo))) {
                    lokalizacjeSkrzyzowan.put(Integer.toString(doo), przesuniecie);
                    pozycjeAlejki.add(przesuniecie);
                } else {
                    pozycjeAlejki.add(lokalizacjeSkrzyzowan.get(Integer.toString(doo)));
                }

                alejki.add(new Alejka(od, doo, przesuniecie, dlugosc, pozycjeAlejki));
            }

            String sprawdzPustaLinie = br.readLine();
            if (sprawdzPustaLinie != null && !sprawdzPustaLinie.isEmpty()) {
                System.out.println("Blad podczas parsowania mapy");
                return null;
            }

            String[] infoOSK = br.readLine().split(" ");
            int liczbaOSK = Integer.parseInt(infoOSK[0]);
            List<Integer> lokalizacjeOSK = new ArrayList<>();
            for (int i = 1; i < liczbaOSK + 1; i++) {
                lokalizacjeOSK.add(lokalizacjeSkrzyzowan.get(infoOSK[i]));
            }

            String[] infoWyjsc = br.readLine().split(" ");
            int liczbaWyjsc = Integer.parseInt(infoWyjsc[0]);
            List<Integer> lokalizacjeWyjsc = new ArrayList<>();
            for (int i = 1; i < liczbaWyjsc + 1; i++) {
                lokalizacjeWyjsc.add(lokalizacjeSkrzyzowan.get(infoWyjsc[i]));
            }

            String[] infoGraczy = br.readLine().split(" ");
            int liczbaGraczy = Integer.parseInt(infoGraczy[0]);
            List<Integer> lokalizacjeGraczy = new ArrayList<>();
            for (int i = 1; i < liczbaGraczy + 1; i++) {
                lokalizacjeGraczy.add(lokalizacjeSkrzyzowan.get(infoGraczy[i]));
            }

            String[] infoKoszyNasmieci = br.readLine().split(" ");
            int liczbaKoszyNasmieci = Integer.parseInt(infoKoszyNasmieci[0]);
            List<Integer> lokalizacjeKoszyNasmieci = new ArrayList<>();
            for (int i = 1; i < liczbaKoszyNasmieci + 1; i++) {
                lokalizacjeKoszyNasmieci.add(lokalizacjeSkrzyzowan.get(infoKoszyNasmieci[i]));
            }

            return new Park(skrzyzowania, alejki, liczbaAlejek, lokalizacjeSkrzyzowan, lokalizacjeOSK, lokalizacjeWyjsc, lokalizacjeGraczy, lokalizacjeKoszyNasmieci);
        } catch (IOException e) {
            System.out.println("Blad podczas parsowania mapy: " + e.getMessage());
            return null;
        }
    }

    public Park(int skrzyzowania, List<Alejka> alejki, int liczbaAlejek, Map<String, Integer> lokalizacjeSkrzyzowan, List<Integer> lokalizacjeOSK, List<Integer> lokalizacjeWyjsc, List<Integer> lokalizacjeGraczy, List<Integer> lokalizacjeKoszyNasmieci) {
        this.skrzyzowania = skrzyzowania;
        this.alejki = alejki;
        this.liczbaAlejek = liczbaAlejek;
        this.lokalizacjeSkrzyzowan = lokalizacjeSkrzyzowan;
        this.lokalizacjeOSK = lokalizacjeOSK;
        this.lokalizacjeWyjsc = lokalizacjeWyjsc;
        this.lokalizacjeGraczy = lokalizacjeGraczy;
        this.lokalizacjeKoszyNasmieci = lokalizacjeKoszyNasmieci;
    }

    public List<Integer> pobierzRuchy(int pozycja) {
        if (pamiecRuchow.containsKey(pozycja))
            return pamiecRuchow.get(pozycja);
        if (czySkrzyzowanie(pozycja)) {
            List<Alejka> polaczoneAlejki = new ArrayList<>();
            for (Alejka alejka : alejki) {
                if (alejka.pozycje.contains(pozycja))
                    polaczoneAlejki.add(alejka);
            }

            if (polaczoneAlejki.size() == 1) {
                if (polaczoneAlejki.get(0).pozycje.indexOf(pozycja) == 0) {
                    List<Integer> wynik = new ArrayList<>();
                    wynik.add(polaczoneAlejki.get(0).pozycje.get(1));
                    pamiecRuchow.put(pozycja, wynik);
                    return wynik;
                } else {
                    List<Integer> wynik = new ArrayList<>();
                    wynik.add(polaczoneAlejki.get(0).pozycje.get(polaczoneAlejki.get(0).pozycje.size() - 2));
                    pamiecRuchow.put(pozycja, wynik);
                    return wynik;
                }
            } else {
                for (Map.Entry<String, Integer> lokalizacja : lokalizacjeSkrzyzowan.entrySet()) {
                    if (lokalizacja.getValue() == pozycja) {
                        List<Integer> ruchy = new ArrayList<>();
                        for (Alejka alejka : polaczoneAlejki) {
                            if (Integer.toString(alejka.odSkrzyzowania).equals(lokalizacja.getKey()))
                                ruchy.add(alejka.pozycje.get(1));
                            else
                                ruchy.add(alejka.pozycje.get(alejka.pozycje.size() - 2));
                        }
                        pamiecRuchow.put(pozycja, ruchy);
                        return ruchy;
                    }
                }
            }
        } else {
            for (Alejka alejka : alejki) {
                if (alejka.pozycje.contains(pozycja)) {
                    int indeksPozycji = alejka.pozycje.indexOf(pozycja);
                    List<Integer> wynik = new ArrayList<>();
                    wynik.add(alejka.pozycje.get(indeksPozycji - 1));
                    wynik.add(alejka.pozycje.get(indeksPozycji + 1));
                    pamiecRuchow.put(pozycja, wynik);
                    return wynik;
                }
            }
        }

        return new ArrayList<>();
    }

    public double[] pobierzPrawdopodobienstwaRuchow(List<Integer> ruchy) {
        double[] prawdopodobienstwa = new double[ruchy.size()];
        int liczbaKoszyNasmieci = (int) ruchy.stream().filter(lokalizacjeKoszyNasmieci::contains).count();
        if (liczbaKoszyNasmieci == 0) {
            double prawdopodobienstwo = 1.0 / ruchy.size();
            for (Integer ruch : ruchy) {
                prawdopodobienstwa[ruchy.indexOf(ruch)] = prawdopodobienstwo;
            }
        } else {
            double prawdopodobienstwo = 1.0 / (ruchy.size() * 2 - liczbaKoszyNasmieci);
            for (Integer ruch : ruchy) {
                if (lokalizacjeKoszyNasmieci.contains(ruch))
                    prawdopodobienstwa[ruchy.indexOf(ruch)] = prawdopodobienstwo;
                else
                    prawdopodobienstwa[ruchy.indexOf(ruch)] = prawdopodobienstwo * 2;
            }
        }

        return prawdopodobienstwa;
    }

    public MacierzDict pobierzRzadkaMacierz() {
        int maksymalnaPozycja = alejki.stream().mapToInt(a -> a.pozycje.stream().max(Integer::compareTo).orElse(0)).max().orElse(0) + 1;
        Comparator<Tuple> customComparator = Comparator.comparing(Tuple::getWartoscA)
                .thenComparing(Tuple::getWartoscB);
        MacierzDict macierz = new MacierzDict(customComparator);
        for (int i = 0; i < maksymalnaPozycja; i++) {
            for (int j = 0; j < maksymalnaPozycja; j++) {
                if (i == j) {
                    macierz.put(new Tuple(i, j), 1.0);
                    if (lokalizacjeOSK.contains(i) || lokalizacjeWyjsc.contains(i))
                        continue;
                    List<Integer> ruchy = pobierzRuchy(i);
                    if (!ruchy.isEmpty()) {
                        double[] prawdopodobienstwa = pobierzPrawdopodobienstwaRuchow(ruchy);
                        for (Integer ruch : ruchy) {
                            if (macierz.containsKey(new Tuple(i, ruch))) {
                                macierz.put(new Tuple(i, ruch), macierz.get(new Tuple(i, ruch)) + prawdopodobienstwa[ruchy.indexOf(ruch)] * -1);
                            } else {
                                macierz.put(new Tuple(i, ruch), prawdopodobienstwa[ruchy.indexOf(ruch)] * -1);
                            }
                        }
                    }
                }
            }
        }

        return macierz;
    }

    public double[] pobierzWektorRozwiazania() {
        int maksymalnaPozycja = alejki.stream().mapToInt(a -> a.pozycje.stream().max(Integer::compareTo).orElse(0)).max().orElse(0) + 1;
        double[] b = new double[maksymalnaPozycja];
        for (int i = 0; i < maksymalnaPozycja; i++) {
            if (lokalizacjeWyjsc.contains(i)) {
                b[i] = 1;
            }
        }

        return b;
    }

    public static String generujLosowaMape(int liczbaAlejek, int skrzyzowania, int maksymalnaDlugosc, boolean brakDuplikatowAlejek, boolean stalaDlugosc) {
        Random losowy = new Random();
        StringBuilder sb = new StringBuilder();

        sb.append(skrzyzowania).append(" ").append(liczbaAlejek).append("\n");

        List<Integer> uzyteSkrzyzowania = new ArrayList<>();
        List<List<Tuple>> uzyteAlejki = new ArrayList<>();

        for (int i = 0; i < liczbaAlejek; i++) {
            int od = losowy.nextInt(1, skrzyzowania - 1);
            int doo = losowy.nextInt(od + 1, skrzyzowania);
            uzyteSkrzyzowania.add(od);
            uzyteSkrzyzowania.add(doo);
            if (brakDuplikatowAlejek) {
                List<Tuple> listaPom = new ArrayList<>();
                listaPom.add(new Tuple(od, doo));
                while (uzyteAlejki.contains(listaPom)) {
                    od = losowy.nextInt(1, skrzyzowania - 1);
                    doo = losowy.nextInt(od + 1, skrzyzowania);
                }
            }
            List<Tuple> listaPom = new ArrayList<>();
            listaPom.add(new Tuple(od, doo));
            uzyteAlejki.add(listaPom);

            int dlugosc = stalaDlugosc ? maksymalnaDlugosc : losowy.nextInt(3, maksymalnaDlugosc);
            sb.append(od).append(" ").append(doo).append(" ").append(dlugosc).append("\n");
        }

        sb.append("\n");
        int osk = losowy.nextInt(1, uzyteSkrzyzowania.size());
        sb.append("1 ").append(uzyteSkrzyzowania.get(osk)).append("\n");

        int wyjscie = losowy.nextInt(1, uzyteSkrzyzowania.size());
        while (uzyteSkrzyzowania.get(wyjscie) == uzyteSkrzyzowania.get(osk)) {
            wyjscie = losowy.nextInt(1, uzyteSkrzyzowania.size());
        }
        sb.append("1 ").append(uzyteSkrzyzowania.get(wyjscie)).append("\n");

        int gracz = losowy.nextInt(1, uzyteSkrzyzowania.size());
        while (uzyteSkrzyzowania.get(gracz) == uzyteSkrzyzowania.get(osk) || uzyteSkrzyzowania.get(gracz) == uzyteSkrzyzowania.get(wyjscie)) {
            gracz = losowy.nextInt(1, uzyteSkrzyzowania.size());
        }
        sb.append("1 ").append(uzyteSkrzyzowania.get(gracz)).append("\n");

        int koszNasmieci = losowy.nextInt(1, uzyteSkrzyzowania.size());
        sb.append("1 ").append(uzyteSkrzyzowania.get(koszNasmieci));

        return sb.toString();
    }

    private boolean czySkrzyzowanie(int pozycja) {
        for (Integer lokalizacja : lokalizacjeSkrzyzowan.values()) {
            if (lokalizacja == pozycja)
                return true;
        }

        return false;
    }

    private boolean czyZaulek(int pozycja) {
        for (Alejka alejka : alejki) {
            if (alejka.pozycje.contains(pozycja))
                return true;
        }
        return false;
    }

    private static double[] stworzTabliceZer(int dlugosc) {
        double[] tablica = new double[dlugosc];
        Arrays.fill(tablica, 0);
        return tablica;
    }

}
