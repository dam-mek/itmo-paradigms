package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Multiply<T> extends BinaryOperation<T> {

    public Multiply(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected T operation(T value1, T value2) {
        return mode.multiply(value1, value2);
    }
}
