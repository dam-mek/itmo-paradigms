package expression;

public abstract class BinaryExpression implements MasterExpression {

    protected final MasterExpression leftExpr;
    private final int leftPriority;
    protected final MasterExpression rightExpr;
    private final int rightPriority;

    protected BinaryExpression(MasterExpression leftExpr, MasterExpression rightExpr) {
        this.leftExpr = leftExpr;
        this.rightExpr = rightExpr;
        this.leftPriority = leftExpr.getPriority();
        this.rightPriority = rightExpr.getPriority();
    }

    public abstract boolean isCommutative();

    @Override
    public String toMiniString() {
        StringBuilder result = new StringBuilder();
        int priorityCurrent = this.getPriority();

        if (needLeftBrackets(priorityCurrent)) {
            result.append("(");
            result.append(leftExpr.toMiniString());
            result.append(")");
        } else {
            result.append(leftExpr.toMiniString());
        }



        result.append(" ").append(getSign()).append(" ");

        if (needRightBrackets(priorityCurrent)) {
            result.append("(");
        }

        result.append(rightExpr.toMiniString());

        if (needRightBrackets(priorityCurrent)) {
            result.append(")");
        }

        return result.toString();
    }

    private boolean needRightBrackets(int priorityCurrent) {
        return priorityCurrent >= rightPriority && (priorityCurrent != rightPriority || !this.isCommutative())
                || this instanceof Multiply && rightExpr instanceof Divide;
    }

    private boolean needLeftBrackets(int priorityCurrent) {
        return leftPriority < priorityCurrent;
    }

    protected abstract int operation(int value1, int value2);

    protected abstract double operation(double value1, double value2);

    protected abstract String getSign();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BinaryExpression &&
                leftExpr.equals(((BinaryExpression) obj).leftExpr) &&
                this.getSign().equals(((BinaryExpression) obj).getSign()) &&
                rightExpr.equals(((BinaryExpression) obj).rightExpr);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return operation(leftExpr.evaluate(x, y, z), rightExpr.evaluate(x, y, z));
    }

    @Override
    public String toString() {
        return "(" + leftExpr.toString() + " " + getSign() + " " + rightExpr.toString() + ")";
    }

    @Override
    public int hashCode() {
        return getSign().hashCode() * 3
                + leftExpr.hashCode() * 17
                + rightExpr.hashCode() * 21;
    }
}
