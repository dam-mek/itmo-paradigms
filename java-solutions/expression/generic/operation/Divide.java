package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Divide<T> extends BinaryOperation<T> {

    public Divide(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected T operation(T value1, T value2) {
        return mode.divide(value1, value2);
    }
}
