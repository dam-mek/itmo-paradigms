package expression;

public class Variable implements MasterExpression {

    private final String argument;

    public Variable(String argument) {
        this.argument = argument;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null &&
                obj.getClass() == this.getClass() &&
                argument.equals(((Variable) obj).argument);
    }
    @Override
    public String toString() {
        return argument;
    }

    @Override
    public int hashCode() {
        return argument.hashCode();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return switch (argument) {
            case "x" -> x;
            case "y" -> y;
            case "z" -> z;
            default -> throw new AssertionError("Invalid argument");
        };
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
