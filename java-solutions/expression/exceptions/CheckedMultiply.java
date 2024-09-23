package expression.exceptions;

import expression.MasterExpression;
import expression.Multiply;

public class CheckedMultiply extends Multiply {


    public CheckedMultiply(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected int operation(int value1, int value2) {
        if (value1 == 0 || value2 == 0) {
            return 0;
        }
        if (value1 * value2 / value2 != value1 ||
                value2 * value1 / value1 != value2) {
            throw new OverflowException(value1, value2, "*");
        }
        return value1 * value2;
    }
}
