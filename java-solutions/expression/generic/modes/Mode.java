package expression.generic.modes;

public interface Mode<T> {
    T parseNumber(String number);

    T parseNumber(int number);

    T add(T x, T y);

    T subtract(T x, T y);

    T multiply(T x, T y);

    T divide(T x, T y);

    T not(T x);

    T abs(T x);

    T square(T x);

    T mod(T x, T y);
}