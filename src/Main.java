public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//    }


        public static int knapsack(int[] wagi, int[] wartosci, int pojemnosc) {
            int n = wagi.length;
            int[][] dp = new int[n + 1][pojemnosc + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= pojemnosc; j++) {
                    if (wagi[i - 1] <= j) {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - wagi[i - 1]] + wartosci[i - 1]);
                    } else {
                        dp[i][j] = dp[i - 1][j];
                    }
                }
            }
            return dp[n][pojemnosc];
        }

        public static void main(String[] args) {
//            int[] wagi = {2, 4, 6, 1, 5, 3, 7, 9, 2, 4, 6, 8, 3, 5, 7, 1, 2, 4, 6, 8};
//            int[] wartosci = {3, 5, 7, 2, 8, 4, 6, 9, 1, 5, 7, 8, 3, 4, 6, 2, 1, 5, 8, 9};
//            int pojemnosc = 50;
//            System.out.println(knapsack(wagi, wartosci, pojemnosc)); // output: 11
            int[] b = {1,1,1};
            a(b);
            System.out.println(b[0]);

        }

        static void a(int[] b) {
            b[0] = 4;
        }



//public class Main {
//    public static void main(String[] args) {
//
//    }
//}
}