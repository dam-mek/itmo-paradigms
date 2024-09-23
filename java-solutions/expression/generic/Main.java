package expression.generic;

public class Main {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2) {
            System.out.println("not enough arguments");
            return;
        }

        String mode = switch (args[0]) {
            case "-d" -> "d";
            case "-i" -> "i";
            case "-bi" -> "bi";
            case "-u" -> "u";
            case "-l" -> "l";
            case "-s" -> "s";
            default -> null;
        };
        if (mode == null) {
            System.out.println("Unknown mode of calculation");
            return;
        }

        String expression = args[1];
        Tabulator tabulator = new GenericTabulator();
        Object[][][] result;
        try {
            result = tabulator.tabulate(mode, expression, -2, 2, -2, 2, -2, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Some problem with expression: " + e);
            return;
        }
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    System.out.println("x=" + i + " y=" + j + " z=" + k + " result=" + result[i + 2][j + 2][k + 2]);
                }
            }
        }
    }
}