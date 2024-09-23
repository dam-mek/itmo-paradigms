package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Add<T> extends BinaryOperation<T> {
    public Add(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    protected T operation(T value1, T value2) {
        return mode.add(value1, value2);
    }
}
