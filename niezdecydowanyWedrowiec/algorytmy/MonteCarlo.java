package niezdecydowanyWedrowiec.algorytmy;

import niezdecydowanyWedrowiec.obiekty.Alejka;
import niezdecydowanyWedrowiec.obiekty.Park;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonteCarlo {

    public static List<Boolean> wedrowcaIdzie(Park park) {
        List<Boolean> wyniki = new ArrayList<>();
        for (int i : park.wedrowcy.keySet()) {
            boolean petlaDlaWedrowcy = true;
            while (true) {
                for (int studnie : park.studnie) {
                    if (studnie == park.wedrowcy.get(i)) {
                        wyniki.add(false);
                        petlaDlaWedrowcy = false;
                        break;
                    }
                }
                for (int wyjscia : park.wyjscia) {
                    if (wyjscia == park.wedrowcy.get(i)) {
                        wyniki.add(true);
                        petlaDlaWedrowcy = false;
                        break;
                    }
                }
                if (!petlaDlaWedrowcy) {
                    break;
                }
                int nrSkrzyzowania = park.wedrowcy.get(i);
                Random random = new Random();
                int randomNumber = random.nextInt(park.skrzyzowania.get(park.wedrowcy.get(i)).alejkiGraniczace.size());
                Alejka alejka = park.skrzyzowania.get(park.wedrowcy.get(i)).alejkiGraniczace.get(randomNumber);
                int dlugoscAlejki = alejka.dlugosc;
                dlugoscAlejki--;
                while (dlugoscAlejki != 0 && dlugoscAlejki != alejka.dlugosc) {
                    randomNumber = random.nextInt(2);
                    if (randomNumber == 1) {
                        dlugoscAlejki--;
                    } else {
                        dlugoscAlejki++;
                    }
                    if (dlugoscAlejki == alejka.dlugosc) {
                        break;
                    } else if (dlugoscAlejki == 0) {
                        if (alejka.skrzyzowanie1 != nrSkrzyzowania) {
                            park.wedrowcy.put(i, alejka.skrzyzowanie1);
                        } else {
                            park.wedrowcy.put(i, alejka.skrzyzowanie2);
                        }
                        break;
                    }
                }
            }
        }
        return wyniki;
    }

    public static List<Boolean> wedrowcaIdzieAleMaGdziesDlugoscSciezki(Park park) {
        List<Boolean> wyniki = new ArrayList<>();
        for (int i : park.wedrowcy.keySet()) {
            boolean petlaDlaWedrowcy = true;
            while (true) {
                for (int studnie : park.studnie) {
                    if (studnie == park.wedrowcy.get(i)) {
                        wyniki.add(false);
                        petlaDlaWedrowcy = false;
                        break;
                    }
                }
                for (int wyjscia : park.wyjscia) {
                    if (wyjscia == park.wedrowcy.get(i)) {
                        wyniki.add(true);
                        petlaDlaWedrowcy = false;
                        break;
                    }
                }
                if (!petlaDlaWedrowcy) {
                    break;
                }
                int nrSkrzyzowania = park.wedrowcy.get(i);
                Random random = new Random();
                int randomNumber = random.nextInt(park.skrzyzowania.get(park.wedrowcy.get(i)).alejkiGraniczace.size());
                Alejka alejka = park.skrzyzowania.get(park.wedrowcy.get(i)).alejkiGraniczace.get(randomNumber);
                if (alejka.skrzyzowanie1 == nrSkrzyzowania) {
                    park.wedrowcy.put(i, alejka.skrzyzowanie2);
                } else {
                    park.wedrowcy.put(i, alejka.skrzyzowanie1);
                }
            }
        }
        return wyniki;
    }

}
