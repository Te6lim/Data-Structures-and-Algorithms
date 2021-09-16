package mergesort;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] lineSegments;
    private final Point[] startSegment;
    private final Point[] endSegment;
    private int n;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        validate(points);

        Point[] setOfPoints = new Point[points.length];
        System.arraycopy(points, 0, setOfPoints, 0, setOfPoints.length);

        int size = (int) Math.pow(setOfPoints.length, 2);
        lineSegments = new LineSegment[size];
        startSegment = new Point[size];
        endSegment = new Point[size];

        for (int i = 0; i < setOfPoints.length - 3; ++i) {
            Arrays.sort(setOfPoints, i, setOfPoints.length);
            findSegmentByIthPosition(i + 1, setOfPoints);
        }
    }

    private void findSegmentByIthPosition(int start, Point[] setOfPoints) {
        Arrays.sort(setOfPoints, start, setOfPoints.length, setOfPoints[start - 1].slopeOrder());

        double  prevSlope = setOfPoints[start - 1].slopeTo(setOfPoints[start]);
        int slopeCount = 1;

        for (int i = start + 1; i < setOfPoints.length; ++i) {

            if (prevSlope == setOfPoints[start - 1].slopeTo(setOfPoints[i])) {
                ++slopeCount;
                if (i == setOfPoints.length - 1 && slopeCount >= 3) {

                    if (isNotSubsegment(setOfPoints[start - 1], setOfPoints[i]))
                        addSegment(setOfPoints[start - 1], setOfPoints[i]);
                }
            } else {
                if (slopeCount >= 3) {

                    if (isNotSubsegment(setOfPoints[start - 1], setOfPoints[i - 1]))
                        addSegment(setOfPoints[start - 1], setOfPoints[i - 1]);
                }
                slopeCount = 1;
                prevSlope = setOfPoints[start - 1].slopeTo(setOfPoints[i]);
            }
        }
    }

    private void addSegment(Point source, Point point) {
        lineSegments[n++] = new LineSegment(source, point);
        startSegment[n - 1] = source;
        endSegment[n - 1] = point;
    }

    private boolean isNotSubsegment(Point source , Point point) {
        for (int j = 0; j < endSegment.length && endSegment[j] != null; ++j) {
            if (point == endSegment[j])
                if (source.slopeTo(startSegment[j]) == startSegment[j].slopeTo(point))
                    return false;
        }
        return true;
    }

    // the number of line segments
    public int numberOfSegments() {
        return n;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segments = new LineSegment[n];
        System.arraycopy(lineSegments, 0, segments, 0, n);
        return segments;
    }

    private void validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException("The argument is null!");
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == null) throw new IllegalArgumentException("Some item(s) in the argument is null!");
            for (int j = i + 1; j < points.length; ++j) {
                if (points[j] == null) throw new IllegalArgumentException("Some item(s) in the argument is null!");
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException("some item(s) is/are repeated!");
            }
        }
    }

    /*/ private void display(mergesort.Point[] points) {
        StdOut.print("[ ");
        for (mergesort.Point p : points)
            StdOut.print(p + " ");
        StdOut.println("]");
    }*/

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        /*/mergesort.Point[] points = {
                new mergesort.Point(1, 1), new mergesort.Point(2, 2),
                new mergesort.Point(3, 3), new mergesort.Point(4, 4),
                new mergesort.Point(5, 5), new mergesort.Point(8, 16),
                new mergesort.Point(1, 2), new mergesort.Point(2, 4),
                new mergesort.Point(3, 6), new mergesort.Point(4, 8),
                new mergesort.Point(5, 10), new mergesort.Point(1, 3),
                new mergesort.Point(1, 4), new mergesort.Point(1, 5),
                new mergesort.Point(1, 8), new mergesort.Point(2, 8),
                new mergesort.Point(3, 8),
                new mergesort.Point(5, 8), new mergesort.Point(6, 8),
        };*/

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        /*/ mergesort.Point[] points = {
                new mergesort.Point(1, 1), new mergesort.Point(2, 2),
                new mergesort.Point(3, 3), new mergesort.Point(4, 4),
                new mergesort.Point(5, 5), new mergesort.Point(8, 16),
                new mergesort.Point(1, 2), new mergesort.Point(2, 4),
                new mergesort.Point(3, 6), new mergesort.Point(4, 8),
                new mergesort.Point(5, 10), new mergesort.Point(1, 3),
                new mergesort.Point(1, 4), new mergesort.Point(1, 5)
        };*/

        /*/ mergesort.FastCollinearPoints fcp = new mergesort.FastCollinearPoints(points);
        StdOut.println("Number of segments is= " + fcp.numberOfSegments());
        for (mergesort.LineSegment ls : fcp.segments())
            StdOut.println(ls);*/

        StdOut.println("Number of line segments= " + collinear.numberOfSegments());
    }
}