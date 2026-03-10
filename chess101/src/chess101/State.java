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
    
    final public Piece[][] board;
    final public Side color;
    final public LinkedHashSet<Index>[] castling;
    final public Index enPassant;
    final public int rule50;
    final public int moveCounter;
    
    final public boolean check;
    
    private State(Piece[][] board, Side color, LinkedHashSet<Index>[] castling, Index enPassant, int rule50, int moveCounter) {
        check = isCheck(color, board);
        
        for (Index from : Index.values()) {
            if (Piece.isColor(color, board[from.rank][from.file])) {
                Piece piece = board[from.rank][from.file];
                HashSet<Index> moves = new HashSet();
                
                board[from.rank][from.file] = EMPTY;
                
                for (Step step : piece.getStep()) {
                    if (step.hasNext(from)) {
                        Index to = step.getNext(from);
                        
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
                                while (tile == EMPTY && step.hasNext(to)) {
                                    to = step.getNext(to);
                                    
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
                                    to = step.getNext(to);
                                    
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
                            for (Index to = Index.values()[Math.max(from.ordinal() - 1, Math.min(rook.ordinal(), from.ordinal() + 1))]; to != rook; to = Index.values()[to.ordinal() + Integer.signum(rook.ordinal() - to.ordinal())]) {
                                if (board[to.rank][to.file] != EMPTY) {
                                    break test;
                                }
                            }
                            
                            Index to = Index.values()[Math.max(from.ordinal() - 2, Math.min(rook.ordinal(), from.ordinal() + 2))];
                            
                            if (moves.contains(Index.values()[(from.ordinal() + to.ordinal()) / 2])) {
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
        
        this.color = color;
        this.board = board;
        this.castling = castling;
        this.enPassant = enPassant;
        this.rule50 = rule50;
        this.moveCounter = moveCounter;
    }
    
    private static boolean isCheck(Side color, Piece[][] board) {
        for (Index index : Index.values()) {
            if (board[index.rank][index.file] == KING[color.ordinal()]) {
                for (Step step : Step.values()) {
                    if (step.hasNext(index)) {
                        Index to = step.getNext(index);
                        
                        if ((Math.abs(step.x * step.y) <= 1 && board[to.rank][to.file] == KING[color.flip().ordinal()]) || (Math.abs(step.x * step.y) == 2 && board[to.rank][to.file] == KNIGHT[color.flip().ordinal()]) || (Math.abs(step.x) == 1 && step.y == new int[] {1, -1}[color.ordinal()] && board[to.rank][to.file] == PAWN[color.flip().ordinal()])) {
                            return true;
                        } else if (Math.abs(step.x * step.y) <= 1) {
                            while (board[to.rank][to.file] == EMPTY && step.hasNext(to)) {
                                to = step.getNext(to);
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
    
    public State move(Move move) {
        Piece[][] board = this.board.clone();
        LinkedHashSet<Index>[] castling = this.castling.clone();
        
        for (int i = 0; i < board.length; i++) {
            board[i] = this.board[i].clone();
        }

        for (int i = 0; i < castling.length; i++) {
            castling[i] = (LinkedHashSet) this.castling[i].clone();
        }
        
        Index from = move.getFrom();
        Index to = move.getTo();
        
        Piece piece = board[from.rank][from.file];
        
        board[from.rank][from.file] = EMPTY;
        board[to.rank][to.file] = piece;
        
        if (piece == KING[color.ordinal()] && !castling[color.ordinal()].isEmpty()) {
            if (Math.abs(to.file - from.file) == 2) {
                board[to.rank][new int[] {0, 7}[to.file / 6]] = EMPTY;
                board[to.rank][(from.file + to.file) / 2] = ROOK[color.ordinal()];
            }
            
            castling[color.ordinal()].clear();
        } else if (piece == PAWN[color.ordinal()]) {
            if (to == enPassant) {
                board[to.rank][from.file] = EMPTY;
            } else if (move instanceof Promotion) {
                board[to.rank][to.file] = ((Promotion) move).getPiece();
            }
        }
        
        castling[color.ordinal()].remove(from);
        castling[color.flip().ordinal()].remove(to);
        
        Index enPassant = piece == PAWN[color.ordinal()] && Math.abs(to.rank - from.rank) == 2 ? Index.values()[(from.ordinal() + to.ordinal()) / 2] : NO_INDEX;
        
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
                switch (column) {
                    case 'K' : board[rank][file++] = Piece.K; break;
                    case 'Q' : board[rank][file++] = Piece.Q; break;
                    case 'R' : board[rank][file++] = Piece.R; break;
                    case 'B' : board[rank][file++] = Piece.B; break;
                    case 'N' : board[rank][file++] = Piece.N; break;
                    case 'P' : board[rank][file++] = Piece.P; break;
                    case 'k' : board[rank][file++] = Piece.k; break;
                    case 'q' : board[rank][file++] = Piece.q; break;
                    case 'r' : board[rank][file++] = Piece.r; break;
                    case 'b' : board[rank][file++] = Piece.b; break;
                    case 'n' : board[rank][file++] = Piece.n; break;
                    case 'p' : board[rank][file++] = Piece.p; break;
                    default : file += Integer.parseInt("" + column);
                }
            }
            
            rank--;
        }
        
        Side color = Side.values()[(Side.w + "" + Side.b).indexOf(tokenizer.nextToken())];
        
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
        
        switch (tokenizer.nextToken()) {
            case "a3" : enPassant = Index.a3; break;
            case "b3" : enPassant = Index.b3; break;
            case "c3" : enPassant = Index.c3; break;
            case "d3" : enPassant = Index.d3; break;
            case "e3" : enPassant = Index.e3; break;
            case "f3" : enPassant = Index.f3; break;
            case "g3" : enPassant = Index.g3; break;
            case "h3" : enPassant = Index.h3; break;
            case "a6" : enPassant = Index.a6; break;
            case "b6" : enPassant = Index.b6; break;
            case "c6" : enPassant = Index.c6; break;
            case "d6" : enPassant = Index.d6; break;
            case "e6" : enPassant = Index.e6; break;
            case "f6" : enPassant = Index.f6; break;
            case "g6" : enPassant = Index.g6; break;
            case "h6" : enPassant = Index.h6;
        }

        int rule50 = Integer.parseInt(tokenizer.nextToken());
        int moveCounter = Integer.parseInt(tokenizer.nextToken());
        
        return new State(board, color, castling, enPassant, rule50, moveCounter);
    }
    
    @Override
    public String toString() {
        String[] board = new String[this.board.length];
        String qkqk = "";
        
        for (int row = 0; row < board.length; row++) {
            board[row] = "";
            
            for (Piece piece : this.board[board.length - 1 - row]) {
                board[row] = board[row] + (piece == EMPTY ? ONE : piece);
                
                if (piece == EMPTY) {
                    for (int i = ONE; i < GRID; i += ONE) {
                        board[row] = board[row].replace(i + "" + ONE, "" + (i + ONE));
                    }
                }
            }
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
