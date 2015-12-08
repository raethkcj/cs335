package edu.uwec.cs.raethkcj.gameplayer;

import edu.uwec.cs.raethkcj.gameplayer.TicTacToeBoard.Space;

public class Main {
	public static void main(String[] args) {
		TicTacToeBoard board = new TicTacToeBoard();
		board.board = new Space[]{ 
				Space.HUMAN, Space.COMPUTER, Space.COMPUTER,
				Space.OPEN, Space.HUMAN, Space.OPEN,
				Space.COMPUTER, Space.OPEN, Space.HUMAN 
        };
//		board.nextOpenPosition = 1;
//		MiniMax m = new MiniMax();
//		System.out.println(m.generateNextMove(board));
		System.out.println(board.staticEvaluation());
	}
}
