package chess101;

import static chess101.Index.GRID;
import static chess101.Index.NO_INDEX;
import static chess101.Piece.BISHOP;
import static chess101.Piece.EMPTY;
import static chess101.Piece.KING;
import static chess101.Piece.KNIGHT;
import static chess101.Piece.PAWN;
import static chess101.Piece.QUEEN;
import static chess101.Piece.ROOK;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

final public class State extends HashMap<Index, HashSet<Index>> {    
    final private static String SLASH = "/";
    final private static String NONE = "-";

    final private static int ONE = 1;
    
    final private Piece[][] board;
    final private Side color;
    final private LinkedHashSet<Index>[] castling;
    final private Index enPassant;
    final private int rule50;
    final private int moveCounter;
    
    final private boolean check;
    
    private State(Piece[][] board, Side color, LinkedHashSet<Index>[] castling, Index enPassant, int rule50, int moveCounter) {
        check = isCheck(color, board);
        
        for (Index from : Index.values()) {
            if (Piece.isColor(color, board[from.rank][from.file])) {
                Piece piece = board[from.rank][from.file];
                HashSet<Index> moves = new HashSet();
                
                board[from.rank][from.file] = EMPTY;
                
                for (Step step : piece.getStep()) {
                    if (step.canStep(from)) {
                        Index to = step.getStep(from);
                        
                        if (((piece == KING[color.ordinal()] || piece == QUEEN[color.ordinal()] || piece == ROOK[color.ordinal()] || piece == BISHOP[color.ordinal()] || piece == KNIGHT[color.ordinal()]) && !Piece.isColor(color, board[to.rank][to.file])) || (piece == PAWN[color.ordinal()] && ((Math.abs(step.x) == 1 && (Piece.isColor(color.flip(), board[to.rank][to.file]) || to == enPassant)) || (step.x == 0 && board[to.rank][to.file] == EMPTY)))) {
                            Piece tile = board[to.rank][to.file];
    
                            board[to.rank][to.file] = piece;
                            
                            if (piece == PAWN[color.ordinal()] && to == enPassant) {
                                board[from.rank][to.file] = EMPTY;
                            }
                            
                            if (!isCheck(color, board)) {
                                moves.add(to);
                            }
                            
                            board[to.rank][to.file] = tile;
                            
                            if (piece == QUEEN[color.ordinal()] || piece == ROOK[color.ordinal()] || piece == BISHOP[color.ordinal()]) {
                                while (board[to.rank][to.file] == EMPTY && step.canStep(to)) {
                                    to = step.getStep(to);                                    
                                    
                                    if (!Piece.isColor(color, board[to.rank][to.file])) {
                                        tile = board[to.rank][to.file];
                                        board[to.rank][to.file] = piece;
                                        
                                        if (!isCheck(color, board)) {
                                            moves.add(to);
                                        }

                                        board[to.rank][to.file] = tile;
                                    }
                                }
                            } else if (piece == PAWN[color.ordinal()]) {
                                if (step.x == 0 && from.rank == new int[] {1, 6}[color.ordinal()]) {
                                    to = step.getStep(to);
                                    
                                    if (board[to.rank][to.file] == EMPTY) {
                                        board[to.rank][to.file] = piece;
                                        
                                        if (!isCheck(color, board)) {
                                            moves.add(to);
                                        }

                                        board[to.rank][to.file] = EMPTY;
                                    }
                                } else if (to == enPassant) {
                                    board[from.rank][to.file] = PAWN[color.flip().ordinal()];
                                }
                            }
                        }
                    }
                }

                if (piece == KING[color.ordinal()] && !check) {
                    for (Index rook : castling[color.ordinal()]) {
                        test : {
                            Index to = Index.TILE[from.rank][Math.max(from.file - 1, Math.min(rook.file, from.file + 1))];

                            for (Index step = to; step != rook; step = Index.TILE[step.rank][step.file + Integer.signum(rook.file - step.file)]) {
                                if (board[step.rank][step.file] != EMPTY) {
                                    break test;
                                }                                
                            }

                            if (moves.contains(to)) {
                                to = Index.TILE[to.rank][Math.max(to.file - 1, Math.min(rook.file, to.file + 1))];
    
                                board[to.rank][to.file] = piece;
                                        
                                if (!isCheck(color, board)) {
                                    moves.add(to);
                                }

                                board[to.rank][to.file] = EMPTY;
                            }
                        }
                    }
                }

                board[from.rank][from.file] = piece;
                
                if (!moves.isEmpty()) {
                    put(from, moves);
                }
            }
        }
        
        this.board = board;
        this.color = color;
        this.castling = castling;
        this.enPassant = enPassant;
        this.rule50 = rule50;
        this.moveCounter = moveCounter;
    }
    
    private static boolean isCheck(Side color, Piece[][] board) {
        for (Index from : Index.values()) {
            if (board[from.rank][from.file] == KING[color.ordinal()]) {
                for (Step step : Step.values()) {
                    if (step.canStep(from)) {
                        Index to = step.getStep(from);
                        
                        if ((Math.abs(step.x * step.y) <= 1 && board[to.rank][to.file] == KING[color.flip().ordinal()]) || (Math.abs(step.x * step.y) == 2 && board[to.rank][to.file] == KNIGHT[color.flip().ordinal()]) || (Math.abs(step.x) == 1 && step.y == new int[] {1, -1}[color.ordinal()] && board[to.rank][to.file] == PAWN[color.flip().ordinal()])) {
                            return true;
                        } else if (Math.abs(step.x * step.y) <= 1) {
                            while (board[to.rank][to.file] == EMPTY && step.canStep(to)) {
                                to = step.getStep(to);
                            }
                            
                            if (board[to.rank][to.file] == QUEEN[color.flip().ordinal()] || (step.x * step.y == 0 && board[to.rank][to.file] == ROOK[color.flip().ordinal()]) || (Math.abs(step.x * step.y) == 1 && board[to.rank][to.file] == BISHOP[color.flip().ordinal()])) {
                                return true;
                            }
                        }
                    }
                }
                
                break;
            }
        }
           
        return false;
    }
    
    public Piece[][] getBoard() {
        return board;
    }
    
    public Side getColor() {
        return color;
    }
    
    public LinkedHashSet<Index>[] getCastling() {
        return castling;
    }
    
    public Index getEnPassant() {
        return enPassant;
    }
    
    public int getRule50() {
        return rule50;
    }
    
    public int getMoveCounter() {
        return moveCounter;
    }
    
    public boolean isCheck() {
        return check;
    }
    
    public State doMove(String move) {
        Piece[][] board = this.board.clone();
        LinkedHashSet<Index>[] castling = this.castling.clone();
        
        for (int i = 0; i < board.length; i++) {
            board[i] = this.board[i].clone();
        }

        for (int i = 0; i < castling.length; i++) {
            castling[i] = new LinkedHashSet(this.castling[i]);
        }
        
        Index from = Index.valueOf(move.substring(0, 2));
        Index to = Index.valueOf(move.substring(2, 4));
        
        Piece piece = board[from.rank][from.file];
        
        board[from.rank][from.file] = EMPTY;
        board[to.rank][to.file] = move.length() == 5 ? Piece.valueOf("" + move.charAt(4)) : piece;
        
        if (piece == KING[color.ordinal()] && !castling[color.ordinal()].isEmpty()) {
            if (Math.abs(to.file - from.file) == 2) {
                board[to.rank][new int[] {0, 7}[to.file / 6]] = EMPTY;
                board[to.rank][(from.file + to.file) / 2] = ROOK[color.ordinal()];
            }
            
            castling[color.ordinal()].clear();
        } else if (piece == PAWN[color.ordinal()] && to == enPassant) {
            board[to.rank][from.file] = EMPTY;
        }
        
        castling[color.ordinal()].remove(from);
        castling[color.flip().ordinal()].remove(to);
        
        Index enPassant = piece == PAWN[color.ordinal()] && Math.abs(to.rank - from.rank) == 2 ? Index.TILE[(from.rank + to.rank) / 2][to.file] : NO_INDEX;

        int rule50 = piece != PAWN[color.ordinal()] && this.board[to.rank][to.file] == EMPTY ? this.rule50 + 1 : 0;
        int moveCounter = this.moveCounter + 1;

        Side color = this.color.flip();
        
        return new State(board, color, castling, enPassant, rule50, moveCounter);
    }
    
    public static State fromFEN(String fen) {
        StringTokenizer tokenizer = new StringTokenizer(fen);

        Piece[][] board = new Piece[GRID][GRID];
        
        int rank = board.length - 1;
        
        for (String row : tokenizer.nextToken().split(SLASH)) {
            int file = 0;
            
            for (char column : row.toCharArray()) {
                try {
                    file += Integer.parseInt("" + column);
                } catch (Exception ex) {
                    board[rank][file++] = Piece.valueOf("" + column);
                }
            }
            
            rank--;
        }
        
        Side color = Side.valueOf(tokenizer.nextToken());

        LinkedHashSet<Index>[] castling = new LinkedHashSet[Side.values().length];
        
        for (int i = 0; i < castling.length; i++) {
            castling[i] = new LinkedHashSet();
        }
        
        for (char rook : tokenizer.nextToken().replace(NONE, "").toCharArray()) {
            switch (rook) {
                case 'Q' : castling[Side.w.ordinal()].add(Index.a1); break;
                case 'K' : castling[Side.w.ordinal()].add(Index.h1); break;
                case 'q' : castling[Side.b.ordinal()].add(Index.a8); break;
                case 'k' : castling[Side.b.ordinal()].add(Index.h8);
            }
        }
        
        Index enPassant = NO_INDEX;

        try {
            enPassant = Index.valueOf(tokenizer.nextToken().replace(NONE, ""));
        } catch (Exception ex) {}
        
        int rule50 = Integer.parseInt(tokenizer.nextToken());
        int moveCounter = Integer.parseInt(tokenizer.nextToken());
        
        return new State(board, color, castling, enPassant, rule50, moveCounter);
    }
    
    @Override
    public String toString() {
        String[] board = new String[this.board.length];
        String qkqk = "";
        
        int rank = board.length - 1;
        
        for (Piece[] row : this.board) {
            board[rank] = "";
            
            for (Piece column : row) {
                board[rank] += column == EMPTY ? ONE : column;
                
                if (column == EMPTY) {
                    for (int i = ONE; i < GRID; i += ONE) {
                        if (board[rank].endsWith(i + "" + ONE)) {
                            board[rank] = board[rank].replace(i + "" + ONE, "" + (i + ONE));
                            
                            break;
                        }
                    }
                }
            }
            
            rank--;
        }
        
        for (LinkedHashSet<Index> castling : castling) {
            for (Index rook : castling) {
                switch (rook) {
                    case a1 : qkqk += Piece.Q; break;
                    case h1 : qkqk += Piece.K; break;
                    case a8 : qkqk += Piece.q; break;
                    case h8 : qkqk += Piece.k;
                }
            }
        }
        
        return String.join(SLASH, board) + " " + color + " " + (qkqk.isEmpty() ? NONE : qkqk) + " " + (enPassant == NO_INDEX ? NONE : enPassant) + " " + rule50 + " " + moveCounter;
    }

}
