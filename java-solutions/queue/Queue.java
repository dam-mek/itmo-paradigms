package queue;

// Model: a[1]...a[n]
// Inv: n >= 0
// Let immutable(n): for i=1..n: a'[i] == a[i]

interface Queue {

    // enqueue – добавить элемент в очередь
    // Pred: elem != null
    // Post: n' = n + 1 && a[n'] = elem && immutable(n)
    void enqueue(Object elem);

    // element – первый элемент в очереди
    // Pred: !isEmpty()
    // Post: R == a[1] && n' == n && immutable(n)
    Object element();

    // dequeue – удалить и вернуть первый элемент в очереди
    // Pred: !isEmpty()
    // Post: R == a[1] && n' == n - 1 && for i=2..n: a'[i - 1] == a[i]
    Object dequeue();

    // size – текущий размер очереди
    // Pred: true
    // Post: R == n && n' == n && immutable(n)
    int size();

    // isEmpty – является ли очередь пустой
    // Pred: true
    // Post: R == (n == 0) && n' == n && immutable(n)
    boolean isEmpty();

    // clear – удалить все элементы из очереди
    // Pred: true
    // Post: n' == 0
    void clear();

    // contains - проверяет, содержится ли элемент в очереди
    // Pred: true
    // Post: R == true if exists i: a[i] == elem
    boolean contains(Object elem);

    // removeFirstOccurrence - удаляет первое вхождение элемента в очередь и возвращает было ли такое
    // Let exist: exists j: a[j] == elem
    // Pred: true
    // Post: R == true if exist &&
    // (exist && n' == n - 1 && for i in 0..j: a'[i] == a[i] && for i in j..n': a'[i] == a[i + 1]
    // || !exist && n' == n && immutable(n)) else R == false
    boolean removeFirstOccurrence(Object elem);
}
