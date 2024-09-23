package expression.generic.operation;

import expression.generic.parser.MasterExpression;

public class Negate<T> extends UnaryOperation<T> {
    public Negate(MasterExpression<T> expr) {
        super(expr);
    }

    @Override
    protected T operation(T value) {
        return mode.not(value);
    }
}
