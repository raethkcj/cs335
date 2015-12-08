package edu.uwec.cs.raethkcj.convexhullstarting;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuickHull implements ConvexHullFinder {

	@Override
	public List<Point2D> computeHull(List<Point2D> points) {
		
		//Find the leftmost and rightmost points
		Point2D left = new Point2D.Double(Double.MAX_VALUE, 0);
		Point2D right = new Point2D.Double(0, 0);
		for(Point2D point : points) {
			if(point.getX() < left.getX()) {
				left = point;
			}
			if(point.getX() > right.getX()) {
				right = point;
			}
		}
		
		//Construct a line connecting the leftmost to rightmost points
		Line2D line = new Line2D.Double(left, right);
		Line2D reverseLine = new Line2D.Double(right, left);
		
		//Divide the points into 2 sets, those above the line and those below the line
		ArrayList<Point2D> above = new ArrayList<Point2D>();
		ArrayList<Point2D> below = new ArrayList<Point2D>();
		
		for(Point2D point : points) {
			int relativeCCW = line.relativeCCW(point);
			if(relativeCCW > 0) {
				above.add(point);
			} else if (relativeCCW < 0) {
				below.add(point);
			}
		}
		
		ArrayList<Point2D> hull = new ArrayList<Point2D>();
		ArrayList<Point2D> hullAbove = (ArrayList<Point2D>) recursiveQuickHull(line, above);
		ArrayList<Point2D> hullBelow = (ArrayList<Point2D>) recursiveQuickHull(reverseLine, below);
		hull.add(right);
		hull.addAll(hullAbove);
		hull.add(left);
		hull.addAll(hullBelow);
		return hull;
	}
	
	private List<Point2D> recursiveQuickHull(Line2D lineAB, List<Point2D> pointsAB) {
		if(pointsAB.size() == 0) {
			return new ArrayList<Point2D>();
		} else if (pointsAB.size() == 1) {
			return pointsAB;
		} 
		//Given a baseline from point A to point B (lineAB) and a set of points above
		//this line (pointsAB), find the point C that is farthest from the line.
		Point2D c = pointsAB.get(0);
		for(Point2D point : pointsAB) {
			if(lineAB.ptLineDist(point) > lineAB.ptLineDist(c)) {
				c = point;
			}
		}
		Line2D lineAC = new Line2D.Double(lineAB.getP1(), c);
		Line2D lineCB = new Line2D.Double(c, lineAB.getP2());
		
		ArrayList<Point2D> pointsAC = new ArrayList<Point2D>();
		ArrayList<Point2D> pointsCB = new ArrayList<Point2D>();
		for(Point2D point : pointsAB) {
			if(lineAC.relativeCCW(point) > 0) {
				pointsAC.add(point);
			} else if (lineCB.relativeCCW(point) > 0) {
				pointsCB.add(point);
			}
		}
		ArrayList<Point2D> hull = new ArrayList<Point2D>();
		ArrayList<Point2D> hullAC = (ArrayList<Point2D>)recursiveQuickHull(lineAC, pointsAC);
		ArrayList<Point2D> hullBC = (ArrayList<Point2D>)recursiveQuickHull(lineCB, pointsCB);
		hull.addAll(hullBC);
		hull.add(c);
		hull.addAll(hullAC);
		return hull;
	}

}
