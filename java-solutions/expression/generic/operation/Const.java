package expression.generic.operation;

import expression.generic.modes.Mode;
import expression.generic.parser.MasterExpression;

public class Const<T> implements MasterExpression<T> {

    private final T number;
    private final Mode<T> mode;

    public Const(String number, Mode<T> mode) {
        this.mode = mode;
        this.number = mode.parseNumber(number);
    }

    @Override
    public Mode<T> getMode() {
        return mode;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return number;
    }
}
