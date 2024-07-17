package connectfour;

import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

public class Game {
    static Player player1;
    static Player player2;
    static Grid grid;
    static Player currentPlayer;
    static int movesMade = 0;
    
    public Game() {
    	initialisePlayers();
    	initialiseGrid();
    } 
    
    private void initialisePlayers() {
    	Scanner s = new Scanner(System.in);
    	String input = "";
    	do {
    		System.out.println("Type 1 if you would like to play against AI.");
    		System.out.println("Or type 2 if you would like to player against another human locally.");
    		input = s.next();
    		if (input.equals("1")) {
    			System.out.println("Playing against the AI...");
    			choosePieces("AI");
    		}
    		else if (input.equals("2")) {
    			System.out.println("Playing against another player...");
    			choosePieces("Human");
    		}
    	} while (!(input.equals("1")) && !(input.equals("2")));
    	currentPlayer = player1;
    }
    
    private void choosePieces(String playingAgainst) {
    	ImageIcon redIcon = null;
    	ImageIcon yellowIcon = null;
		try {
			redIcon = new ImageIcon(ImageIO.read(new File("Red.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
			yellowIcon = new ImageIcon(ImageIO.read(new File("Yellow.png")).getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
    	Scanner s = new Scanner(System.in);
    	String input = "";
    	do {
    		if (playingAgainst.equals("AI")) {
    			System.out.println("Red or Yellow (R or Y): ");
    			input = s.next().toUpperCase();
    			if (input.equals("R")) {
    				player1 = new Human(new Piece(true, -1, redIcon));
    				player2 = new AI(new Piece(false, 1, yellowIcon));
    			}
    			else if (input.equals("Y")) {
    				player1 = new Human(new Piece(false, -1, yellowIcon));
    				player2 = new AI(new Piece(true, 1, redIcon));
    			}
    		}
    		else {
    			System.out.println("Player 1, Red or Yellow (R or Y): ");
    			input = s.next().toUpperCase();
    			if (input.equals("R")) {
    				player1 = new Human(new Piece(true, -1, redIcon));
    				player2 = new Human(new Piece(false, -1, yellowIcon));
    			}
    			else if (input.equals("Y")) {
    			    player1 = new Human(new Piece(false, -1, yellowIcon));
    			    player2 = new Human(new Piece(true, -1, redIcon));
    			}
    		}
    	} while (!(input.equals("R")) && !(input.equals("Y")));
    }
    
    private void initialiseGrid() {
    	grid = new Grid(5, 4); // Width, Height
    	GUI gui = new GUI(grid);
    }
    
    public static void changeTurn() {  
    	currentPlayer = currentPlayer == player1 ? player2 : player1;  
    } 
     
    public static void move(Space space) {  
    	// Check if the move is valid   
    	if (!isMoveValid(space)) {  
    		return;  
    	}  
    	space.setPieceInSpace(currentPlayer.getPiece(), grid.getColumns()[space.getYPosition()]); 
    	// Disable space (it cannot be used again)  
    	space.setEnabled(false); 
    	// Check for win 
    	if (hasWon(space)) { 
    		showOutcome("Win", currentPlayer); 
    		return; 
    	} 
    	changeTurn(); 
    	movesMade++; 
    	// Draw 
    	if (movesMade == (grid.getGridWidth() * grid.getGridHeight())) { 
    		showOutcome("Draw", currentPlayer); 
    	} 
    	if (currentPlayer instanceof AI) { 
    		Scanner s = new Scanner(System.in);
    		String input = "";
    		boolean AIMove = true; 
    		/*
    		do {
    			System.out.println("Type 1 for choosing move, or 2 for AI move: ");
    	        input = s.next(); 
    			if (input.equals("1")) {
    				AIMove = false;
    			}
    		} while (!input.equals("1") && !input.equals("2"));
    		*/
    		// Perform AI move 
    		if (AIMove) {
    			makeAIMove(); 
    		}
    	} 
    } 
    
    /** 
     * Checks if the intended move is valid (i.e. obeys the gravity that would 
     * occur in a real-life Connect 4 game). 
     */ 
    public static boolean isMoveValid(Space space) { 
    	Column squareColumn = grid.getColumns()[space.getYPosition()]; 
    	if (squareColumn.getRowsFilled() != ((grid.getGridHeight() - 1) - space.getXPosition())) { 
    		return false; 
    	} 
    	return true; 
    } 
    
    public static void makeAIMove() {
    	if (movesMade != (grid.getGridWidth() * grid.getGridHeight())) {
    		Space AIMove = ((AI) currentPlayer).determineMove(grid);
    		move(AIMove);
    	}
    	// Wait for move from player
    }
    
    public static boolean hasWon(Space space) {
    	// Check for 4 in a row, column or diagonal (from last placed piece)
    	return winRow(space) || winColumn(space) || winDiagonal(space);
    }
    
    public static boolean winRow(Space lastPlaced) {
    	// Check if 4 in row of lastPlaced
    	int lastPlacedCounter = 0;
    	for (int i = 0; i < grid.getGridWidth(); i++) {
    		if (grid.getSpaces()[lastPlaced.getXPosition()][i].getPieceInSpace() == lastPlaced.getPieceInSpace()) {
    			lastPlacedCounter++;
    			if (lastPlacedCounter == 4) {
    				return true;
    			}
    		}
    		else {
    			lastPlacedCounter = 0;
    		}
    	}
    	return false;
    }
    
    public static boolean winColumn(Space lastPlaced) {
    	// Check if 4 in a column of lastPlaced
    	int lastPlacedCounter = 0;
    	for (int i = 0; i < grid.getGridHeight(); i++) {
    		if (grid.getSpaces()[i][lastPlaced.getYPosition()].getPieceInSpace() == lastPlaced.getPieceInSpace()) {
    			lastPlacedCounter++;
    			if (lastPlacedCounter == 4) {
    				return true;
    			}
    		}
    		else {
    			lastPlacedCounter = 0;
    		}
    	}
    	return false;
    }
    
    public static boolean winDiagonal(Space lastPlaced) {
    	// Check if 4 in a diagonal of lastPlaced
    	
    	// South-North diagonals
    	for (int i = 3; i < grid.getGridWidth() - 1; i++) {
    		for (int j = 0; j < (grid.getGridHeight() - 3); j++) {
    			if (grid.getSpaces()[i][j].getPieceInSpace() == lastPlaced.getPieceInSpace() && 
    				    grid.getSpaces()[i-1][j+1].getPieceInSpace() == lastPlaced.getPieceInSpace() &&
    				    grid.getSpaces()[i-2][j+2].getPieceInSpace() == lastPlaced.getPieceInSpace() &&
    				    grid.getSpaces()[i-3][j+3].getPieceInSpace() == lastPlaced.getPieceInSpace()) {
    				return true;
    			}
    		}
    	}
    	
    	// North-South diagonals
    	for (int i = 3; i < grid.getGridWidth() - 1; i++) {
    		for (int j = 3; j < grid.getGridHeight(); j++) {
    			if (grid.getSpaces()[i][j].getPieceInSpace() == lastPlaced.getPieceInSpace() && 
    				    grid.getSpaces()[i-1][j-1].getPieceInSpace() == lastPlaced.getPieceInSpace() &&
    				    grid.getSpaces()[i-2][j-2].getPieceInSpace() == lastPlaced.getPieceInSpace() &&
    				    grid.getSpaces()[i-3][j-3].getPieceInSpace() == lastPlaced.getPieceInSpace()) {
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }
    
    public static void showOutcome(String outcome, Player winner) {
    	if (outcome.equals("Draw")) {
    		System.out.println("It's a draw!");
    	}
    	else {
    		System.out.println("Winner is " + winner.getPiece().PIECE_NAME + "!");
    	}
    	grid.disableGrid();
    }
    
}
