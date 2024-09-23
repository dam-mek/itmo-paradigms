package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {

    private Object[] elements;
    private int head;
    private int tail;

    public ArrayQueue() {
        elements = new Object[8];
        head = 0;
        tail = 0;
    }

    private void doubleCapacity() {
        assert tail == head;
        int length = elements.length;
        Object[] new_array = new Object[length * 2];
        copyToArray(new_array);
        head = 0;
        tail = length;
        elements = new_array;
    }

    private void copyToArray(Object[] array) {
        if (head >= tail) {
            System.arraycopy(elements, head, array, 0, elements.length - head);
            System.arraycopy(elements, 0, array, elements.length - head, tail);
        } else {
            System.arraycopy(elements, head, array, 0, tail - head);
        }
    }

    @Override
    public void enqueueImpl(Object elem) {
        elements[tail++] = elem;
        tail &= elements.length - 1;
        if (tail == head) {
            doubleCapacity();
        }
    }

    @Override
    public Object elementImpl() {
        return elements[head];
    }

    protected void delete() {
        elements[head++] = null;
        head &= elements.length - 1;
    }

    @Override
    public int size() {
        return (tail - head) & (elements.length - 1);
    }


    @Override
    public void clear() {
        elements = new Object[8];
        tail = 0;
        head = 0;
    }

    @Override
    public boolean contains(Object elem) {
        for (int j = head; j != tail; j = (j + 1) & (elements.length - 1)) {
            if (elements[j].equals(elem)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object elem) {
        Object[] array = toArray();
        int elemIndex = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(elem)) {
                elemIndex = i;
                break;
            }
        }
        if (elemIndex == -1) {
            return false;
        }
        Object[] new_array = new Object[elements.length];
        System.arraycopy(toArray(), 0, new_array, 0, elemIndex);
        System.arraycopy(toArray(), elemIndex + 1, new_array, elemIndex, size() - elemIndex - 1);
        tail = size() - 1;
        head = 0;
        elements = new_array;
        return true;
    }

    // push – добавить элемент в очередь
    // Pred: elem != null
    // Post: n' = n + 1 && a[1] = elem && for i=1..n: a'[i + 1] == a[i]
    public void push(Object elem) {
        Objects.requireNonNull(elem);
        head = (head - 1) & (elements.length - 1);
        elements[head] = elem;
        if (tail == head) {
            doubleCapacity();
        }
    }

    // peek – последний элемент в очереди
    // Pred: n > 0
    // Post: R == a[n] && n' == n && immutable(n)
    public Object peek() {
        return elements[(tail - 1) & (elements.length - 1)];
    }

    // remove – удалить и вернуть последний элемент в очереди
    // Pred: n > 0
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public Object remove() {
        int new_tail = (tail - 1) & (elements.length - 1);
        Object result = elements[new_tail];
        elements[tail] = null;
        tail = new_tail;
        return result;
    }

    // toArray - массив, содержащий элементы, лежащие в очереди в порядке от головы к хвосту
    // Pred: true
    // Post: immutable(n) && n' == n && R is array && for i=1..n: R[i - 1] == a[i]
    public Object[] toArray() {
        Object[] array = new Object[size()];
        if (!isEmpty()) {
            copyToArray(array);
        }
        return array;
    }
}
