package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {

    protected abstract void delete();

    protected abstract void enqueueImpl(Object elem);

    protected abstract Object elementImpl();

    @Override
    public void enqueue(Object elem) {
        Objects.requireNonNull(elem);
        enqueueImpl(elem);
    }

    @Override
    public Object dequeue() {
        assert size() > 0;
        Object result = element();
        delete();
        return result;
    }

    @Override
    public Object element() {
        assert size() > 0;
        return elementImpl();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
}
