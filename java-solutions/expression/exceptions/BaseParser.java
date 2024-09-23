package expression.exceptions;

public class BaseParser {
    private static final char END = '\0';
    private final CharSource source;
    private char ch;

    protected BaseParser(final CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected void back() {
        ch = source.back();
    }

    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (!take(expected.charAt(i))) {
                while (i-- > 0) {
                    back();
                }
                return false;
            }
        }
        for (int i = 0; i < expected.length(); i++) {
            back();
        }
        return true;
    }

    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }
    protected char getChar() {
        return ch;
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            if (ch == END) {
                throw error("Expected '" + expected + "'");
            }
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message, final boolean printPosition) {
        return source.error(message, printPosition);
    }

    protected IllegalArgumentException error(final String message) {
        return error(message, false);
    }
}
