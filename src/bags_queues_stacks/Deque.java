package bags_queues_stacks;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Item[] array;
    private int top, bottom;
    private int size;

    // construct an empty deque
    public Deque() {
        array = (Item[]) new Object[1];
        top = -1;
        bottom = 1;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);
        if (top < 0) {
            if (size < 2) resize(2 * (size + 1));
            else resize(2 * (size));
        }
        array[top--] = item;
        ++size;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateItem(item);
        if (bottom >= array.length) {
            if (size < 2) resize(2 * (size + 1));
            else resize(2 * (size));
        }
        array[bottom++] = item;
        ++size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateRemoval();
        Item item = array[++top];
        array[top] = null;
        --size;
        if (size > 0 && size <= array.length / 4)
            resize(array.length / 2);

        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateRemoval();
        Item item = array[--bottom];
        array[bottom] = null;
        --size;
        if (size > 0 && size <= array.length / 4)
            resize(array.length / 2);

        return item;
    }

    private void resize(int cap) {
        Item[] temp = (Item[]) new Object[cap];
        int center = cap / 2;
        int start = (center - (size / 2));
        for (int i = start, j = (top + 1); i < start + size; i++, ++j)
            temp[i] = array[j];

        top = start - 1;
        bottom =  start + size;
        array = temp;
    }

    private void validateItem(Item item) {
        if (item == null) throw new IllegalArgumentException();
    }

    private void validateRemoval() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    private void display() {
        StdOut.print("[");
        for (Item i : array) {
            if (i != null)
                StdOut.print(i + " ");
            else StdOut.print(" - ");
        }
        StdOut.println("]");
        StdOut.println("top= " + top);
        StdOut.println("bottom= " + bottom);
        StdOut.println("size= " + size);
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        int current = top + 1;
        @Override
        public boolean hasNext() {
            return /*current < bottom && */ array[current] != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return array[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        int n = Integer.parseInt(args[0]);

        if (deque.isEmpty()) StdOut.println("deque is empty");
        else StdOut.println("deque is not empty");

        StdOut.println("----------ADDING----------");
        for (int i = 1; i <= n/2; ++i) {
            deque.addLast(i);
            deque.display();
        }

        StdOut.println();

        for (int i = (n/2) + 1; i <= n + 2; ++i) {
            deque.addFirst(i);
            deque.display();
        }

        StdOut.println("----------REMOVING----------");
        for (int i = 1; i <= n; ++i) {
            StdOut.println(deque.removeLast() + " was removed");
            deque.display();
        }

        StdOut.println("----------REMOVING----------");
        StdOut.println(deque.removeFirst() + " was removed");
        deque.display();

        StdOut.println("----------ADDING----------");
        for (int i = n + n; i <= n + 2*n + 3; ++i) {
            deque.addFirst(i);
            deque.display();
        }

        StdOut.println("----------ITERATOR----------");
        StdOut.print("[");
        for(int c : deque)
            StdOut.print(" " + c);
        StdOut.println(" ]");

    }
}