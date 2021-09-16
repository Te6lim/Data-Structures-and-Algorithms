package mergesort;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private int n;
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        validate(points);
        Point[] setOfPoints = new Point[points.length];
        System.arraycopy(points, 0, setOfPoints, 0, setOfPoints.length);
        Arrays.sort(setOfPoints);
        lineSegments = new LineSegment[setOfPoints.length];

        for (int p = 0; p < setOfPoints.length; ++p) {
            double lastKnownSlope = Double.NaN;
            boolean hasFoundSegmentOfP;
            for (int q = p + 1; q < setOfPoints.length; ++q) {
                double slopeFromPtoQ = setOfPoints[p].slopeTo(setOfPoints[q]);
                hasFoundSegmentOfP = false;
                for (int r = q + 1; r < setOfPoints.length; ++r) {
                    if (lastKnownSlope == slopeFromPtoQ)
                        break;
                    for (int s = r + 1; s < setOfPoints.length; ++s) {
                        if (slopeFromPtoQ == setOfPoints[q].slopeTo(setOfPoints[r]) &&
                                setOfPoints[q].slopeTo(setOfPoints[r]) == setOfPoints[r].slopeTo(setOfPoints[s])) {
                            if (lastKnownSlope != slopeFromPtoQ) {
                                lineSegments[n++] = new LineSegment(setOfPoints[p], setOfPoints[s]);
                                lastKnownSlope = slopeFromPtoQ;
                                hasFoundSegmentOfP = true;
                            }
                        }
                        if (hasFoundSegmentOfP) break;
                    }
                    if (hasFoundSegmentOfP) break;
                }
            }
        }
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

         /* mergesort.Point[] points = {
                new mergesort.Point(1, 1), new mergesort.Point(2, 2),
                new mergesort.Point(3, 3), new mergesort.Point(4, 4),
                new mergesort.Point(5, 5), new mergesort.Point(1, 2),
                new mergesort.Point(2, 4), new mergesort.Point(3, 6),
                new mergesort.Point(4, 8)
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

        StdOut.printf("Number of line segments= %d\n", collinear.numberOfSegments());


        // sorted points
        /* // mergesort.Point[] points = {
                new mergesort.Point(1, 1), new mergesort.Point(2, 2),
                new mergesort.Point(3, 3), new mergesort.Point(4, 4),
                new mergesort.Point(5, 5), new mergesort.Point(1, 2),
                new mergesort.Point(2, 4), new mergesort.Point(3, 6),
                new mergesort.Point(4, 8)
        };*/

        // unsorted points
        /* // mergesort.Point[] points = {
                new mergesort.Point(3, 6), new mergesort.Point(4, 4),
                new mergesort.Point(5, 5), new mergesort.Point(1, 2),
                new mergesort.Point(2, 4),
                new mergesort.Point(2, 2), new mergesort.Point(1, 1),
                new mergesort.Point(4, 8), new mergesort.Point(3, 3)
        };

        mergesort.BruteCollinearPoints fcp = new mergesort.BruteCollinearPoints(points);
        StdOut.println("Number of segments is= " + fcp.numberOfSegments());
        for (mergesort.LineSegment ls : fcp.segments())
            StdOut.println(ls);*/
    }
}