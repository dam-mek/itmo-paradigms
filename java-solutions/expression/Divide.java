package expression;

public class Divide extends BinaryExpression {

    public Divide(MasterExpression leftExpr, MasterExpression rightExpr) {
        super(leftExpr, rightExpr);
    }

    @Override
    public int getPriority() {
        return 8;
    }

    @Override
    public boolean isCommutative() {
        return false;
    }

    @Override
    protected int operation(int leftExpr, int rightExpr) {
        return leftExpr / rightExpr;
    }

    @Override
    protected double operation(double leftExpr, double rightExpr) {
        return leftExpr / rightExpr;
    }

    @Override
    protected String getSign() {
        return "/";
    }
}
