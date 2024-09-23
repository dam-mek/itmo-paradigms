package expression.exceptions;

import expression.Const;
import expression.MasterExpression;
import expression.TripleExpression;
import expression.Variable;

public class Count implements MasterExpression {
    protected final TripleExpression expr;

    public Count(TripleExpression expr) {
        this.expr = expr;
    }

    @Override
    public String toMiniString() {
        if (expr instanceof Variable || expr instanceof Const || expr instanceof Negate
                || expr instanceof Count || expr instanceof Pow10 || expr instanceof Log10) {
            return "count " + expr.toMiniString();
        }
        return "count(" + expr.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return "count(" + expr.toString() + ")";
    }
    @Override
    public int getPriority() {
        return 12;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return Integer.bitCount(expr.evaluate(x, y, z));
    }
}
