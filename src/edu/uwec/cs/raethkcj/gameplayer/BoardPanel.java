package edu.uwec.cs.raethkcj.gameplayer;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	TwoPlayerGameBoard gameBoard;

	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setName(String string) {
		// TODO Auto-generated method stub
		
	}

	public void setBackground(Color color) {
		// TODO Auto-generated method stub
		
	}
	
	public void setBoard(TwoPlayerGameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	@Override
	public void paintComponent(Graphics g) {
		if(gameBoard != null) gameBoard.draw(g);
	}
}
