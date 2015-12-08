package edu.uwec.cs.raethkcj.graphcoloring;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		BacktrackerIterative bt = new BacktrackerIterative();
		// Define the graph from the ppt
		boolean[][] graph = { 
				{ false, true, true },
				{ true, false, true },
				{ true, true, false },
				
		};
		// Define the colors used in the ppt
		ArrayList<Color> colors = new ArrayList<Color>();
		colors.add(new Color("Red", 2));
		colors.add(new Color("Green", 3));
		colors.add(new Color("Blue", 5));
		colors.add(new Color("Yellow", 2));
		State s = new GraphColoringState(graph, colors);
		State result = bt.backtrack(s);
		System.out.println(result);
	}
}