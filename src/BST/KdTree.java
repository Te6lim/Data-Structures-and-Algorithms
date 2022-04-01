package BST;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class KdTree {

    private static class Node implements Comparable<Node> {
        private Node left;
        private Node right;
        private final Point2D point;
        private boolean isHorizontal;


        public Node(Point2D p) {
            point = p;
            isHorizontal = false;
        }

        @Override
        public int compareTo(Node that) {
            int cmp;
            if (!isHorizontal) {
                cmp = Double.compare(point.x(), that.point.x());
                if (cmp == 0) return Double.compare(point.y(), that.point.y());
                return cmp;
            }
            else {
                cmp = Double.compare(point.y(), that.point.y());
                if (cmp == 0) return Double.compare(point.x(), that.point.x());
                return cmp;
            }
        }
    }

    private Node root;
    private final RectHV rootRect;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
        rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        root = put(root, new Node(p));
    }

    private Node put(Node rootNode, Node newNode) {
        if (rootNode == null) {
            ++size;
            return newNode;
        }
        int cmp = newNode.compareTo(rootNode);
        newNode.isHorizontal = !rootNode.isHorizontal;
        if (cmp < 0) rootNode.left = put(rootNode.left, newNode);
        if (cmp > 0) rootNode.right = put(rootNode.right, newNode);
        return rootNode;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return contains(root, p);
    }

    private boolean contains(Node node, Point2D point) {
        if (node == null) return false;
        if (node.point.equals(point)) return true;
        if (node.isHorizontal) {
            if (point.y() < node.point.y()) return contains(node.left, point);
            if (point.y() == node.point.y()) {
                if (point.x() < node.point.x()) return contains(node.left, point);
                return contains(node.right, point);
            }
            return contains(node.right, point);
        }
        if (point.x() < node.point.x()) return contains(node.left, point);
        if (point.x() == node.point.x()) {
            if (point.y() < node.point.y()) return contains(node.left, point);
            return contains(node.right, point);
        }
        return contains(node.right, point);
    }

    // draw all points to standard draw
    public void draw() {
        draw(root, rootRect);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        ArrayList<Point2D> points = new ArrayList<>();
        findPointsInRect(root, rootRect, rect, points);
        return points;
    }

    private void findPointsInRect(Node node, RectHV rRect, RectHV rect, ArrayList<Point2D> points) {
        if (node == null) return;

        if (isInRectangle(node.point, rect)) {
            points.add(node.point);
            findPointsInRect(node.left, rRect, rect, points);
            findPointsInRect(node.right, rRect, rect, points);
            return;
        }

        RectHV leftRect = constructRect(node, true, rRect);
        RectHV rightRect = constructRect(node, false, rRect);

        if (splitsQueryRect(rect, node)) {
            findPointsInRect(node.left, leftRect, rect, points);
            findPointsInRect(node.right, rightRect, rect, points);
            return;
        }

        if (rect.intersects(leftRect)) findPointsInRect(node.left, leftRect, rect, points);
        else findPointsInRect(node.right, rightRect, rect, points);
    }

    private RectHV constructRect(Node node, boolean left, RectHV rRect) {
         if (node.isHorizontal) {
             if (left) return new RectHV(rRect.xmin(), rRect.ymin(), rRect.xmax(), node.point.y());
             return new RectHV(rRect.xmin(), node.point.y(), rRect.xmax(), rRect.ymax());
         }

        if (left) return new RectHV(rRect.xmin(), rRect.ymin(), node.point.x(), rRect.ymax());
        return new RectHV(node.point.x(), rRect.ymin(), rRect.xmax(), rRect.ymax());
    }

    private boolean splitsQueryRect(RectHV rect, Node node) {
        double x = node.point.x();
        if (node.isHorizontal) {
            double y = node.point.y();
            return rect.ymin() <= y && y <= rect.ymax();
        }
        return rect.xmin() <= x && x <= rect.xmax();
    }

    private boolean isInRectangle(Point2D point, RectHV rect) {
        double x = point.x();

        if (rect.xmin() <= x && x <= rect.xmax()) {
            double y = point.y();
            return rect.ymin() <= y && y <= rect.ymax();
        }
        return false;
    }

    private void draw(Node node, RectHV rect) {

        if (node == null) return;

        drawLine(node, rect);
        StdDraw.setPenColor(StdDraw.BLACK);
        drawPoint(node.point);

        draw(node.left, constructRect(node, true, rect));
        draw(node.right, constructRect(node, false, rect));
    }

    private void drawPoint(Point2D point) {
        StdDraw.setPenRadius(0.005);
        StdDraw.point(point.x(), point.y());
        StdDraw.show();
    }

    private void drawLine(Node node, RectHV rect) {
        StdDraw.setPenRadius();
        if (!node.isHorizontal) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
        }
        StdDraw.show();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        if (root != null) return nearest(root, p, root, rootRect).point;
        return null;
    }

    private Node nearest(Node node, Point2D query, Node nearest, RectHV rRect) {
        if (node == null) return nearest;
        Node champ = champ(node, query, nearest);
        RectHV rect;
        if (!node.isHorizontal) {
            if (query.x() < node.point.x()) {
                champ = nearest(node.left, query, champ, rRect);
                rect = constructRect(node, false, rRect);
                if (champ.point.distanceSquaredTo(query) > rect.distanceSquaredTo(query))
                    champ = nearest(node.right, query, champ, rRect);
            } else {
                champ = nearest(node.right, query, champ, rRect);
                rect = constructRect(node, true, rRect);
                if (champ.point.distanceSquaredTo(query) > rect.distanceSquaredTo(query))
                    champ = nearest(node.left, query, champ, rRect);
            }
        } else {
            if (query.y() < node.point.y()) {
                champ = nearest(node.left, query, champ, rRect);
                rect = constructRect(node, false, rRect);
                if (champ.point.distanceSquaredTo(query) > rect.distanceSquaredTo(query))
                    champ = nearest(node.right, query, champ, rRect);
            } else {
                champ = nearest(node.right, query, champ, rRect);
                rect = constructRect(node, true, rRect);
                if (champ.point.distanceSquaredTo(query) > rect.distanceSquaredTo(query))
                    champ = nearest(node.left, query, champ, rRect);
            }
        }
        return champ;
    }

    private Node champ(Node node, Point2D query, Node nearest) {
        if (node.point.distanceSquaredTo(query) <= nearest.point.distanceSquaredTo(query))
            return node;
        return nearest;
    }

    private void validate(Object obj) {
        if (obj == null) throw new IllegalArgumentException();
    }

    private static double roundOff(double d) {
        int n = 1;
        for (int r = 0; r < 4; ++r) n *= 10;

        double roundOff = Math.round(d * n);
        return roundOff / n;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();

        double pointX;
        double pointY;

        for (int i = 0; i < 1000; ++i) {
            pointX = StdRandom.uniform(0.0, 1.0);
            pointY = StdRandom.uniform(0.0, 1.0);
            tree.insert(new Point2D(KdTree.roundOff(pointX), KdTree.roundOff(pointY)));
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        tree.draw();

        double xMin = StdRandom.uniform(0.0, 1.0);
        double yMin = StdRandom.uniform(0.0, 1.0);
        double xMax = StdRandom.uniform(xMin, 1.0);
        double yMax = StdRandom.uniform(yMin, 1.0);

        RectHV rect = new RectHV(KdTree.roundOff(xMin), KdTree.roundOff(yMin),
                KdTree.roundOff(xMax), KdTree.roundOff(yMax));

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.setPenRadius(0.005);
        StdDraw.line(xMin, yMin, xMax, yMin);
        StdDraw.line(xMax, yMin, xMax, yMax);
        StdDraw.line(xMax, yMax, xMin, yMax);
        StdDraw.line(xMin, yMax, xMin, yMin);
        StdDraw.show();

        StdDraw.setXscale(0.0, 1.0);
        StdDraw.setYscale(0.0, 1.0);
        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.line(xMin, yMin, xMax, yMin);
        StdDraw.line(xMax, yMin, xMax, yMax);
        StdDraw.line(xMax, yMax, xMin, yMax);
        StdDraw.line(xMin, yMax, xMin, yMin);

        StdDraw.setPenColor(StdDraw.GREEN);
        for (Point2D point : tree.range(rect)) tree.drawPoint(point);
        StdOut.println(tree.nearest(new Point2D(0.5, 0.5)));

    }
}