package edu.uwec.cs.raethkcj.gameplayer;

import java.util.Stack;

public class MiniMax {
	private int maxLevel;

	public MiniMax(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public TwoPlayerGameBoard generateNextMove(TwoPlayerGameBoard currentProblem) {
		double max = Double.NEGATIVE_INFINITY;
		TwoPlayerGameBoard bestMove = null;
		TwoPlayerGameBoard currentChild = null;
		while(currentProblem.hasMoreChildren()) {
			currentChild = currentProblem.nextChild();
			
			double childEval = recursiveMiniMaxAlphaBeta(currentChild, 0, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
			if(childEval > max) {
				bestMove = currentChild;
				max = childEval;
			}
		}
		return bestMove;
	}
	
	private double recursiveMiniMaxAlphaBeta(TwoPlayerGameBoard currentProblem, int currentLevel,
			double alpha, double beta) {
		if(!currentProblem.hasMoreChildren() || currentLevel == maxLevel) {
			return currentProblem.staticEvaluation();
		}
		double result = 0;
		if(currentLevel % 2 == 1) {
			while(currentProblem.hasMoreChildren() && alpha < beta) {
				result = recursiveMiniMaxAlphaBeta(currentProblem.nextChild(), currentLevel + 1, alpha, beta);
				if(result < beta) beta = result;
			}
		} else {
			while(currentProblem.hasMoreChildren() && alpha < beta) {
				result = recursiveMiniMaxAlphaBeta(currentProblem.nextChild(), currentLevel + 1, alpha, beta);
				if(result > alpha) alpha = result;
			}
		}
		return result;
	}
}
