package connectfour;

import java.util.*;

public class AI extends Player {
	private HashMap<String, Integer> scores;
	// Cache of previously seen grid configurations - saves analysing them more than once
	private HashMap<String, Integer> transpositions = new HashMap<>(); 
	
    public AI(Piece piece) {
    	super(piece);
    	scores = new HashMap<>();
    }
    
    public Space determineMove(Grid grid) {
    	setScores(); // Determines the correct heuristic for players
    	int bestScore = Integer.MIN_VALUE;
    	int[] bestMove = new int[2];
    	int depth = Game.movesMade;
    	// Loop through all available spaces
    	for (Space space : grid.availableSpaces()) {
    		int evaluation;
    		space.setPieceInSpace(Game.player2.getPiece(), grid.getColumns()[space.getYPosition()]); // AI player's piece
    		if (depth < 0) { 
    			evaluation = badSearch();
    		}
    		// Check if position has already been encountered
    		else if (transpositions.containsKey(grid.gridState())) {
    			evaluation = transpositions.get(grid.gridState());
    		}
    		else {
    			evaluation = alphaBeta(grid, space, depth, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
        		//evaluation = mtdf(grid, space, false, depth, 4);
    			transpositions.put(grid.gridState(), evaluation);
    		}
    		space.removePieceFromSpace(grid.getColumns()[space.getYPosition()]);
    		if (evaluation > bestScore) {
    			bestScore = evaluation;
    			bestMove[0] = space.getXPosition();
    			bestMove[1] = space.getYPosition();
    		}
    	}
    	return grid.getSpaces()[bestMove[0]][bestMove[1]];
    }
    
    public int alphaBeta(Grid grid, Space root, int depth, boolean isMax, int alpha, int beta) {
    	String result = "";
    	if (Game.hasWon(root)) {
    		result = root.getPieceInSpace().PIECE_NAME;
    	}
    	else if (depth == (grid.getGridWidth() * grid.getGridHeight()) - 1 && result.equals("")) {
    		result = "Tie";
    	}
    	// Base case - test for terminal node
    	if (!result.equals("")) {
    		return scores.get(result);
    	}
    	if (isMax) {
    		int bestScore = Integer.MIN_VALUE;
    		for (Space space : grid.availableSpaces()) {
    			space.setPieceInSpace(Game.player2.getPiece(), grid.getColumns()[space.getYPosition()]);
    			if (transpositions.containsKey(grid.gridState())) {
    				bestScore = transpositions.get(grid.gridState());
    			}
    			else {
    			    bestScore = Math.max(bestScore, alphaBeta(grid, space, depth + 1, false, alpha, beta));
    			    transpositions.put(grid.gridState(), bestScore);
    			}
    			space.removePieceFromSpace(grid.getColumns()[space.getYPosition()]);
    			alpha = Math.max(alpha, bestScore); // FAIL-SOFT
    			if (bestScore >= beta) {
    				break;
    			}
    			//alpha = Math.max(alpha, bestScore); // FAIL-HARD
    		}
    		return bestScore;
    	}
    	else {
    		int bestScore = Integer.MAX_VALUE;
    		for (Space space : grid.availableSpaces()) {
    			space.setPieceInSpace(Game.player1.getPiece(), grid.getColumns()[space.getYPosition()]);
    			if (transpositions.containsKey(grid.gridState())) {
    				bestScore = transpositions.get(grid.gridState());
    			}
    			else {
    			    bestScore = Math.min(bestScore, alphaBeta(grid, space, depth + 1, true, alpha, beta));
    			    transpositions.put(grid.gridState(), bestScore);
    			}
    			space.removePieceFromSpace(grid.getColumns()[space.getYPosition()]);
    			beta = Math.min(beta, bestScore); // FAIL-SOFT
    			if (bestScore <= alpha) {
    				break;
    			}
    			//beta = Math.min(beta, bestScore); // FAIL-HARD
    		}
    		return bestScore;
    	}
    }
    
    public int mtdf(Grid grid, Space root, boolean isMax, int depth, int firstGuess) {
    	int g = firstGuess;
    	int upperBound = Integer.MAX_VALUE - 1;
    	int lowerBound = Integer.MIN_VALUE + 1;
    	while (lowerBound < upperBound) {
    		//int beta = Math.max(g, lowerBound + 1);
    		int beta = g == lowerBound ? g + 1 : g;
    		g = alphaBeta(grid, root, depth, isMax, -beta - 1, beta);
    		isMax = isMax == false ? true : false; // Change player
    		if (g < beta) {
    			upperBound = g;
    		}
    		else {
    			lowerBound = g;
    		}
    	}
    	return g;
    }
    
    public int badSearch() {
    	return 1;
    }
    
    public void setScores() {
    	scores.put("Tie", 0);
    	if (this.getPiece().PIECE_NAME.equals("Red")) {
    		scores.put("Red", 1);
    		scores.put("Yellow", -1);
    	}
    	else {
    		scores.put("Yellow", 1);
    	    scores.put("Red", -1);
    	}
    }
}
