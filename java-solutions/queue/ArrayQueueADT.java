package queue;

import java.util.Objects;

// Model: a[1]...a[n]
// Inv: n >= 0 && a[tail] == null && elements[(head - 1) & (elements.length - 1)] == null && (
//      head == tail && a[head] == null ||
//      head != tail && elements[head] != null && elements[(tail - 1) & (elements.length - 1)] != null
//      )
// Let immutable(n): for i=1..n: a'[i] == a[i]

public class ArrayQueueADT {

    private Object[] elements = new Object[8];
    private int head = 0;
    private int tail = 0;

    public ArrayQueueADT() {
    }

    public static ArrayQueueADT create() {
        return new ArrayQueueADT();
    }

    private static void doubleCapacity(ArrayQueueADT queue) {
        assert queue.tail == queue.head;
        int length = queue.elements.length;
        Object[] new_array = new Object[length * 2];
        copyToArray(queue, new_array);
        queue.head = 0;
        queue.tail = length;
        queue.elements = new_array;
    }

    private static void copyToArray(ArrayQueueADT queue, Object[] array) {
        if (queue.head >= queue.tail) {
            System.arraycopy(queue.elements, queue.head, array, 0, queue.elements.length - queue.head);
            System.arraycopy(queue.elements, 0, array, queue.elements.length - queue.head, queue.tail);
        } else {
            System.arraycopy(queue.elements, queue.head, array, 0, queue.tail - queue.head);
        }
    }

    // enqueue – добавить элемент в очередь
    // Pred: elem != null
    // Post: n' = n + 1 && a[n'] = elem && immutable(n)
    public static void enqueue(ArrayQueueADT queue, Object elem) {
        Objects.requireNonNull(elem);
        queue.elements[queue.tail++] = elem;
        queue.tail &= queue.elements.length - 1;
        if (queue.tail == queue.head) {
            doubleCapacity(queue);
        }
    }

    // element – первый элемент в очереди
    // Pred: n > 0
    // Post: R == a[1] && n' == n && immutable(n)
    public static Object element(ArrayQueueADT queue) {
        return queue.elements[queue.head];
    }

    // dequeue – удалить и вернуть первый элемент в очереди
    // Pred: n > 0
    // Post: R == a[1] && n' == n - 1 && for i=2..n: a'[i - 1] == a[i]
    public static Object dequeue(ArrayQueueADT queue) {
        Object result = queue.elements[queue.head];
        queue.elements[queue.head++] = null;
        queue.head &= queue.elements.length - 1;
        return result;
    }

    // size – текущий размер очереди
    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    public static int size(ArrayQueueADT queue) {
        return (queue.tail - queue.head) & (queue.elements.length - 1);
    }

    // isEmpty – является ли очередь пустой
    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.tail == queue.head;
    }

    // clear – удалить все элементы из очереди
    // Pred: true
    // Post: n' == 0
    public static void clear(ArrayQueueADT queue) {
        queue.elements = new Object[8];
        queue.head = 0;
        queue.tail = 0;
    }
    // push – добавить элемент в начало очереди
    // Pred: elem != null
    // Post: n' = n + 1 && a'[1] = elem && for i=1..n: a'[i + 1] == a[i]
    public static void push(ArrayQueueADT queue, Object elem) {
        Objects.requireNonNull(elem);
        queue.head = (queue.head - 1) & (queue.elements.length - 1);
        queue.elements[queue.head] = elem;
        if (queue.tail == queue.head) {
            doubleCapacity(queue);
        }
    }

    // peek – последний элемент в очереди
    // Pred: n > 0
    // Post: R == a[n] && n' == n && immutable(n)
    public static Object peek(ArrayQueueADT queue) {
        return queue.elements[(queue.tail - 1) & (queue.elements.length - 1)];
    }

    // remove – удалить и вернуть последний элемент в очереди
    // Pred: n > 0
    // Post: R == a[n] && n' == n - 1 && immutable(n')
    public static Object remove(ArrayQueueADT queue) {
        int new_tail = (queue.tail - 1) & (queue.elements.length - 1);
        Object result = queue.elements[new_tail];
        queue.elements[queue.tail] = null;
        queue.tail = new_tail;
        return result;
    }

    // toArray - массив, содержащий элементы, лежащие в очереди в порядке от головы к хвосту
    // Pred: true
    // Post: immutable(n) && n' == n && R is array && for i=1..n: R[i - 1] == a[i]
    public static Object[] toArray(ArrayQueueADT queue) {
        Object[] array = new Object[size(queue)];
        if (!isEmpty(queue)) {
            copyToArray(queue, array);
        }
        return array;
    }
}
