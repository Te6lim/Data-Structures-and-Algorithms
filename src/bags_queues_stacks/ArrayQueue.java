package bags_queues_stacks;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class ArrayQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int top, bottom;
    private int size;

    public ArrayQueue() {
        array = (Item[]) new Object[1];
    }

    public void enqueue(Item item) {
        if(isFull()) resize(array.length + ( bottom - (2 * top) ) );
        array[bottom++] = item;
        ++size;
    }

    public Item dequeue() {
        if(top < bottom) {
            Item item = array[top++];
            if (top > 0 && size == array.length / 4) resize(array.length / 2);
            --size;
            return item;
        }
        throw new IndexOutOfBoundsException();
    }

    public int size() {
        return size;
    }

    private boolean isFull() {
        return bottom == array.length;
    }

    private boolean isEmpty() {
        return size == 0;
    }

    public int arraySize() {
        return array.length;
    }

    private void resize(int cap) {
        StdOut.println("New size= " + cap);
        Item[] temp = (Item[]) new Object[cap];
        for(int i = 0; i < size; ++i)
            if(i + top < array.length)
                temp[i] = array[i + top];
            bottom -= top;
            top = 0;
        array = temp;
    }

    @Override
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        int pointer = top;
        @Override
        public boolean hasNext() {
            return pointer < bottom;
        }

        @Override
        public Item next() {
            return array[pointer++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("That shit ain't allowed in this bitch!");
        }
    }

    public static void main(String[] args) {
        ArrayQueue<Integer> queue = new ArrayQueue<>();
        StdOut.println("the queue is empty? " + queue.isEmpty());

        for(int i = 0; i < 6; i++) {
            queue.enqueue(i + 1);
            StdOut.println( i + 1 + " was enqueued");
            StdOut.print("Top= " + queue.top);
            StdOut.print(" Bottom= " + queue.bottom);
            StdOut.println(" Size= " + queue.size);
            StdOut.println("queue is empty? " + queue.isEmpty());
            StdOut.println();
        }

        for(int i = 0; i < 5; ++i) {
            queue.dequeue();
            StdOut.println( i + 1 + " dequeued");
            StdOut.print("Top= " + queue.top);
            StdOut.print(" Bottom= " + queue.bottom);
            StdOut.println(" Size= " + queue.size);
            StdOut.println("queue is empty? " + queue.isEmpty());
            StdOut.println();
        }

        for(int i = 7; i < 14; i++) {
            queue.enqueue(i + 1);
            StdOut.println( i + 1 + " was enqueued");
            StdOut.print("Top= " + queue.top);
            StdOut.print(" Bottom= " + queue.bottom);
            StdOut.println(" Size= " + queue.size);
            StdOut.println("queue is empty? " + queue.isEmpty());
            StdOut.println();
        }

        StdOut.println("queue is empty? " + queue.isEmpty());

        for(int c : queue)
            StdOut.println(c);

        StdOut.println("Final array size: " + queue.arraySize());

        StdOut.println("\nsize of the queue: " + queue.size());
    }
}