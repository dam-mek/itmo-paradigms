package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Mod<T> extends BinaryOperation<T> {
    public Mod(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected T operation(T value1, T value2) {
        return mode.mod(value1, value2);
    }
}
