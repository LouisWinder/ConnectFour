package connectfour;

import javax.swing.*;

public class Space extends JButton {
    private int xPos, yPos;
    private Piece pieceInSpace;
    
    public Space(int xPos, int yPos, Piece piece) {
    	this.xPos = xPos;
    	this.yPos = yPos;
    	this.pieceInSpace = piece;
    	this.addActionListener(e -> spaceClicked());
    }

    public void setPieceInSpace(Piece piece, Column squareColumn) {
    	pieceInSpace = piece;
    	this.setIcon(piece.PIECE_IMAGE);
    	this.setDisabledIcon(piece.PIECE_IMAGE);
    	// Increase rows filled counter for squareColumn
    	squareColumn.incrementRowsFilled();
    }
    
    public Piece getPieceInSpace() {
    	return pieceInSpace;
    }
    
    /**
     * For the AI when determining its move
     */
    public void removePieceFromSpace(Column squareColumn) {
    	this.setIcon(null);
    	pieceInSpace = null;
    	// Decrease rows filled counter for squareColumn
    	squareColumn.decrementRowsFilled();
    }
    
    public boolean isPieceInSpace() {
    	return getPieceInSpace() != null;
    }
    
    public int getXPosition() {
    	return xPos;
    }
    
    public int getYPosition() {
    	return yPos;
    }
    
    private void spaceClicked() {
    	Game.move(this);
    }
}
