package niezdecydowanyWedrowiec.algorytmy;

public class GaussEliminacja {

    public static double[] gausjanskaElimincja(double[][] A, double[] b) {
        int n = A.length;
        for (int k = 0; k < n; k++) {
            for (int i = k + 1; i < n; i++) {
                double factor = A[i][k] / A[k][k];
                for (int j = k; j < n; j++) {
                    A[i][j] -= factor * A[k][j];
                }
                b[i] -= factor * b[k];
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = b[i] / A[i][i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= A[i][j] * x[j] / A[i][i];
            }
        }
        for (int i = 0; i < x.length; i++) {
            if (x[i] < 0) {
                x[i] = x[i] * -1;
            }
        }

        return x;
    }

}
