package expression.generic.modes;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class IntegerMode implements Mode<Integer> {
    final boolean checkOverflow;

    public IntegerMode(boolean checkOverflow) {
        this.checkOverflow = checkOverflow;
    }

    @Override
    public Integer parseNumber(String number) {
        return Integer.parseInt(number);
    }

    @Override
    public Integer parseNumber(int number) {
        return number;
    }

    @Override
    public Integer add(Integer value1, Integer value2) {
        if (checkOverflow) {
            if (value1 > 0 && value2 > 0 && value1 + value2 < 0 ||
                    value1 < 0 && value2 < 0 && value1 + value2 >= 0) {
                throw new OverflowException(value1, value2, "+");
            }
        }
        return value1 + value2;
    }

    @Override
    public Integer subtract(Integer value1, Integer value2) {
        if (checkOverflow) {
            if (value1 >= 0 && value2 < 0 && value1 - value2 < 0 ||
                    value1 < 0 && value2 > 0 && value1 - value2 > 0) {
                throw new OverflowException(value1, value2, "-");
            }
        }
        return value1 - value2;
    }

    @Override
    public Integer multiply(Integer value1, Integer value2) {
        if (checkOverflow) {
            if (value1 == 0 || value2 == 0) {
                return 0;
            }
            if (value1 * value2 / value2 != value1 ||
                    value2 * value1 / value1 != value2) {
                throw new OverflowException(value1, value2, "*");
            }
        }
        return value1 * value2;
    }

    @Override
    public Integer divide(Integer value1, Integer value2) {
        if (checkOverflow) {
            if (value2 == 0) {
                throw new DivisionByZeroException("Denominator is zero");
            }
            if (value1 == Integer.MIN_VALUE && value2 == -1) {
                throw new OverflowException(value1, value2, "/");
            }
        }
        return value1 / value2;
    }

    @Override
    public Integer not(Integer x) {
        if (checkOverflow) {
            if (x == Integer.MIN_VALUE) {
                throw new OverflowException(x, "-");
            }
        }
        return -x;
    }

    @Override
    public Integer abs(Integer x) {
        if (x < 0) {
            return not(x);
        }
        return x;
    }

    @Override
    public Integer square(Integer x) {
        return multiply(x, x);
    }

    @Override
    public Integer mod(Integer value1, Integer value2) {
        if (checkOverflow) {
            if (value2 == 0) {
                throw new DivisionByZeroException("Denominator is zero");
            }
        }
        return value1 % value2;
    }
}
