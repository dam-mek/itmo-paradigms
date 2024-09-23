package expression.exceptions;

import expression.Const;
import expression.MasterExpression;
import expression.TripleExpression;
import expression.Variable;

public class Log10 implements MasterExpression {
    protected final TripleExpression expr;

    public Log10(TripleExpression expr) {
        this.expr = expr;
    }

    @Override
    public String toMiniString() {
        if (expr instanceof Variable || expr instanceof Const || expr instanceof Negate
                || expr instanceof Count|| expr instanceof Log10|| expr instanceof Pow10) {
            return "log10 " + expr.toMiniString();
        }
        return "log10(" + expr.toMiniString() + ")";
    }

    @Override
    public String toString() {
        return "log10(" + expr.toString() + ")";
    }
    @Override
    public int getPriority() {
        return 12;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        int arg = expr.evaluate(x, y, z);
        if (arg <= 0) {
            throw new OverflowException(arg, "log10");
        }
        int res = -1;
        while (arg > 0) {
//            if (10*res/res != 10) {
//                throw new OverflowException(arg, "pow10");
//            }
            arg /= 10;
            res++;
//            System.out.println(res);
        }
        return res;
//        return Integer.bitCount(expr.evaluate(x, y, z));
    }
}
