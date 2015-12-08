package edu.uwec.cs.raethkcj.graphcoloring;

import java.util.LinkedList;
import java.util.List;

public class BacktrackerIterative {
	
	private int numberExpanded = 0;
	
	public State backtrack(State s) {
		
		State soln = null;
		
		int bestSolnCost = Integer.MAX_VALUE;
		
		List<State> statesToProcess = new LinkedList<State>();
		statesToProcess.add(s);  // push
		
		while (!statesToProcess.isEmpty()) {
			
			// Pop a feasible state from the list
			State currentState = statesToProcess.remove(0);
			numberExpanded++;
			
			if (currentState.isSolved()) {
				
//				System.out.println("Solved: " + currentState);
				
				if (currentState.getBound() < bestSolnCost) {
					bestSolnCost = currentState.getBound();
					soln = currentState;
				}
				
			} else {
							
				while((currentState.hasMoreChildren()) &&
				      (currentState.getBound() < bestSolnCost)) { 
						
					State child = currentState.nextChild();
				
					if (child.isFeasible()) {
						statesToProcess.add(0, child);
					}	
				}
			}
		}
		System.out.println("Nodes expanded: " + numberExpanded);
		return soln;
	}
}
