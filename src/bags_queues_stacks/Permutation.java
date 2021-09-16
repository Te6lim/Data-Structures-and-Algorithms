package bags_queues_stacks;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rQ = new RandomizedQueue<>();

        while (!StdIn.isEmpty())
            rQ.enqueue(StdIn.readString());

        for (int i = 0; i < k; ++i) {
            StdOut.println(rQ.dequeue());
        }
    }
}