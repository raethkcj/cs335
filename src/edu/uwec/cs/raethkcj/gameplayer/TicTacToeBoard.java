package edu.uwec.cs.raethkcj.gameplayer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class TicTacToeBoard implements TwoPlayerGameBoard {
	
	protected enum Space {
		OPEN,
		HUMAN,
		COMPUTER
	}

	protected Space[] board;
	protected int nextOpenPosition;
	protected boolean isHumanMove;
	
	public TicTacToeBoard() {
		board = new Space[9];
		Arrays.fill(board, Space.OPEN);
		nextOpenPosition = 0;
		isHumanMove = false;
	}
	
	public TicTacToeBoard(TicTacToeBoard o) {
		board = Arrays.copyOf(o.board, o.board.length);
		nextOpenPosition = o.nextOpenPosition;
		isHumanMove = o.isHumanMove;
	}

	@Override
	public boolean hasMoreChildren() {
		return nextOpenPosition >= 0;
	}

	@Override
	public TicTacToeBoard nextChild() {
		TicTacToeBoard child = new TicTacToeBoard(this);
		child.board[this.nextOpenPosition] = isHumanMove ? Space.HUMAN : Space.COMPUTER;
		child.nextOpenPosition = Arrays.asList(child.board).indexOf(Space.OPEN);
		child.isHumanMove = !this.isHumanMove;
//		this.nextOpenPosition = Arrays.asList(this.board).indexOf(Space.OPEN);
		this.nextOpenPosition = nextIndex();
		return child;
	}
	
	private int nextIndex() {
		int subIndex = Arrays.asList(board).subList(nextOpenPosition + 1, board.length).indexOf(Space.OPEN);
		return subIndex == -1 ? -1 : nextOpenPosition + subIndex + 1;
	}
	
	private boolean win(Space s) {
		return 
				//Column wins
				board[0] == s && board[3] == s && board[6] == s ||
				board[1] == s && board[4] == s && board[7] == s ||
				board[2] == s && board[5] == s && board[8] == s ||
				//Row wins
				board[0] == s && board[1] == s && board[2] == s ||
				board[3] == s && board[4] == s && board[5] == s ||
				board[6] == s && board[7] == s && board[8] == s ||
				//Diagonal wins
				board[0] == s && board[4] == s && board[8] == s ||
				board[2] == s && board[4] == s && board[6] == s;
	}

	@Override
	public double staticEvaluation() {
		if(win(Space.COMPUTER)) {
			return 1;
		} else if(win(Space.HUMAN)) {
			return -1;
		} else return 0;
	}

	@Override
	public void draw(Graphics g) {
		((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawBoard(g);
		drawMoves(g);
		drawWin(g);
	}
	
	private void drawBoard(Graphics g) {
		((Graphics2D)g).setStroke(new BasicStroke(10));
		g.setColor(Color.WHITE);
		g.drawLine(233, 100, 233, 500);
		g.drawLine(367, 100, 367, 500);
		g.drawLine(100, 233, 500, 233);
		g.drawLine(100, 367, 500, 367);
	}
	
	private void drawMoves(Graphics g) {
		((Graphics2D)g).setStroke(new BasicStroke(8));
		for(int i = 0; i < board.length; i++) {
            int x = 133/2 + 133*(i%3) + 100;
            int y = 133/2 + 133*(int)(i/9.0 * 3) + 100;
			Space s = board[i];
			if(s == Space.COMPUTER) {
				drawO(x,y,g);
			} else if(s == Space.HUMAN) {
				drawX(x,y,g);
			}
		}
	}
	
	private void drawWin(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getName(), g.getFont().getStyle(), 22));
		String s = "";
		boolean done = false;
		if(done = isComputerWinner()) {
			s = "Computer wins!";
		} else if(done = isUserWinner()) {
			s = "User wins!";
		} else if(done = isDraw()) {
			s = "It's a draw.";
		}
		if(done) g.drawString(s, 233, 50);
	}
	
	private void drawX(int x, int y, Graphics g) {
		g.setColor(Color.GREEN);
		g.drawLine(x-50, y-50, x+50, y+50);
		g.drawLine(x-50, y+50, x+50, y-50);
	}
	
	private void drawO(int x, int y, Graphics g) {
		g.setColor(Color.RED);
		g.drawOval(x - 50, y - 50, 100, 100);
	}

	@Override
	public boolean isComputerWinner() {
		return staticEvaluation() == 1;
	}

	@Override
	public boolean isDraw() {
		return !Arrays.asList(board).contains(Space.OPEN) && staticEvaluation() == 0;
	}

	@Override
	public boolean isUserWinner() {
		return staticEvaluation() == -1;
	}

	@Override
	public void placeUserMove(Point2D mouseLocation) throws Exception {
		if(!(isComputerWinner() || isUserWinner() || isDraw())) {
            int index = 0;
            double x = mouseLocation.getX();
            double y = mouseLocation.getY();
            if(x >= 100 && x < 233) {
                    index += 0;
            } else if (x >= 233 && x < 367) {
                    index += 1;
            } else if (x >= 367 && x <= 500) {
                    index += 2;
            } else {
                    throw new Exception("X out of bounds.");
            }
            
            if(y >= 100 && y < 233) {
                    index += 0;
            } else if (y >= 233 && y < 367) {
                    index += 3;
            } else if (y >= 367 && y <= 500) {
                    index += 6;
            } else {
                    throw new Exception("Y out of bounds.");
            }
		
			board[index] = Space.HUMAN;
			nextOpenPosition = Arrays.asList(board).indexOf(Space.OPEN);
		}
		isHumanMove = false;
	}
	
	@Override
	public String toString() {
		String s = "";
		s += "[";
		for(int i = 0; i < board.length; i++) {
			s += board[i].toString() + " ";
			if(i == 2 || i == 5) s += "\n";
		}
		s += "]";
		return s;
	}

}
