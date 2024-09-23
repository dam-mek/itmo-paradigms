package queue;

import java.util.Objects;

// Model: a[1]...a[n]
// Inv: n >= 0 && a[tail] == null && elements[(head - 1) & (elements.length - 1)] == null && (
//      head == tail && a[head] == null ||
//      head != tail && elements[head] != null && elements[(tail - 1) & (elements.length - 1)] != null
//      )
// Let immutable(n): for i=1..n: a'[i] == a[i]

public class ArrayQueueModule {

    private static Object[] elements = new Object[8];
    private static int head = 0;
    private static int tail = 0;

    private static void doubleCapacity() {
        assert tail == head;
        int length = elements.length;
        Object[] new_array = new Object[length * 2];
        copyToArray(new_array);
        head = 0;
        tail = length;
        elements = new_array;
    }

    private static void copyToArray(Object[] array) {
        if (head >= tail) {
            System.arraycopy(elements, head, array, 0, elements.length - head);
            System.arraycopy(elements, 0, array, elements.length - head, tail);
        } else {
            System.arraycopy(elements, head, array, 0, tail - head);
        }
    }

    // enqueue – добавить элемент в очередь
    // Pred: elem != null
    // Post: n' = n + 1 && a[n'] = elem && immutable(n)
    public static void enqueue(Object elem) {
        Objects.requireNonNull(elem);
        elements[tail++] = elem;
        tail &= elements.length - 1;
        if (tail == head) {
            doubleCapacity();
        }
    }

    // element – первый элемент в очереди
    // Pred: n > 0
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element() {
        return elements[head];
    }

    // dequeue – удалить и вернуть первый элемент в очереди
    // Pred: n > 0
    // Post: R == a[1] && n' == n - 1 && for i=2..n: a'[i - 1] == a[i]
    public static Object dequeue() {
        Object result = elements[head];
        elements[head++] = null;
        head &= elements.length - 1;
        return result;
    }

    // size – текущий размер очереди
    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    public static int size() {
        return (tail - head) & (elements.length - 1);
    }

    // isEmpty – является ли очередь пустой
    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty() {
        return tail == head;
    }

    // clear – удалить все элементы из очереди
    // Pred: true
    // Post: n' == 0
    public static void clear() {
        elements = new Object[8];
        head = 0;
        tail = 0;
    }

    // push – добавить элемент в начало очереди
    // Pred: elem != null
    // Post: n' = n + 1 && a[1] = elem && for i=1..n: a'[i + 1] == a[i]
    public static void push(Object elem) {
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
    public static Object peek() {
        return elements[(tail - 1) & (elements.length - 1)];
    }

    // remove – удалить и вернуть последний элемент в очереди
    // Pred: n > 0
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public static Object remove() {
        int new_tail = (tail - 1) & (elements.length - 1);
        Object result = elements[new_tail];
        elements[tail] = null;
        tail = new_tail;
        return result;
    }

    // toArray - массив, содержащий элементы, лежащие в очереди в порядке от головы к хвосту
    // Pred: true
    // Post: immutable(n) && n' == n && R is array && for i=1..n: R[i - 1] == a[i]
    public static Object[] toArray() {
        Object[] array = new Object[size()];
        if (!isEmpty()) {
            copyToArray(array);
        }
        return array;
    }
}
