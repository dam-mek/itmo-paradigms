package queue;

public class LinkedQueue extends AbstractQueue {

    private Node head;
    private Node tail;
    private int size;

    public LinkedQueue() {
        head = new Node(null, null);
        tail = head;
    }

    @Override
    public void enqueueImpl(Object elem) {
        size++;
        tail.next = new Node(elem, null);
        tail = tail.next;
    }

    @Override
    public Object elementImpl() {
        return head.next.value;
    }

    @Override
    protected void delete() {
        head = head.next;
        size--;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
        head = new Node(null, null);
        tail = head;
    }

    @Override
    public boolean contains(Object elem) {
        if (isEmpty()) {
            return false;
        }
        Node current = head.next;
        while (current != null) {
            if (current.value.equals(elem)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    @Override
    public boolean removeFirstOccurrence(Object elem) {
        Node previous = head;
        Node current = head.next;
        while (current != null) {
            if (current.value.equals(elem)) {
                previous.next = current.next;
                if (tail == current) {
                    tail = previous;
                }
                size--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }

    private static class Node {
        private final Object value;
        private Node next;

        public Node(Object value, Node next) {
            this.value = value;
            this.next = next;
        }
    }
}
