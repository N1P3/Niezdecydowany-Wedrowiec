package niezdecydowanyWedrowiec.algorytmy;

public class GaussSeidel {

    public static double[] gausjanskiSeidel(double[][] A, double[] b, int maxIterations, double tolerance) {
        int n = A.length;
        double[] x = new double[n];
        double[] xPrev = new double[n];
        for (int iter = 0; iter < maxIterations; iter++) {
            System.arraycopy(x, 0, xPrev, 0, n);
            for (int i = 0; i < n; i++) {
                double sum = b[i];
                for (int j = 0; j < n; j++) {
                    if (j != i) {
                        sum -= A[i][j] * x[j];
                    }
                }
                x[i] = sum / A[i][i];
            }
            if (converged(x, xPrev, tolerance)) {
                break;
            }
        }
        for (int i = 0; i < x.length; i++) {
            if (x[i] < 0) {
                x[i] = x[i] * -1;
            }
        }
        return x;
    }

    private static boolean converged(double[] x, double[] xPrev, double tolerance) {
        for (int i = 0; i < x.length; i++) {
            if (Math.abs(x[i] - xPrev[i]) > tolerance) {
                return false;
            }
        }
        return true;
    }

}
