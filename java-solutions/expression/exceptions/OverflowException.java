package expression.exceptions;

public class OverflowException extends RuntimeException {

    public OverflowException(int value, String operation) {
        super(operation + " " + value + " is overflow");
    }

    public OverflowException(String value, String operation) {
        super(value + " is overflow const");
    }

    public OverflowException(int value1, int value2, String operation) {
        super(value1 + " " + operation + " " + value2 + " is overflow");
    }
}