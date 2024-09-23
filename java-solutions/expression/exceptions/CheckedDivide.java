package expression.exceptions;

import expression.Divide;
import expression.MasterExpression;

public class CheckedDivide extends Divide {

    public CheckedDivide(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }


    @Override
    protected int operation(int value1, int value2) {
        if (value2 == 0) {
            throw new DivisionByZeroException("Denominator is zero");
        }
        if (value1 == Integer.MIN_VALUE && value2 == -1) {
            throw new OverflowException(value1, value2, "/");
        }
        return value1 / value2;
    }
}
