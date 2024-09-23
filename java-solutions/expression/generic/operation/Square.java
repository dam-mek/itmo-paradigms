package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Square<T> extends UnaryOperation<T> {

    public Square(MasterExpression<T> expr) {
        super(expr);
    }

    @Override
    protected T operation(T value) {
        return mode.square(value);
    }
}
