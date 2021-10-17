package BST;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) StdDraw.point(point.x(), point.y());
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        ArrayList<Point2D> intersectingPoints = new ArrayList<>();

        if (points.isEmpty()) return null;
        for (Point2D point : points)
            if (rect.contains(point)) intersectingPoints.add(point);

        return intersectingPoints;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D query) {
        validate(query);
        Point2D nearestPoint = null;
        if (points.isEmpty()) return null;
        for (Point2D point : points) {
            if (nearestPoint != null) {
                if (point.distanceSquaredTo(query) < nearestPoint.distanceSquaredTo(query)) nearestPoint = point;
            } else nearestPoint =  point;
        }
        return nearestPoint;
    }

    private void validate(Object o) {
        if (o == null) throw new IllegalArgumentException();
    }

    public static void main(String[] args) {

    }

}