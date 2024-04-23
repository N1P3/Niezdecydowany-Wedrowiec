package niezdecydowanyWedrowiec.macierze;

import niezdecydowanyWedrowiec.obiekty.Alejka;
import niezdecydowanyWedrowiec.obiekty.Park;

public class zbudujMacierz {

    public static double[][] zbudujMacierz(Park park) {
        double[][] macierz = new double[park.skrzyzowania.keySet().size()][park.skrzyzowania.keySet().size()];
        System.out.println(park.skrzyzowania.keySet().size());
        for (int i = 1; i <= park.skrzyzowania.keySet().size(); i++) {
            for (int j = 1; j <= park.skrzyzowania.keySet().size(); j++) {
                if (i == j) {
                    macierz[i - 1][j - 1] = 1.0;
                } else {
                    boolean czyPomijacWierszBoKoniec = false;
                    for (int wyjscie : park.wyjscia) {
                        if (wyjscie == i) {
                            czyPomijacWierszBoKoniec = true;
                            break;
                        }
                    }
                    for (int studnie : park.studnie) {
                        if (studnie == i) {
                            czyPomijacWierszBoKoniec = true;
                            break;
                        }
                    }
                    if (!czyPomijacWierszBoKoniec) {
                        double sumaDlugosci = 0;
                        for (Alejka alejka : park.skrzyzowania.get(i).alejkiGraniczace) {
                            sumaDlugosci += alejka.dlugosc;
                        }
                        for (Alejka alejka : park.skrzyzowania.get(i).alejkiGraniczace) {
                            if (alejka.skrzyzowanie1 == i) {
                                if (macierz[i - 1][alejka.skrzyzowanie2 - 1] != 1) {
                                    macierz[i - 1][alejka.skrzyzowanie2 - 1] = alejka.dlugosc *-1/ sumaDlugosci;
                                }
                            } else {
                                if (macierz[i - 1][alejka.skrzyzowanie1 - 1] != 1) {
                                    macierz[i - 1][alejka.skrzyzowanie1 - 1] = alejka.dlugosc *-1/ sumaDlugosci;
                                }
                            }
                        }
                    } else {
                        macierz[i - 1][j - 1] = 0;
                    }
                }
            }
        }
        return macierz;
    }

}
