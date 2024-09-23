package queue;

import base.Asserts;

import java.util.Deque;
import java.util.Random;
import java.util.ArrayDeque;

public class ArrayQueueMyTest {
    static final int testNumber = 1000;
    static final ArrayQueueADT queueADT = ArrayQueueADT.create();
    static final ArrayQueue queue = new ArrayQueue();
    private static final Deque<Object> controlQueue = new ArrayDeque<>();
    private static final Random randomizer = new Random();

    enum Type {
        MODULE, ADT, CLASS
    }

    static Type type;

    static final Object[] items = new Object[]{"Lorem", "ipsum", "dolor", "sit", "amet"};

    public static void enqueue(Object element) {
        switch (type) {
            case MODULE -> ArrayQueueModule.enqueue(element);
            case ADT -> ArrayQueueADT.enqueue(queueADT, element);
            case CLASS -> queue.enqueue(element);
        }
    }

    public static void push(Object element) {
        switch (type) {
            case MODULE -> ArrayQueueModule.push(element);
            case ADT -> ArrayQueueADT.push(queueADT, element);
            case CLASS -> queue.push(element);
        }
    }

    public static void clear() {
        switch (type) {
            case MODULE -> ArrayQueueModule.clear();
            case ADT -> ArrayQueueADT.clear(queueADT);
            case CLASS -> queue.clear();
        }
    }

    public static boolean isEmpty() {
        return switch (type) {
            case MODULE -> ArrayQueueModule.isEmpty();
            case ADT -> ArrayQueueADT.isEmpty(queueADT);
            case CLASS -> queue.isEmpty();
        };
    }

    public static Object dequeue() {
        return switch (type) {
            case MODULE -> ArrayQueueModule.dequeue();
            case ADT -> ArrayQueueADT.dequeue(queueADT);
            case CLASS -> queue.dequeue();
        };
    }

    public static Object remove() {
        return switch (type) {
            case MODULE -> ArrayQueueModule.remove();
            case ADT -> ArrayQueueADT.remove(queueADT);
            case CLASS -> queue.remove();
        };
    }

    public static void checkSize(int controlSize) {
        int checkedSize = switch (type) {
            case MODULE -> ArrayQueueModule.size();
            case ADT -> ArrayQueueADT.size(queueADT);
            case CLASS -> queue.size();
        };
        Asserts.assertEquals("wrong size of queue", controlSize, checkedSize);
    }

    public static void checkElement(Object controlElement) {
        Object checkedElement = switch (type) {
            case MODULE -> ArrayQueueModule.element();
            case ADT -> ArrayQueueADT.element(queueADT);
            case CLASS -> queue.element();
        };
        Asserts.assertEquals("wrong last element", controlElement, checkedElement);
    }

    public static void checkPeek(Object controlElement) {
        Object checkedElement = switch (type) {
            case MODULE -> ArrayQueueModule.peek();
            case ADT -> ArrayQueueADT.peek(queueADT);
            case CLASS -> queue.peek();
        };
        Asserts.assertEquals("wrong peek", controlElement, checkedElement);
    }

    public static Object newRandomElement(Random randomizer) {
        Object element = items[randomizer.nextInt(items.length)];
        if (element.equals(items[0])) {
            element = randomizer.nextInt(10000);
        }
        return element;
    }

    public static void test() {
        for (int i = 0; i < testNumber; i++) {
            try {
                runTest(randomizer.nextInt(17));
            } catch (AssertionError error) {
                System.err.println("Problem at test " + i + ": " + error);
            }
            if (i % (testNumber / 5) == 0) {
                System.out.println(i + " tests passed");
            }
        }
    }

    public static void runTest(int testType) {
        if (testType < 3) {
            testDequeue();
        } else if (testType < 9) {
            testEnqueue();
        } else if (testType < 14) {
            testPush();
        } else if (testType < 16) {
            testRemove();
        } else {
            testClear();
        }
        testState();
    }

    public static void testDequeue() {
        Asserts.assertEquals("wrong value of empty", controlQueue.isEmpty(), isEmpty());
        if (!isEmpty()) {
            Asserts.assertEquals("wrong last element", controlQueue.remove(), dequeue());
        }
    }

    public static void testRemove() {
        Asserts.assertEquals("wrong value of empty", controlQueue.isEmpty(), isEmpty());
        if (!isEmpty()) {
            Asserts.assertEquals("wrong remove", controlQueue.removeLast(), remove());
        }
    }

    public static void testEnqueue() {
        Object new_element = newRandomElement(randomizer);
        controlQueue.add(new_element);
        enqueue(new_element);
    }

    public static void testPush() {
        Object new_element = newRandomElement(randomizer);
        controlQueue.push(new_element);
        push(new_element);
    }

    public static void testClear() {
        controlQueue.clear();
        clear();
    }

    public static void testState() {
        Asserts.assertEquals("wrong value of empty", controlQueue.isEmpty(), isEmpty());
        if (!isEmpty()) {
            checkElement(controlQueue.element());
            checkPeek(controlQueue.peekLast());
        }
        checkSize(controlQueue.size());
    }

    public static void main(String[] args) {
        System.out.println("starting test");

        type = Type.MODULE;
        test();
        controlQueue.clear();
        System.out.println(testNumber + " tests passed successfully");

        type = Type.ADT;
        test();
        controlQueue.clear();
        System.out.println(testNumber + " tests passed successfully");

        type = Type.CLASS;
        test();
        System.out.println(testNumber + " tests passed successfully");

        System.out.println("all right");
    }
}
