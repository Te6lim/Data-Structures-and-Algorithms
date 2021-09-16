package bags_queues_stacks;

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LLDeque<Item> implements Iterable<Item> {

    private Node first, last;

    private int size;

    // construct an empty deque
    public LLDeque() { }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        validateItem(item);
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.right = oldFirst;
        if(oldFirst == null) last = first;
        else oldFirst.left = first;
        ++size;
    }

    // add the item to the back
    public void addLast(Item item) {
        validateItem(item);
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.left = oldLast;
        if(oldLast == null) first = last;
        else oldLast.right = last;
        ++size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        validateRemoval();
        Item item = first.item;
        first = first.right;
        if(first != null) {
            first.left = null;
        } else last = null;
        --size;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        validateRemoval();
        Item item = last.item;
        last = last.left;
        if(last != null) {
            last.right = null;
        } else first = null;
        --size;
        return item;
    }

    private class Node {
        private Item item;
        private Node left;
        private Node right;
    }

    private void validateItem(Item item) {
        if(item == null) throw new IllegalArgumentException();
    }
    private void validateRemoval() {
        if(isEmpty()) throw new NoSuchElementException();
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.right;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        LLDeque<Integer> deque = new LLDeque<>();
        //for(int i = 0; i < 10; ++i)
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);

        StdOut.println(deque.removeLast() + " was removed");
        StdOut.println(deque.removeFirst() + " was removed");
        StdOut.println(deque.removeLast() + " was removed");
        StdOut.println(deque.removeFirst() + " was removed");
        StdOut.println(deque.removeLast() + " was removed");
        StdOut.println(deque.removeFirst() + " was removed");
        StdOut.println();
        for(int c : deque)
            StdOut.println(c);

        StdOut.println("\nSize: " + deque.size());
        StdOut.println("is the deque empty? " + deque.isEmpty());

    }
}