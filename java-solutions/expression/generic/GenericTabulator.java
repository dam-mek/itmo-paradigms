package expression.generic;

import expression.generic.modes.BigIntegerMode;
import expression.generic.modes.IntegerMode;
import expression.generic.modes.Mode;
import expression.generic.modes.DoubleMode;
import expression.generic.modes.ShortMode;
import expression.generic.modes.LongMode;
import expression.generic.parser.ExpressionParser;
import expression.generic.parser.TripleExpression;

public class GenericTabulator implements Tabulator {

    @Override
    public Object[][][] tabulate(String modeName, String expression, int x1, int x2, int y1, int y2, int z1, int z2) {
        Mode<?> mode = switch (modeName) {
            case "d" -> new DoubleMode();
            case "i" -> new IntegerMode(true);
            case "bi" -> new BigIntegerMode();
            case "u" -> new IntegerMode(false);
            case "l" -> new LongMode();
            case "s" -> new ShortMode();
            default -> throw new IllegalArgumentException("There is no such mode as " + modeName);
        };

        try {
            return tabulate(mode, expression, x1, x2 - x1 + 1, y1, y2 - y1 + 1, z1, z2 - z1 + 1);
        } catch (IllegalArgumentException e) {
            System.out.println("Some problem with expression: " + e);
            return null;
        }
    }

    public <T> Object[][][] tabulate(Mode<T> mode, String expression, int x1, int dx, int y1, int dy, int z1, int dz) {
        TripleExpression<T> parsedExpression = new ExpressionParser<>(mode).parse(expression);
        Object[][][] result = new Object[dx][dy][dz];
        for (int i = 0; i < dx; i++) {
            for (int j = 0; j < dy; j++) {
                for (int k = 0; k < dz; k++) {
                    try {
                        result[i][j][k] = parsedExpression.evaluate(
                                mode.parseNumber(x1 + i),
                                mode.parseNumber(y1 + j),
                                mode.parseNumber(z1 + k)
                        );
                    } catch (Exception e) {
                        result[i][j][k] = null;
                    }
                }
            }
        }
        return result;
    }
}
