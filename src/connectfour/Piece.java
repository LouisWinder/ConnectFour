package connectfour;

import javax.swing.*;

public class Piece {
	private boolean isRed;
	final ImageIcon PIECE_IMAGE;
	final String PIECE_NAME;
    int value;
	
	public Piece(boolean isRed, int value, ImageIcon pieceImage) {
		this.isRed = isRed;
		this.PIECE_IMAGE = pieceImage;
		PIECE_NAME = isRed ? "Red" : "Yellow";
		this.value = value;
	}
	
	public boolean isRed() {
		return isRed;
	}
}
