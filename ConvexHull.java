// BERKE NURI
// MADDIE LONDON

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class ConvexHull extends JPanel {

    SwingShell parent = null;
    LinkedList<Point> vertices = null;
    LinkedList<Point> hull = null;
    Color currentColor = Color.red;

    public ConvexHull(SwingShell _parent) {
	super();
	parent = _parent;
	vertices = parent.vertices;
	hull = parent.hull;
    }

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(currentColor);

		ListIterator iterator = vertices.listIterator(0);

		Point currentVertex = null;

		for (int i = 0; i < vertices.size(); ++i) {
			currentVertex = (Point) iterator.next();
			g.fillOval(currentVertex.x - parent.NODE_RADIUS, currentVertex.y - parent.NODE_RADIUS,
					2 * parent.NODE_RADIUS, 2 * parent.NODE_RADIUS);
		}

		// Draws the convex hull vertices one by one
		for (int i=0; i < hull.size() -1; i++) {
			g.drawLine(hull.get(i).x, hull.get(i).y, hull.get(i+1).x, hull.get(i+1).y);
				
			// Connects the last and the first vertex
		    	if (i == (hull.size()-2))
		    		g.drawLine(hull.get(i+1).x, hull.get(i+1).y, hull.get(0).x, hull.get(0).y);
		}
	}

	public void changeColor() {

		if (currentColor.equals(Color.red)) {
			currentColor = Color.yellow;
		} else {
			currentColor = Color.red;
		}
	}

	// Returns convex hull of a set of n points.
    public LinkedList<Point> convexHull()
    {
    	  // Initialized points as an array for quicker and easier access to its specific elements
    		Point[] points = (Point[]) vertices.toArray(new Point[vertices.size()]);

        // There must be at least 2 points to compute the convex hull
        if (vertices.size() < 2) return null;

        // Find the leftmost point
        int leftmost = 0;
        for (int i = 1; i < vertices.size(); i++)
            if (points[i].getX() < points[leftmost].getX())
                leftmost = i;

        // Start at leftmost point and move counterclockwise until the starting point is reached again
        int x = leftmost;
        int y;
        int j = 0;

        	while (x != leftmost || j == 0) { // We want to count the first point, but only once
            // Add current point to result
            hull.add(points[x]);

            // Search for a point 'y' such that is counterclockwise for all points of x
            y = (x + 1) % vertices.size();

            for (int i = 0; i < vertices.size(); i++)
            {
               // If i is more counterclockwise than our current y, than update y to be the most counterclockwise point
               if (rotation(points[x], points[i], points[y]) < 0)
                   y = i;
            }

            // Set x as y for next iteration, so  y is added to hull
            x = y;
            j++;

        }
        return hull;
    }

	// Calculates the slope of the three points to determine the rotation to be either colinear, clockwise, or counterclockwise
	public static int rotation(Point p1, Point p2, Point p3) {

		double foo = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX()) -
				(p2.getX() - p1.getX()) * (p3.getY() - p2.getY());

		if (foo == 0)
			return 0; // colinear
		else if (foo > 1)
			return 1; // clockwise
		else
			return -1; // counterclockwise

	}
}
