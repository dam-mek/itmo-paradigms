package expression.exceptions;

import expression.MasterExpression;
import expression.Subtract;

public class CheckedSubtract extends Subtract {

    public CheckedSubtract(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected int operation(int value1, int value2) {
        if (value1 >= 0 && value2 < 0 && value1 - value2 < 0 ||
                value1 < 0 && value2 > 0 && value1 - value2 > 0) {
            throw new OverflowException(value1, value2, "-");
        }
        return value1 - value2;
    }

}
