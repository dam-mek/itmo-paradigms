package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Abs<T> extends UnaryOperation<T> {

    public Abs(MasterExpression<T> expr) {
        super(expr);
    }

    @Override
    protected T operation(T value) {
        return mode.abs(value);
    }
}
