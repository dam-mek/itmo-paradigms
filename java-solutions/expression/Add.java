package expression;

public class Add extends BinaryExpression {

    public Add(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public int getPriority() {
        return 6;
    }

    @Override
    public boolean isCommutative() {
        return true;
    }

    @Override
    protected int operation(int leftExpr, int rightExpr) {
        return leftExpr + rightExpr;
    }

    @Override
    protected double operation(double leftExpr, double rightExpr) {
        return leftExpr + rightExpr;
    }

    @Override
    protected String getSign() {
        return "+";
    }
}
