package expression.exceptions;

public class StringSource implements CharSource {
    private final String data;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        if (pos < data.length()) {
            return true;
        } else {
            pos++;
            return false;
        }
    }

    @Override
    public char next() {
        return data.charAt(pos++);
    }

    @Override
    public char back() {
        return data.charAt(--pos - 1);
    }

    @Override
    public IllegalArgumentException error(final String message, final boolean printPosition) {
        if (printPosition) {
            return new IllegalArgumentException(message + " at position " + pos);
        }
        return new IllegalArgumentException(message);
    }
}
