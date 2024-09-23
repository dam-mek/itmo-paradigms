package expression.exceptions;

public interface CharSource {
    boolean hasNext();
    char next();
    char back();
    IllegalArgumentException error(String message, boolean position);
}
