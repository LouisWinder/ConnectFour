package connectfour;

public abstract class Player {
    private final Piece PIECE;
    
    public Player(Piece piece) {
    	this.PIECE = piece;
    }
    
    public Piece getPiece() {
    	return PIECE;
    }
}
