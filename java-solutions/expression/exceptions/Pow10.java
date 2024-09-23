package expression.exceptions;

import expression.Const;
import expression.MasterExpression;
import expression.TripleExpression;
import expression.Variable;

public class Pow10 implements MasterExpression {
    protected final TripleExpression expr;

    public Pow10(TripleExpression expr) {
        this.expr = expr;
    }

    @Override
    public String toMiniString() {
        if (expr instanceof Variable || expr instanceof Const || expr instanceof Negate
                || expr instanceof Count || expr instanceof Pow10 || expr instanceof Log10) {
            return "pow10 " + expr.toMiniString();
        }
        return "pow10(" + expr.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return "pow10(" + expr.toString() + ")";
    }
    @Override
    public int getPriority() {
        return 12;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int res = 1;
        int arg = expr.evaluate(x, y, z);
        if (arg < 0) {
            throw new OverflowException(arg, "pow10");
//            return 0;
        }
        while (arg-- > 0) {
            if (10*res/res != 10) {
                throw new OverflowException(arg, "pow10");
            }
            res *= 10;
//            System.out.println(res);
        }
        return res;
//        return Integer.bitCount(expr.evaluate(x, y, z));
    }
}
