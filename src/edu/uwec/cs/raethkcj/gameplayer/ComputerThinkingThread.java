package edu.uwec.cs.raethkcj.gameplayer;

public class ComputerThinkingThread implements Runnable {
	private GamePlayer gamePlayer;
	private TwoPlayerGameBoard gameBoard;
	private int maxLevel;
	
	public ComputerThinkingThread(GamePlayer gamePlayer, TwoPlayerGameBoard gameBoard, int maxLevel) {
		this.gamePlayer = gamePlayer;
		this.gameBoard = gameBoard;
		this.maxLevel = maxLevel;
	}

	@Override
	public void run() {
		MiniMax m = new MiniMax(maxLevel);
		gamePlayer.computerDone(m.generateNextMove(gameBoard));
	}

}
