package edu.uwec.cs.raethkcj.convexhullstarting;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MergeHull implements ConvexHullFinder {

	@Override
	public List<Point2D> computeHull(List<Point2D> points) {
		Collections.sort(points, new PointComparator());
		return recursiveMergeHull(points);
	}

	private List<Point2D> recursiveMergeHull(List<Point2D> points) {
		if (points.size() == 1) {
			return points;
		}
		List<Point2D> leftHull = recursiveMergeHull(points.subList(0, points.size() / 2));
		List<Point2D> rightHull = recursiveMergeHull(points.subList(points.size() / 2, points.size()));

		int i = 0;
		int a = 0;
		for (Point2D point : leftHull) {
			if (point.getX() > leftHull.get(a).getX()) {
				a = i;
			}
			i++;
		}
		i = 0;
		int b = 0;
		for (Point2D point : rightHull) {
			if (point.getX() < leftHull.get(b).getX()) {
				b = i;
			}
			i++;
		}

		Line2D bottomTangent = findTangent(a, b, leftHull, rightHull);
		Line2D topTangent = findTangent(b, a, rightHull, leftHull);
		ArrayList<Point2D> hull = new ArrayList<Point2D>();

		hull.add(bottomTangent.getP1());
		i = 0;
		while (!rightHull.get(i).equals(bottomTangent.getP2())) {
			i++;
		}
		while (!rightHull.get(Math.floorMod(i, rightHull.size())).equals(topTangent.getP1())) {
			hull.add(rightHull.get(Math.floorMod(i, rightHull.size())));
			i++;
		}

		hull.add(topTangent.getP1());
		i = 0;
		while (!leftHull.get(i).equals(topTangent.getP2())) {
			i++;
		}
		while (!leftHull.get(Math.floorMod(i, leftHull.size())).equals(bottomTangent.getP1())) {
			hull.add(leftHull.get(Math.floorMod(i, leftHull.size())));
			i++;
		}

		return hull;
	}

	private Line2D findTangent(int a, int b, List<Point2D> leftHull, List<Point2D> rightHull) {
		Line2D tangent = new Line2D.Double(leftHull.get(a), rightHull.get(b));
		while (!isTangentLine(tangent, leftHull, a) || !isTangentLine(tangent, rightHull, b)) {
			while (!isTangentLine(tangent, leftHull, a)) {
				a = Math.floorMod(a - 1, leftHull.size());
				tangent.setLine(leftHull.get(a), rightHull.get(b));
			}
			while (!isTangentLine(tangent, rightHull, b)) {
				b = Math.floorMod(b + 1, rightHull.size());
				tangent.setLine(leftHull.get(a), rightHull.get(b));
			}
		}
		return tangent;
	}

	private boolean isTangentLine(Line2D line, List<Point2D> hull, int pointIndex) {
		//Use Math.floorMod instead of % because Java is weird
		int pointIndexCW = 	Math.floorMod(pointIndex - 1, hull.size());
		int pointIndexCCW = Math.floorMod(pointIndex + 1, hull.size());
		return line.relativeCCW(hull.get(pointIndexCW)) >= 0
				&& line.relativeCCW(hull.get(pointIndexCCW)) >= 0;
	}

}

class PointComparator implements Comparator<Point2D> {

	@Override
	public int compare(Point2D o1, Point2D o2) {
		return Double.compare(o1.getX(), o2.getX());
	}

}