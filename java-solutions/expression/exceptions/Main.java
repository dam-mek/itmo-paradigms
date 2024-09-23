package expression.exceptions;

import expression.TripleExpression;

public class Main {
    public static void main(String[] args) {

        TripleExpression parsedExpression = new ExpressionParser().parse("1 set");
//        TripleExpression parsedExpression = new ExpressionParser().parse("1 * count");

        /*
        String expression = "1000000*x*x*x*x*x/(x-1)";
        TripleExpression parsedExpression = new ExpressionParser().parse(expression);
        System.out.println("x\tf");
        for (int x = 0; x < 11; x++) {
            String result;
            try {
                result = String.valueOf(parsedExpression.evaluate(x, 0, 0));
            } catch (DivisionByZeroException e) {
                result = "division by zero";
            } catch (OverflowException e) {
                result = "overflow";
            }
            System.out.println(x + "\t" + result);
        }
         */
    }
}
