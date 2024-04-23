package niezdecydowanyWedrowiec.obiekty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Park {
   public List<Alejka> alejki = new ArrayList<>();

    //tutaj pierwsze to to samo co nr w skrzyzowaniu, zrobione zeby sie szybciej pisało kod
   public Map<Integer, Skrzyzowanie> skrzyzowania = new HashMap<>();
   public List<Integer> wyjscia = new ArrayList<>();
   public List<Integer> studnie = new ArrayList<>();

   //przy wedrowcy mamy pierwsze to jest id a drugie to skrzyzowanie na ktorym sie znajduje
   public Map<Integer, Integer> wedrowcy = new HashMap<>();
}
