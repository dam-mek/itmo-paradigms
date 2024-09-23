package expression.generic.modes;

public class ShortMode implements Mode<Short> {

    @Override
    public Short parseNumber(String number) {
        try {
            return Short.parseShort(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("invalid number for short: " + number);
        }
    }

    @Override
    public Short parseNumber(int number) {
        return (short) number;
    }

    @Override
    public Short add(Short x, Short y) {
        return (short) (x + y);
    }

    @Override
    public Short subtract(Short x, Short y) {
        return (short) (x - y);
    }

    @Override
    public Short multiply(Short x, Short y) {
        return (short) (x * y);
    }

    @Override
    public Short divide(Short x, Short y) {
        return (short) (x / y);
    }

    @Override
    public Short not(Short x) {
        return (short) (-x);
    }

    @Override
    public Short abs(Short x) {
        return (short) Math.abs(x);
    }

    @Override
    public Short square(Short x) {
        return multiply(x, x);
    }


    @Override
    public Short mod(Short x, Short y) {
        return (short) (x % y);
    }
}

