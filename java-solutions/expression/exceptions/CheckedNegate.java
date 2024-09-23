package expression.exceptions;

import expression.TripleExpression;

public class CheckedNegate extends Negate {
    protected CheckedNegate(TripleExpression expr) {
        super(expr);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int result = expr.evaluate(x, y, z);
        if (result == -2147483648) {
            throw new OverflowException(result, "-");
        }
        return -result;
    }
}
