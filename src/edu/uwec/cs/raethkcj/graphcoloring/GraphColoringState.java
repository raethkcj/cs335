package edu.uwec.cs.raethkcj.graphcoloring;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GraphColoringState implements State, Comparable {
	private boolean[][] graph;
	private ArrayList<Color> colors;
	private int[] chosenColors;
	private int nextNode;
	private int nextChildColor;

	public GraphColoringState(boolean[][] graph, ArrayList<Color> colors) {
		this.graph = graph;
		this.colors = colors;
		this.chosenColors = new int[graph.length];
		Arrays.fill(chosenColors, -1);
		this.nextNode = 0;
		this.nextChildColor = 0;
	}

	public GraphColoringState(GraphColoringState o) {
		this.graph = new boolean[o.graph.length][o.graph.length];
		for(int x = 0; x < graph.length; x++) {
			for(int y = 0; y < graph.length; y++) {
				graph[x][y] = o.graph[x][y];
			}
		}
		this.colors = o.colors;
		this.chosenColors = new int[graph.length];
		for(int i = 0; i < chosenColors.length; i++) {
			chosenColors[i] = o.chosenColors[i];
		}
		this.nextNode = o.nextNode;
		this.nextChildColor = o.nextChildColor;
	}

	@Override
	public boolean hasMoreChildren() {
		return this.nextChildColor < colors.size();
	}

	@Override
	public State nextChild() {
		GraphColoringState child = new GraphColoringState(this);
		child.nextNode++;
		child.chosenColors[this.nextNode] = this.nextChildColor;
		child.nextChildColor = 0;

		this.nextChildColor++;

		return child;
	}

	@Override
	public boolean isFeasible() {
		boolean feasible = true;
		
		for(int i = 0; i < graph.length; i++) {
			if(graph[nextNode-1][i] == true && chosenColors[nextNode-1] == chosenColors[i]) {
				feasible = false;
			}
		}
		
		return feasible;
	}

	@Override
	public boolean isSolved() {
		return nextNode == graph.length;
	}

	@Override
	public int getBound() {
		int bound = 0;
		for(int n : chosenColors) {
			bound += n>=0 ? colors.get(n).weight : 0;
		}
		bound += (graph.length - this.nextNode) * minColorWeight();
		return bound;
	}
	
	private int minColorWeight() {
		return Collections.min(colors, (x,y)->Math.min(x.weight, y.weight)).weight;
	}
	
	public String toString() {
		String result = "<";
		
		result += " Node:" + (nextNode - 1);
		result += "  ChosenColors:[";
		for(int n : chosenColors) {
			result += colors.get(n).color + " ";
		}
		result += "] Bound: " + getBound();
		
		result += " >";
		
		return result;
	}

	@Override
	public int compareTo(Object o) {
		return Integer.compare(this.getBound(), ((GraphColoringState)o).getBound());
	}
	
}
