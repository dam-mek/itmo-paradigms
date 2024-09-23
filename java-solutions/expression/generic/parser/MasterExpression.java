package expression.generic.parser;

import expression.generic.modes.Mode;

public interface MasterExpression<T> extends TripleExpression<T> {
    T evaluate(T x, T y, T z);
    Mode<T> getMode();
}
