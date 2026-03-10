package chess101;

import static chess101.Piece.BISHOP;
import static chess101.Piece.KNIGHT;
import static chess101.Piece.QUEEN;
import static chess101.Piece.ROOK;

final public class Promotion extends Move {
    final public static Piece[][] QRBN = {QUEEN, ROOK, BISHOP, KNIGHT};
    
    final private Piece piece;
    
    public Promotion(Index from, Index to, Piece piece) {
        super(from, to);
        
        this.piece = piece;
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    @Override
    public String toString() {
        return from + "" + to + "" + piece;
    }
}
