package expression.generic.operation;

import expression.generic.modes.Mode;
import expression.generic.parser.MasterExpression;

public class Variable<T> implements MasterExpression<T> {

    private final String argument;
    private final Mode<T> mode;

    public Variable(String argument, Mode<T> mode) {
        this.argument = argument;
        this.mode = mode;
    }

    @Override
    public T evaluate(T x, T y, T z) {
        return switch (argument) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Invalid argument");
        };
    }

    @Override
    public Mode<T> getMode() {
        return mode;
    }

}
