package niezdecydowanyWedrowiec.algorytmy;

import niezdecydowanyWedrowiec.obiekty.MacierzDict;
import niezdecydowanyWedrowiec.obiekty.Park;
import niezdecydowanyWedrowiec.obiekty.Tuple;

import java.util.*;
import java.util.stream.Collectors;

public class Algorytmy {
    public static double[] GaussZWyborem(MacierzDict macierz, double[] b, Park park) {
        int m = macierz.liczbaKolumn();
        int n = macierz.liczbaWierszy();

        for (int i = 0; i < n; i++) {
            int finalI = i;
            List<Map.Entry<Tuple, Double>> kolumna = macierz.entrySet().stream()
                    .filter(entry -> entry.getKey().getWartoscB() == finalI && entry.getKey().getWartoscA() >= finalI).toList();

            if (kolumna.isEmpty()) {
                continue;
            }
            Optional<Map.Entry<Tuple, Double>> maxEntry = kolumna.stream()
                    .max(Comparator.comparingDouble(entry -> Math.abs(entry.getValue())));

            int maxRowIndex = maxEntry.map(entry -> entry.getKey().getWartoscA()).orElse(-1);

            if (maxRowIndex != i) {
                for (int j = 0; j < m; j++) {
                    if (macierz.containsKey(new Tuple(i, j))) {
                        if (macierz.containsKey(new Tuple(maxRowIndex, j))) {
                            var temp = macierz.get(new Tuple(i, j));
                            macierz.put(new Tuple(i, j), macierz.get(new Tuple(maxRowIndex, j)));
                            macierz.put(new Tuple(maxRowIndex, j), temp);
                        } else {
                            macierz.put(new Tuple(maxRowIndex, j), macierz.get(new Tuple(i, j)));
                            macierz.remove(new Tuple(i, j));
                        }
                    } else if (macierz.containsKey(new Tuple(maxRowIndex, j))) {
                        macierz.put(new Tuple(i, j), macierz.get(new Tuple(maxRowIndex, j)));
                        macierz.remove(new Tuple(maxRowIndex, j));
                    }
                }
                double temp = b[i];
                b[i] = b[maxRowIndex];
                b[maxRowIndex] = temp;
            }

            for (int k = i + 1; k < n; k++) {
                var pivotIndex = new Tuple(i, i);
                var currentValueToEliminateIndex = new Tuple(k, i);
                if (!macierz.containsKey(pivotIndex))
                    continue;
                if (!macierz.containsKey(currentValueToEliminateIndex))
                    continue;

                double c = macierz.get(currentValueToEliminateIndex) / macierz.get(pivotIndex);

                for (int j = i; j < m; j++) {
                    var indexKJ = new Tuple(k, j);
                    var indexIJ = new Tuple(i, j);
                    if (j == i) {
                        macierz.remove(indexKJ);
                        continue;
                    }

                    if (macierz.containsKey(indexIJ)) {
                        if (macierz.containsKey(indexKJ)) {
                            macierz.put(indexKJ, macierz.get(indexKJ) - c * macierz.get(indexIJ));
                        } else {
                            macierz.put(indexKJ, -c * macierz.get(indexIJ));
                        }
                    }
                }
                b[k] -= c * b[i];
            }
        }

        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            var keyII = new Tuple(i, i);
            if (macierz.containsKey(keyII) && macierz.get(keyII) != 0) {
                solution[i] = b[i] / macierz.get(keyII);
                for (int k = i - 1; k >= 0; k--) {
                    if (macierz.containsKey(new Tuple(k, i))) {
                        b[k] -= macierz.get(new Tuple(k, i)) * solution[i];
                    }
                }
            }
        }
        return solution;
    }

    public static WynikSeidela seidel(MacierzDict A, double[] b, Park park, double tolerance, int maxIterations) {
        int n = b.length;
        double[] x = new double[n];
        double[] x_prev = new double[n];
        int iterations = 0;

        while (iterations < maxIterations) {
            System.arraycopy(x, 0, x_prev, 0, n);

            for (int i = 0; i < n; i++) {
                double sigma = 0.0;

                if (!park.lokalizacjeOSK.contains(i) && !park.lokalizacjeWyjsc.contains(i)) {
                    List<Integer> moves = park.pamiecRuchow.getOrDefault(i, new ArrayList<Integer>());
                    for (Integer move : moves) {
                        var key = new Tuple(i, move);
                        if (A.containsKey(key)) {
                            sigma += A.get(key) * x[move];
                        }
                    }
                }

                x[i] = (b[i] - sigma) / A.get(new Tuple(i, i));
            }

            double maxDiff = 0.0;
            for (int i = 0; i < n; i++) {
                maxDiff = Math.max(maxDiff, Math.abs(x[i] - x_prev[i]));
            }

            if (maxDiff < tolerance) {
                return new WynikSeidela(x, iterations, true);
            }

            iterations++;
        }

        return new WynikSeidela(x, iterations, false);
    }

    public static double[] eliminacjaGaussa(MacierzDict matrix, double[] b) {
        int n = matrix.liczbaWierszy();
        double[] solution = new double[n];
        System.arraycopy(b, 0, solution, 0, n);

        for (int i = 1; i < n; i++) {
            Tuple key = new Tuple(i, i);
            for (int j = i + 1; j <= n; j++) {
                double divisor = matrix.get(key);
                if (divisor == 0.0) {
                    throw new IllegalArgumentException("Dzielenie przez zero");
                }
                var keyJ = new Tuple(j, i);
                if (!matrix.containsKey(keyJ)) {
                    continue;
                }
                double factor = matrix.get(keyJ) / divisor;
                for (int k = i; k <= n + 1; k++) {
                    var keyJK = new Tuple(j, k);
                    var keyIK = new Tuple(i, k);
                    double val = matrix.getOrDefault(keyJK, 0.0);
                    double val2 = matrix.getOrDefault(keyIK, 0.0);
                    val -= factor * val2;
                    if (val != 0) {
                        matrix.put(keyJK, val);
                    }
                }
                solution[j] -= factor * solution[i];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j <= n; j++) {
                var keyIJ = new Tuple(i, j);
                if (matrix.containsKey(keyIJ) && x[j] != 0.0) {
                    sum += matrix.get(keyIJ) * x[j];
                }
            }
            var key2 = new Tuple(i, i);
            if (matrix.containsKey(key2)) {
                double diagonalValue = matrix.get(key2);
                if (diagonalValue == 0.0) {
                    throw new IllegalArgumentException("Dzielenie przez zero");
                }
                x[i] = (solution[i] - sum) / diagonalValue;
            }
        }
        return x;
    }

    public static int GetRandomWithProbability(double[] probabilities) {
        double random = new Random().nextDouble();
        double sum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            sum += probabilities[i];
            if (random < sum)
                return i;
        }

        return probabilities.length - 1;
    }

    public static double MonteCarlo(Park park, int n) {
        Random random = new Random();
        int successes = 0;
        for (int i = 0; i < n; i++) {
            int currentLocation = park.lokalizacjeGraczy.get(0);

            while (!park.lokalizacjeWyjsc.contains(currentLocation) && !park.lokalizacjeOSK.contains(currentLocation)) {
                List<Integer> moves = park.pobierzRuchy(currentLocation);
                if (moves.isEmpty())
                    break;
                double[] probabilities = park.pobierzPrawdopodobienstwaRuchow(moves);
                currentLocation = moves.get(GetRandomWithProbability(probabilities));
            }

            if (park.lokalizacjeWyjsc.contains(currentLocation))
                successes++;
        }

        return (double) successes / n;
    }
}

