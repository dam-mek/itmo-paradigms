package expression;

public class Const implements MasterExpression {

    private final Number number;

    public Const(int intConst) {
        number = intConst;
    }

    public Const(double doubleConst) {
        number = doubleConst;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Const &&
                number.equals(((Const) obj).number);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return number.intValue();
    }

    @Override
    public String toString() {
        return number.toString();
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
