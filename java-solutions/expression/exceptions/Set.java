package expression.exceptions;

import expression.BinaryExpression;
import expression.MasterExpression;

public class Set extends BinaryExpression {
    public Set(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    protected int operation(int value1, int value2) {
        if ((value1 & (1 << value2)) == 0) {
            return value1 + (1 << value2);
        }
        return value1;
    }

    @Override
    protected double operation(double value1, double value2) {
        return 0;
    }

    @Override
    protected String getSign() {
        return "set";
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
