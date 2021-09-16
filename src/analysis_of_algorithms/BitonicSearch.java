package analysis_of_algorithms;

import edu.princeton.cs.algs4.StdOut;

public class BitonicSearch {

    public static int search(int[] a, int key) {
        int bitonicPoint = bitonicPoint(a, 0, a.length - 1);
        int result = searchAscending(a, key, 0, bitonicPoint);
        return (result == -1) ? searchDescending(a, key, bitonicPoint, a.length - 1) : result;
    }

    private static int bitonicPoint(int[] a, int lo, int hi) {
        int mid = (lo + hi) / 2;
        if(lo > hi) return -1;
        if(mid - 1 < 0 || mid + 1 >= a.length) return -1;
        if(a[mid - 1] < a[mid] && a[mid] > a[mid + 1])
            return mid;
        if(a[mid - 1] < a[mid] && a[mid] < a[mid + 1])
            return bitonicPoint(a, mid + 1, hi);
        if(a[mid - 1] > a[mid] && a[mid] > a[mid + 1])
            return bitonicPoint(a, lo, mid - 1);
        return -1;
    }

    private static int searchAscending(int[] a, int key, int lo, int hi) {
        int mid = (lo + hi) / 2;
        if(lo > hi) return -1;
        if(key == a[mid]) return mid;
        if(key < a[mid]) return searchAscending(a, key, lo, mid - 1);
        if(key > a[mid]) return searchAscending(a, key, mid + 1, hi);
        return -1;
    }

    private static int searchDescending(int[] a, int key, int lo, int hi) {
        int mid = (lo + hi) / 2;
        if(lo > hi) return -1;
        if(key == a[mid]) return mid;
        if(key < a[mid]) return searchDescending(a, key, mid + 1, hi);
        if(key > a[mid]) return searchDescending(a, key, lo, mid - 1);
        return -1;
    }

    public static void main(String[] args) {
        int[] sequence = {19, 50, 69, 72, 48, 32, 20};
        StdOut.println(BitonicSearch.search(sequence, 32));
    }
}