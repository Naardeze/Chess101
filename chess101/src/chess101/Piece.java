package chess101;

public enum Piece {
    K(new Step[] {Step.xy, Step.x_, Step.xY, Step._y, Step._Y, Step.Xy, Step.X_, Step.XY}),
    Q(new Step[] {Step.xy, Step.x_, Step.xY, Step._y, Step._Y, Step.Xy, Step.X_, Step.XY}),
    R(new Step[] {Step.x_, Step._y, Step._Y, Step.X_}),
    B(new Step[] {Step.xy, Step.xY, Step.Xy, Step.XY}),
    N(new Step[] {Step.xxy, Step.xxY, Step.xyy, Step.xYY, Step.Xyy, Step.XYY, Step.XXy, Step.XXY}),
    P(new Step[] {Step.xY, Step._Y, Step.XY}),
    k(new Step[] {Step.xy, Step.x_, Step.xY, Step._y, Step._Y, Step.Xy, Step.X_, Step.XY}),
    q(new Step[] {Step.xy, Step.x_, Step.xY, Step._y, Step._Y, Step.Xy, Step.X_, Step.XY}),
    r(new Step[] {Step.x_, Step._y, Step._Y, Step.X_}),
    b(new Step[] {Step.xy, Step.xY, Step.Xy, Step.XY}),
    n(new Step[] {Step.xxy, Step.xxY, Step.xyy, Step.xYY, Step.Xyy, Step.XYY, Step.XXy, Step.XXY}),
    p(new Step[] {Step.xy, Step._y, Step.Xy});
 
    final private Step[] step;
 
    Piece(Step[] step) {
        this.step = step;
    }
    
    public Step[] getStep() {
        return step;
    }
    
    final public static Piece[] KING = {K, k};
    final public static Piece[] QUEEN = {Q, q};
    final public static Piece[] ROOK = {R, r};
    final public static Piece[] BISHOP = {B, b};
    final public static Piece[] KNIGHT = {N, n};
    final public static Piece[] PAWN = {P, p};

    public static boolean isColor(Side color, Piece piece) {
        return piece == KING[color.ordinal()] || piece == QUEEN[color.ordinal()] || piece == ROOK[color.ordinal()] || piece == BISHOP[color.ordinal()] || piece == KNIGHT[color.ordinal()] || piece == PAWN[color.ordinal()]; 
    }

    final public static Piece EMPTY = null;
}
