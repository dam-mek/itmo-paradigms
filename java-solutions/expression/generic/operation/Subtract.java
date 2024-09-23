package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Subtract<T> extends BinaryOperation<T> {

    public Subtract(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected T operation(T value1, T value2) {
        return mode.subtract(value1, value2);
    }
}
