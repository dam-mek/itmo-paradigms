package expression.exceptions;

import expression.Const;
import expression.MasterExpression;
import expression.TripleExpression;
import expression.Variable;

public class ExpressionParser implements TripleParser {

/*------------------------------------------------------------------
 *                         EXPRESSION RULES
 *
 * expression
 *       setclear EOF
 *
 * setclear
 *       addsub ( ( 'set' | 'clear' ) addsub )*
 *
 * addsub
 *       muldiv ( ( '+' | '-' ) muldiv )*
 *
 * muldiv
 *       factor ( ( '*' | '/' ) factor )*
 *
 * factor
 *       'count' factor
 *       '-' factor
 *        '(' setclear ')'
 *       operand
 *
 *  operand
 *       number
 *       variable
 *
 * number
 *     digits
 *     '-'digits
 *
 * variable
 *      x | y | z
 *
 *------------------------------------------------------------------*/

    @Override
    public TripleExpression parse(String expression) {
        return parse(new StringSource(expression));
    }

    public TripleExpression parse(final CharSource source) {
        return new Parser(source).parseExpression();
    }

    private static class Parser extends BaseParser {
        private boolean hasFirstArgument = false;
        private String operation;
        public Parser(final CharSource source) {
            super(source);
        }

        public TripleExpression parseExpression() {
            final TripleExpression result = parseSetClear();
            if (eof()) {
                return result;
            }
            throw error("End of expression expected");
        }

        private MasterExpression parseSetClear() {
            MasterExpression result = parseAddSub();
            skipWhitespace();
            while (true) {
                if (test("set")) {
                    expect("set");
                    operation = "set";
                    if (test("(") || test("-")) {
                        result = new Set(result, parseAddSub());
                        continue;
                    }
                    if (eof()) {
                        throw error("Excepted operand after set", true);
                    }
                    if (!skipWhitespace()) {
                        throw error("Unknown operation", true);
                    }
                    result = new Set(result, parseAddSub());
                } else if (test("clear")) {
                    expect("clear");
                    operation = "clear";
                    if (test("(") || test("-")) {
                        result = new Clear(result, parseAddSub());
                        continue;
                    }
                    if (eof()) {
                        throw error("Excepted operand after clear", true);
                    }
                    if (!skipWhitespace()) {
                        throw error("Unknown operation", true);
                    }
                    result = new Clear(result, parseAddSub());
                } else {
                    break;
                }
            }

            return result;
        }

        private MasterExpression parseAddSub() {
            MasterExpression result = parseMulDiv();
            skipWhitespace();
            while (test('+') || test('-')) {
                if (take('+')) {
                    operation = "+";
                    result = new CheckedAdd(result, parseMulDiv());
                }
                if (take('-')) {
                    operation = "-";
                    result = new CheckedSubtract(result, parseMulDiv());
                }
                skipWhitespace();
            }

            return result;
        }

        private MasterExpression parseMulDiv() {
            MasterExpression result = parseFactor();
            skipWhitespace();
            while (test('*') || test('/')) {
                if (take('*')) {
                    operation = "*";
                    result = new CheckedMultiply(result, parseFactor());
                }
                if (take('/')) {
                    operation = "/";
                    result = new CheckedDivide(result, parseFactor());
                }
                skipWhitespace();
            }

            return result;
        }

        private MasterExpression parseFactor() {
            skipWhitespace();
            if (test("count")) {
                expect("count");
                operation = "count";
                if (test("(")) {
                    return new Count(parseFactor());
                }
                if (eof()) {
                    throw error("Excepted operand after count", true);
                }
                if (!skipWhitespace()) {
                    throw error("Unknown operation", true);
                }
                return new Count(parseFactor());
            }
            if (test("log10")) {
                expect("log10");
                operation = "log10";
                if (test("(") || test("-")) {
                    return new Log10(parseFactor());
                }
                if (eof()) {
                    throw error("Excepted operand after log10", true);
                }
                if (!skipWhitespace()) {
                    throw error("Unknown operation", true);
                }
                return new Log10(parseFactor());
            }
            if (test("pow10")) {
                expect("pow10");
                operation = "pow10";
                if (test("(")) {
                    return new Pow10(parseFactor());
                }
                if (eof()) {
                    throw error("Excepted operand after pow10", true);
                }
                if (!skipWhitespace()) {
                    throw error("Unknown operation", true);
                }
                return new Pow10(parseFactor());
            }
            if (take('-')) {
                if (Character.isDigit(getChar())) {
                    back();
                    return parseNumber();
                }
                return new CheckedNegate(parseFactor());
            }
            if (take('(')) {
                return parseBrackets();
            }
            MasterExpression tmp = parseOperand();
            hasFirstArgument = true;
            return tmp;
        }

        private MasterExpression parseBrackets() {
            MasterExpression result = parseSetClear();
            expect(')');
            return result;
        }

        private MasterExpression parseOperand() {
            if (Character.isLetter(getChar())) {
                if (test('x') || test('y') || test('z')) {
                    return new Variable(String.valueOf(take()));
                }
                throw error("Invalid argument " + take(), true);
            }
            if (test('-') || Character.isDigit(getChar())) {
                return parseNumber();
            }
            if (hasFirstArgument) {
                throw error("Expected operand after " + operation, true);
            } else {
                throw error("Expected operand before " + getChar(), true);
            }
        }

        private MasterExpression parseNumber() {
            final StringBuilder sb = new StringBuilder();
            takeInteger(sb);
            try {
                return new Const(Integer.parseInt(sb.toString()));
            } catch (final NumberFormatException e) {
                throw new OverflowException(sb.toString(), "-");
            }
        }

        private void takeInteger(final StringBuilder sb) {
            if (take('-')) {
                sb.append('-');
            }
            while (Character.isDigit(getChar())) {
                sb.append(take());
            }
        }

        private boolean skipWhitespace() {
            boolean f = false;
            while (Character.isWhitespace(getChar())) {
                take();
                f = true;
            }
            return f;
        }
    }
}
