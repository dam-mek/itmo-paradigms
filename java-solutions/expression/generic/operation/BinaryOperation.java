package expression.generic.operation;

import expression.generic.modes.Mode;
import expression.generic.parser.MasterExpression;

public abstract class BinaryOperation<T> implements MasterExpression<T> {

    protected Mode<T> mode;

    protected final MasterExpression<T> leftExpr;
    protected final MasterExpression<T> rightExpr;

    protected BinaryOperation(MasterExpression<T> leftExpr, MasterExpression<T> rightExpr) {
        assert leftExpr.getMode().equals(rightExpr.getMode());
        mode = leftExpr.getMode();
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
    }

    protected abstract T operation(T value1, T value2);

    @Override
    public Mode<T> getMode() {
        return mode;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return operation(leftExpr.evaluate(x, y, z), rightExpr.evaluate(x, y, z));
    }
}
