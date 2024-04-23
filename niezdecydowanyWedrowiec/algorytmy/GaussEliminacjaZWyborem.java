package niezdecydowanyWedrowiec.algorytmy;

public class GaussEliminacjaZWyborem {

    public static double[] gausjanskaEliminacjaZCzesciowymWyboremElemPodst(double[][] A, double[] b) {
        int n = A.length;
        for (int k = 0; k < n; k++) {
            int maxIdx = k;
            for (int i = k + 1; i < n; i++) {
                if (Math.abs(A[i][k]) > Math.abs(A[maxIdx][k])) {
                    maxIdx = i;
                }
            }
            double[] tempA = A[k];
            A[k] = A[maxIdx];
            A[maxIdx] = tempA;
            double tempB = b[k];
            b[k] = b[maxIdx];
            b[maxIdx] = tempB;

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
