package expression.exceptions;

import expression.Const;
import expression.MasterExpression;
import expression.TripleExpression;
import expression.Variable;

public class Negate implements MasterExpression {
    protected final TripleExpression expr;

    protected Negate(TripleExpression expr) {
        this.expr = expr;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return -expr.evaluate(x, y, z);
    }

    @Override
    public String toMiniString() {
        if (expr instanceof Variable || expr instanceof Const || expr instanceof Negate
                || expr instanceof Count || expr instanceof Pow10 || expr instanceof Log10) {
            return "- " + expr.toMiniString();
        }
        return "-(" + expr.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return "-(" + expr.toString() + ")";
    }

    @Override
    public int getPriority() {
        return 12;
    }
}
