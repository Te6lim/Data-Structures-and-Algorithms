package bags_queues_stacks;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int last;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        array = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        validateItem(item);
        if (size > 0 && size == array.length) resize(array.length * 2);
        array[last++] = item;
        ++size;
    }

    private void validateItem(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void validateRemoval() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    // remove and return a random item
    public Item dequeue() {
        validateRemoval();
        int i = StdRandom.uniform(0, size);
        swapWithLastItem(i);
        Item item = array[--last];
        array[last] = null;
        --size;
        if (size > 0 && size == array.length / 4) resize(array.length / 2);
        return item;
    }

    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        for (int i = 0; i < size; ++i)
            temp[i] = array[i];
        array = temp;
    }

    private void swapWithLastItem(int i) {
        Item temp = array[i];
        array[i] = array[last - 1];
        array[last - 1] = temp;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        validateRemoval();
        int i = StdRandom.uniform(0, size);
        return array[i];
    }

    private void display() {
        StdOut.print("[");
        for (Item i : array) {
            if (i != null)
                StdOut.print(i + " ");
            else StdOut.print(" - ");
        }
        StdOut.println("]");
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int current = 0;
        final int[] positions = new int[size];
        public RandomizedQueueIterator() {
            int temp;
            for (int i = 0; i < size; ++i)
                positions[i] = i;

            for (int i = 0; i < size; ++i) {
                int rand = StdRandom.uniform(i + 1);
                temp = positions[i];
                positions[i] = positions[rand];
                positions[rand] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return array[positions[current++]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not in here; chief...");
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rQ = new RandomizedQueue<>();
        int N = Integer.parseInt(args[0]);
        StdOut.printf("----------ENQUEUING SEQUENCE----------\n");
        for (int i = 1; i <= N; ++i) {
            rQ.enqueue(i);
            StdOut.println(i + " was enqueued");
            rQ.display();
            StdOut.println("size= " + rQ.size);
            StdOut.println();
        }

        StdOut.printf("----------DE-QUEUING SEQUENCE----------\n");
        for (int i = 1; i <= N; ++i) {
            StdOut.println(rQ.dequeue() + " was dequeued");
            StdOut.println("size= " + rQ.size);
            rQ.display();
            StdOut.println();
        }

        StdOut.printf("----------ENQUEUING SEQUENCE----------\n");
        for (int i = 1 + N; i <= N + N; ++i) {
            rQ.enqueue(i);
            StdOut.println(i + " was enqueued");
            rQ.display();
            StdOut.println("size= " + rQ.size);
            StdOut.println();
        }

        StdOut.println("----------ITERATIONS----------");
        StdOut.print("[");
        for (int c : rQ)
            StdOut.print(c + " ");
        StdOut.println("]");
    }
}