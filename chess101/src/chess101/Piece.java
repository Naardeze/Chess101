package chess101;

public enum Piece {
    K(new Step[] {Step.x_, Step.xy, Step.xY, Step._y, Step.X_, Step.Xy, Step.XY, Step._Y}),
    Q(new Step[] {Step.x_, Step.xy, Step.xY, Step._y, Step.X_, Step.Xy, Step.XY, Step._Y}),
    R(new Step[] {Step.x_, Step._y, Step.X_, Step._Y}),
    B(new Step[] {Step.xy, Step.xY, Step.Xy, Step.XY}),
    N(new Step[] {Step.xyy, Step.xYY, Step.xxy, Step.xxY, Step.Xyy, Step.XYY, Step.XXy, Step.XXY}),
    P(new Step[] {Step.xY, Step._Y, Step.XY}),
    k(new Step[] {Step.x_, Step.xy, Step.xY, Step._y, Step.X_, Step.Xy, Step.XY, Step._Y}),
    q(new Step[] {Step.x_, Step.xy, Step.xY, Step._y, Step.X_, Step.Xy, Step.XY, Step._Y}),
    r(new Step[] {Step.x_, Step._y, Step.X_, Step._Y}),
    b(new Step[] {Step.xy, Step.xY, Step.Xy, Step.XY}),
    n(new Step[] {Step.xyy, Step.xYY, Step.xxy, Step.xxY, Step.Xyy, Step.XYY, Step.XXy, Step.XXY}),
    p(new Step[] {Step.xy, Step._y, Step.Xy});
 
    final private Step[] step;
 
    Piece(Step[] step) {
        this.step = step;
    }
    
    public Step[] getStep() {
        return step;
    }
    
    final public static Piece EMPTY = null;

    final public static Piece[] KING = {K, k};
    final public static Piece[] QUEEN = {Q, q};
    final public static Piece[] ROOK = {R, r};
    final public static Piece[] BISHOP = {B, b};
    final public static Piece[] KNIGHT = {N, n};
    final public static Piece[] PAWN = {P, p};

    public static boolean isColor(Side color, Piece piece) {
        return piece == KING[color.ordinal()] || piece == QUEEN[color.ordinal()] || piece == ROOK[color.ordinal()] || piece == BISHOP[color.ordinal()] || piece == KNIGHT[color.ordinal()] || piece == PAWN[color.ordinal()]; 
    }

}
