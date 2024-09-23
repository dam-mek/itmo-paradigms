package expression.generic.modes;

import java.math.BigInteger;

public class BigIntegerMode implements Mode<BigInteger> {
    @Override
    public BigInteger parseNumber(String number) {
        return new BigInteger(number);
    }

    @Override
    public BigInteger parseNumber(int number) {
        return BigInteger.valueOf(number);
    }

    @Override
    public BigInteger add(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger subtract(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger multiply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger divide(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    @Override
    public BigInteger not(BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger abs(BigInteger x) {
        return x.abs();
    }

    @Override
    public BigInteger square(BigInteger x) {
        return multiply(x, x);
    }

    @Override
    public BigInteger mod(BigInteger x, BigInteger y) {
        return x.mod(y);
    }
}
