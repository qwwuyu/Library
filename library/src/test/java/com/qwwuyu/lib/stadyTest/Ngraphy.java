package com.qwwuyu.lib.stadyTest;

public class Ngraphy {
    public static void printIS(int n) {
        printIS(getResultArray(n));
    }

    public static int[][] getResultArray(int n) {
        if (n == 1) {
            return new int[][]{{1}};
        } else if (n == 2) {
            return null;
        }
        int[][] resultArray;
        int[][] array1 = new int[n][n];
        int[][] array2 = new int[n][n];
        if (n % 2 == 1) {
            meshGrid(n, array1, array2);
            resultArray = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int a = mod(array1[i][j] + array2[i][j] - (n + 3) / 2, n);
                    int b = mod(array1[i][j] + 2 * array2[i][j] - 2, n);
                    resultArray[i][j] = n * a + b + 1;
                }
            }
        } else if (n % 4 == 0) {
            meshGrid(n, array1, array2);
            resultArray = reShape(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    int a = mod(array1[i][j], 4) / 2;
                    int b = mod(array2[i][j], 4) / 2;
                    if (a == b) {
                        resultArray[i][j] = n * n + 1 - resultArray[i][j];
                    }
                }
            }
        } else {
            int p = n / 2;
            int[][] temp = getResultArray(p);
            resultArray = new int[n][n];
            for (int i = 0; i < p; i++) {
                for (int j = 0; j < p; j++) {
                    assert temp != null;
                    resultArray[i][j] = temp[i][j];
                    resultArray[i][j + p] = temp[i][j] + 2 * p * p;
                    resultArray[i + p][j] = temp[i][j] + 3 * p * p;
                    resultArray[i + p][j + p] = temp[i][j] + p * p;
                }
            }
            printIS(resultArray);
            int k = (n - 2) / 4;
            for (int j = 0; j < k; j++) {
                for (int i = 0; i < p; i++) {
                    int t = resultArray[i][j];
                    resultArray[i][j] = resultArray[i + p][j];
                    resultArray[i + p][j] = t;
                }
            }
            printIS(resultArray);
            for (int j = n - k + 1; j < n; j++) {
                for (int i = 0; i < p; i++) {
                    int t = resultArray[i][j];
                    resultArray[i][j] = resultArray[i + p][j];
                    resultArray[i + p][j] = t;
                }
            }
            printIS(resultArray);
            int t = resultArray[k][0];
            resultArray[k][0] = resultArray[k + p][0];
            resultArray[k + p][0] = t;
            t = resultArray[k][k];
            resultArray[k][k] = resultArray[k + p][k];
            resultArray[k + p][k] = t;
        }
        return resultArray;
    }

    private static void printIS(int[][] is) {
        if (is == null) return;
        int n = is.length;
        for (int[] i1 : is) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += i1[j];
                System.out.print(String.format("%5d", i1[j]));
                if (j == n - 1) System.out.print(String.format("%5d", sum));
            }
            System.out.println();
        }
        for (int i = 0; i < n; i++) {
            int sum = 0;
            for (int j = 0; j < n; j++) {
                sum += is[j][i];
                if (j == n - 1) System.out.print(String.format("%5d", sum));
            }
        }
        int sumX = 0, sumY = 0;
        for (int i = 0; i < n; i++) {
            sumX += is[i][i];
            sumY += is[i][n - i - 1];
        }
        System.out.print(String.format("%5d-%d\n\n", sumX, sumY));
    }

    private static void meshGrid(int n, int[][] array1, int[][] array2) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                array1[i][j] = i + 1;
                array2[i][j] = j + 1;
            }
        }
    }

    private static int[][] reShape(int n) {
        int[][] M = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = i * n + j + 1;
            }
        }
        return M;
    }

    private static int mod(int a, int n) {
        int m = a % n;
        return m < 0 ? m + n : m;
    }
}
