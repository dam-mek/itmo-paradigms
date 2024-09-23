package search;

public class BinarySearchUni {
    // Pred: args != null && 0 < args.length &&
    //       all(args[i] can be represented as int for i in range(args.length)) &&
    //       exists x in range(args.length):
    //       all(int(args[i - 1]) < int(args[i]) for i in range(1, x)) and
    //       all(int(args[i - 1]) > int(args[i]) for i in range(x + 1, args.length))
    // Post: prints x to stdout
    public static void main(final String[] args) {
        // Pred
        int n = args.length;
        // Pred && n = args.length
        int[] a = new int[n];
        // Pred && n = args.length && a != null && a.length == n

        // I: Pred && n = args.length && a != null && a.length == n && all(a[j] == int(args[j]) for j in range(i))
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(args[i]);
        }
        // Let Pred = Pred && n = args.length && a != null && a.length == n && a == int(args)
        int s = 0;
        // Pred && s == 0 && exists x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, n))]

        // I: Pred && s == sum(a[j] for j in range(i)) && exists x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, n))]
        for (int j : a) {
            s += j;
        }

        // Pred && exists x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, n))]
        int x;
        if (s % 2 == 0) {
            x = recursive(a, 0, n);
        } else {
            x = iterative(a, n);
        }
        // x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, n))]
        System.out.println(x);
    }

    // Pred: n == a.length && exists x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, n))]
    // Post: R == x
    public static int iterative(int[] a, int n) {

        // Pred
        int l = 0;
        // Pred && l == 0
        int r = n;
        // Pred && l == 0 && r == n

        // I: Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n
        while (r - l > 1) {
            // I
            int m = (r + l) / 2;
            // I && l < m < r
            if (a[m - 1] <= a[m]) {
                // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n && l < m < r && a[m - 1] <= a[m]
                // Pred &&                                 (r == n || a[r - 1] > a[r]) && 0 <= l < m < r <= n && (m == 0 || a[m - 1] <= a[m])
                l = m;
                // Pred &&                                 (r == n || a[r - 1] > a[r]) && 0 <= m < r <= n && (m == 0 || a[m - 1] <= a[m]) && l == m
                // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n
            } else {
                // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n && l < m < r && a[m - 1] > a[m]
                // Pred && (l == 0 || a[l - 1] <= a[l]) &&                                0 <= l < m < r <= n && (m == n || a[m - 1] > a[m])
                r = m;
                // Pred && (l == 0 || a[l - 1] <= a[l]) &&                                 0 <= l < m <= n && (m == n || a[m - 1] > a[m]) && r == m
                // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n
            }
            // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n
            // I
        }
        // I && !(r - l > 1)
        // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n && r <= 1 + l
        // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == n || a[r - 1] > a[r]) && 0 <= l < r <= n && r == l + 1
        // Pred && (l == 0 || a[l - 1] < a[l] || a[l - 1] == a[l]) && (l + 1 == n || a[l] > a[l + 1]) && 0 <= l < n
        // Pred && (l == 0 || a[l - 1] < a[l] || a[l - 1] == a[l]) && (l + 1 == n || a[l] > a[l + 1]) && 0 <= l < n
        // Pred && (l == 0 || all(a[i - 1] < a[i] for i in range(l, l + 1)) || a[l - 1] == a[l]) && (l + 1 == n || all(a[i - 1] > a[i] for i in range(l + 1, l + 2))) && 0 <= l < n
        // Pred && all(a[i - 1] < a[i] for i in range(1, l)) && all(a[i - 1] > a[i] for i in range(l + 1, args.length)) && 0 <= l < n
        // Pred && all(int(args[i - 1]) < int(args[i]) for i in range(1, l)) && all(int(args[i - 1]) > int(args[i]) for i in range(l + 1, args.length))
        return l;
    }

    // Pred:
    //       (l == 0 || a[l - 1] <= a[l]) && (r == a.length || a[r - 1] > a[r]) && 0 <= l < r <= a.length &&
    //       exists x in range(args.length): [all(a[i - 1] < a[i] for i in range(1, x)) && all(a[i - 1] > a[i] for i in range(x + 1, a.length))]
    // Post: R == x
    static int recursive(final int[] a, final int l, final int r) {
        // Pred
        if (1 >= r - l) {
            // Pred && l < r && 1 >= r - l
            // Pred && (l == 0 || a[l - 1] <= a[l]) && (r == a.length || a[r - 1] > a[r]) && 0 <= l < r <= a.length && r == l + 1
            // similar proof as in iterative after while
            return l;
        }

        // Pred && l + 1 < r
        int m = (r + l) / 2;
        // Pred && l < m < r

        if (a[m - 1] <= a[m]) {
            // similar proof as in iterative in if Statement
            return recursive(a, m, r);
        } else {
            // similar proof as in iterative in if Statement
            return recursive(a, l, m);
        }
    }
}
