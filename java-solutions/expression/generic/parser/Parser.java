package expression.generic.parser;

public interface Parser<T> {
    TripleExpression<T> parse(String expression) throws Exception;
}
