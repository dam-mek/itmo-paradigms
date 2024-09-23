package expression.generic.operation;

import expression.generic.modes.Mode;
import expression.generic.parser.MasterExpression;

public abstract class UnaryOperation<T> implements MasterExpression<T> {

    protected Mode<T> mode;

    protected final MasterExpression<T> expr;

    protected UnaryOperation(MasterExpression<T> expr) {
        this.expr = expr;
        mode = expr.getMode();
    }

    protected abstract T operation(T value);

    @Override
    public T evaluate(T x, T y, T z) {
        return operation(expr.evaluate(x, y, z));
    }

    @Override
    public Mode<T> getMode() {
        return mode;
    }
}
