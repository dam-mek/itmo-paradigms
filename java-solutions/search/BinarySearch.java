package search;

public final class BinarySearch {
    // Pred: args != null && 0 < args.length &&
    //       all(args[i] can be represented as int for i in range(args.length)) &&
    //       all(int(args[i - 1]) >= int(args[i]) for i in range(2, args.length))
    // Post: prints to stdout minimal i such that 0 < i < args.length and int(args[i]) <= int(args[0]),
    //       or args.length if no such element exists, or 0 if args.length == 1
    public static void main(final String[] args) {
        final int x = Integer.parseInt(args[0]);
        // Pred && x = int(args[0])
        final int[] a = new int[args.length - 1];
        // Pred && x = int(args[0]) && a.length == args.length - 1

        // I: Pred && x = int(args[0]) && a != null && a.length == args.length - 1 && all(a[j] == int(args[j + 1] for j in range(i))
        for (int i = 0; i < a.length; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }

        int s = 0;
        // Pred && s == 0 && x = int(args[0]) && a != null && a.length == args.length - 1 && all(a[i] >= a[i + 1] for i in range(a.length - 1))

        // I: Pred && s == sum(a[j] for j in range(i)) && x = int(args[0]) && a != null && a.length == args.length - 1 && all(a[j] == int(args[j + 1] for j in range(i))
        for (int i = 0; i < a.length; i++) {
            s += a[i];
        }

        // Pred && x = int(args[0]) && a != null && a.length == args.length - 1 && all(a[i] >= a[i + 1] for i in range(a.length - 1))
        if (s % 2 == 1) {
            System.out.println(iterative(a, x));
        } else {
           System.out.println(recursive(a, x, -1, a.length));
        }

    }

    // Pred: a != null && all(a[i] >= a[i + 1] for i in range(a.length - 1))
    // Post: Pred && (R == a.length && x < a[a.length - 1] || R == 0 && a[0] <= x || 0 < R < a.length && a[R] <= x && a[R - 1] < x)
    static int iterative(final int[] a, final int x) {
        int l = -1;
        // Pred && l == -1
        int r = a.length;
        // Pred && l == -1 && r == a.length

        // I: Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length
        while (1 < r - l) {
            // I && 1 < r - l
            int m = (r + l) / 2;
            // I && 1 < r - l && l < m < r
            if (x < a[m]) {
                // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < m < r <= a.length && 1 < r - l && x < a[m]
                // Pred &&                          (r == a.length || a[r] <= x) && -1 <= l < m < r <= a.length && 1 < r - l && (m == -1 || x < a[m])
                l = m;
                // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length
            } else {
                // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < m < r <= a.length && 1 < r - l && !(x < a[m])
                // Pred && (l == -1 || x < a[l]) &&                                 -1 <= l < m < r <= a.length && 1 < r - l && (m == a.length || a[m] <= x)
                r = m;
                // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length
            }
            // I
        }
        // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length && !(1 < r - l)
        // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length && l + 1 >= r
        // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length && r == l + 1
        // Pred && (r == 0 || x < a[r - 1]) && (r == a.length || a[r] <= x) && (r == 0 || 0 < r < a.length || r == a.length)
        // Pred && (r == 0 || x < a[r - 1]) && (r == a.length || 0 <= r <= a.length && a[r] <= x)
        // Pred && (r == 0 || x < a[r - 1]) && (r == a.length || 0 <= r < a.length && a[r] <= x)
        // Pred && (r == 0 && a[0] <= x || x < a[r - 1] && 0 <= r < a.length && a[r] <= x || x < a[r - 1] && r == a.length)
        // Pred && (r == 0 && a[0] <= x || 0 < r < a.length && a[r] <= x && a[r - 1] > x || r == a.length && x < a[r - 1])
        // Pred && (r == a.length && x < a[a.length - 1] || r == 0 && a[0] <= x || 0 < r < a.length && a[r] <= x && a[r - 1] < x)
        return r;
    }

    // Pred: a != null && all(a[i] >= a[i + 1] for i in range(a.length - 1)) && -1 <= l < r <= a.length &&
    //       (l == -1 || x < a[l]) && (r == a.length || a[r] <= x)
    // Post: Pred && (R == a.length && x < a[a.length - 1] || R == 0 && a[0] <= x || 0 < R < a.length && a[R] <= x && a[R - 1] < x)
    static int recursive(final int[] a, final int x, final int l, final int r) {
        // Pred
        if (1 >= r - l) {
            // Pred && l < r && 1 >= r - l
            // Pred && (l == -1 || x < a[l]) && (r == a.length || a[r] <= x) && -1 <= l < r <= a.length && r == l + 1
            // similar proof as in iterative after while
            return r;
        }

        // Pred && l + 1 < r
        int m = (r + l) / 2;
        // Pred && l < m < r

        if (x < a[m]) {
            // similar proof as in iterative in if Statement
            return recursive(a, x, m, r);
        } else {
            // similar proof as in iterative in if Statement
            return recursive(a, x, l, m);
        }
    }
}