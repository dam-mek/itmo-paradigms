package expression.generic.parser;

import expression.exceptions.BaseParser;
import expression.exceptions.CharSource;
import expression.exceptions.OverflowException;
import expression.exceptions.StringSource;
import expression.generic.modes.Mode;
import expression.generic.operation.*;

public class ExpressionParser<T> implements Parser<T> {

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

    Mode<T> mode;

    public ExpressionParser(Mode<T> mode) {
        this.mode = mode;
    }

    @Override
    public TripleExpression<T> parse(String expression) {
        return parse(new StringSource(expression));
    }

    private TripleExpression<T> parse(final CharSource source) {
        return new InnerParser<T>(source, mode).parseExpression();
    }

    private static class InnerParser<T> extends BaseParser {
        Mode<T> mode;
        public InnerParser(final CharSource source, Mode<T> mode) {
            super(source);
            this.mode = mode;
        }

        public TripleExpression<T> parseExpression() {
            final TripleExpression<T> result = parseAddSub();
            if (eof()) {
                return result;
            }
            throw error("End of expression expected");
        }

        private MasterExpression<T> parseAddSub() {
            MasterExpression<T> result = parseMulDiv();
            skipWhitespace();
            while (test('+') || test('-')) {
                if (take('+')) {
                    result = new Add<>(result, parseMulDiv());
                }
                if (take('-')) {
                    result = new Subtract<>(result, parseMulDiv());
                }
                skipWhitespace();
            }

            return result;
        }

        private MasterExpression<T> parseMulDiv() {
            MasterExpression<T> result = parseFactor();
            skipWhitespace();
            while (test('*') || test('/') || test("mod")) {
                if (take('*')) {
                    result = new Multiply<>(result, parseFactor());
                }
                if (take('/')) {
                    result = new Divide<>(result, parseFactor());
                }
                if (test("mod")) {
                    expect("mod");
                    result = new Mod<>(result, parseFactor());
                }
                skipWhitespace();
            }

            return result;
        }

        private MasterExpression<T> parseFactor() {
            skipWhitespace();
            if (test("abs")) {
                expect("abs");
                if (test("(")) {
                    return new Abs<>(parseFactor());
                }
                if (eof()) {
                    throw error("Excepted operand after abs", true);
                }
                if (!skipWhitespace()) {
                    throw error("Unknown operation", true);
                }
                return new Abs<>(parseFactor());
            }
            if (test("square")) {
                expect("square");
                if (test("(") || test("-")) {
                    return new Square<>(parseFactor());
                }
                if (eof()) {
                    throw error("Excepted operand after square", true);
                }
                if (!skipWhitespace()) {
                    throw error("Unknown operation", true);
                }
                return new Square<>(parseFactor());
            }
            if (take('-')) {
                if (Character.isDigit(getChar())) {
                    back();
                    return parseNumber();
                }
                return new Negate<>(parseFactor());
            }
            if (take('(')) {
                return parseBrackets();
            }
            return parseOperand();
        }

        private MasterExpression<T> parseBrackets() {
            MasterExpression<T> result = parseAddSub();
            expect(')');
            return result;
        }

        private MasterExpression<T> parseOperand() {
            if (Character.isLetter(getChar())) {
                if (test('x') || test('y') || test('z')) {
                    return new Variable<>(String.valueOf(take()), mode);
                }
                throw error("Invalid argument " + take(), true);
            }
            if (test('-') || Character.isDigit(getChar())) {
                return parseNumber();
            }
            throw error("Expected operand before " + getChar(), true);
        }

        private MasterExpression<T> parseNumber() {
            final StringBuilder sb = new StringBuilder();
            takeNumber(sb);
            try {
                return new Const<>(sb.toString(), mode);
            } catch (final NumberFormatException e) {
                throw new OverflowException(sb.toString(), "-");
            }
        }

        private void takeNumber(final StringBuilder sb) {
            if (take('-')) {
                sb.append('-');
            }

            while (!Character.isWhitespace(getChar()) && !eof() && getChar() != ')') {
                sb.append(take());
            }
        }

        private boolean skipWhitespace() {
            if (!Character.isWhitespace(getChar())) {
                return false;
            }
            while (Character.isWhitespace(getChar())) {
                take();
            }
            return true;
        }
    }
}
