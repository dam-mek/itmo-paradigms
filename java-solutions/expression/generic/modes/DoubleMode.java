package expression.generic.modes;

public class DoubleMode implements Mode<Double> {

    @Override
    public Double parseNumber(String number) {
        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("invalid number for double: " + number);
        }
    }

    @Override
    public Double parseNumber(int number) {
        return (double) number;
    }

    @Override
    public Double add(Double x, Double y) {
        return x + y;
    }

    @Override
    public Double subtract(Double x, Double y) {
        return x - y;
    }

    @Override
    public Double multiply(Double x, Double y) {
        return x * y;
    }

    @Override
    public Double divide(Double x, Double y) {
        return x / y;
    }

    @Override
    public Double not(Double x) {
        return -x;
    }

    @Override
    public Double abs(Double x) {
        return Math.abs(x);
    }

    @Override
    public Double square(Double x) {
        return multiply(x, x);
    }


    @Override
    public Double mod(Double x, Double y) {
        return x % y;
    }
}

