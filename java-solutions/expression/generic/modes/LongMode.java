package expression.generic.modes;

public class LongMode implements Mode<Long> {

    @Override
    public Long parseNumber(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("invalid number for long: " + number);
        }
    }

    @Override
    public Long parseNumber(int number) {
        return (long) number;
    }

    @Override
    public Long add(Long x, Long y) {
        return x + y;
    }

    @Override
    public Long subtract(Long x, Long y) {
        return x - y;
    }

    @Override
    public Long multiply(Long x, Long y) {
        return x * y;
    }

    @Override
    public Long divide(Long x, Long y) {
        return x / y;
    }

    @Override
    public Long not(Long x) {
        return -x;
    }

    @Override
    public Long abs(Long x) {
        return Math.abs(x);
    }

    @Override
    public Long square(Long x) {
        return multiply(x, x);
    }


    @Override
    public Long mod(Long x, Long y) {
        return x % y;
    }
}

