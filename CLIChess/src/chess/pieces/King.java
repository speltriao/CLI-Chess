package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {
    private ChessMatch chessMatch;

    public King(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    private boolean[][] listHorizontalMoves(Position p, boolean[][] matriz){
        int x=1;
        for (int i = 0; i < 2; i++) {
            p.setValues(position.getRow() + x, position.getColumn());
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) matriz[p.getRow()][p.getColumn()] = true;
            matriz = opponentOnNextPosition(p, matriz);
            x*=-1;
        }
        return matriz;
    }

    private boolean[][] listVerticalMoves(Position p, boolean[][] matriz){
        int x=1;
        for (int i = 0; i < 2; i++) {
            p.setValues(position.getRow(), position.getColumn()+x);
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) matriz[p.getRow()][p.getColumn()] = true;
            matriz = opponentOnNextPosition(p, matriz);
            x*=-1;
        }
        return matriz;
    }

    private boolean[][] listDiagonalMoves(Position p, boolean[][] matriz){
        int x = 1, y = 1;
        for (int i = 0; i < 4; i++) {
            if (i%2==0) x*=-1;
            else y*=-1;
            p.setValues(position.getRow() + x , position.getColumn()+y);
            if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) matriz[p.getRow()][p.getColumn()] = true;
            matriz = opponentOnNextPosition(p, matriz);
        }
        return matriz;
    }

    private boolean testRookCastling( Position pos){ //Castling special movement
        ChessPiece p = (ChessPiece) getBoard().pieceOnPosition(pos);
        return (p!=null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount()==0);
    }

    private boolean[][] testKingSmallCastling(boolean[][] matriz){//Special Movement: Kingside rook
        if (getMoveCount()==0 && !chessMatch.getCheck()){
            Position rook1 = new Position(position.getRow(), position.getColumn()+3);
            if (testRookCastling(rook1)) {
                Position aux1 = new Position(position.getRow(), position.getColumn()+1);
                Position aux2 = new Position (position.getRow(), position.getColumn()+2);
                if (getBoard().pieceOnPosition(aux1) == null && getBoard().pieceOnPosition(aux2) == null ){//Check if both positions are free
                     matriz[position.getRow()][position.getColumn()+2]=true;
                }

            }
        }
        return matriz;
    }

    private boolean[][] testKingBigCastling(boolean[][] matriz){//Special Movement: Queenside rook
        if (getMoveCount()==0 && !chessMatch.getCheck()){
            Position rook1 = new Position(position.getRow(), position.getColumn()-4);
            if (testRookCastling(rook1)) {
                Position aux1 = new Position(position.getRow(), position.getColumn()-1);
                Position aux2 = new Position (position.getRow(), position.getColumn()-2);
                Position aux3 = new Position (position.getRow(), position.getColumn()-3);
                if (getBoard().pieceOnPosition(aux1) == null && getBoard().pieceOnPosition(aux2) == null && getBoard().pieceOnPosition(aux3) ==null ){//Check if the three positions are free
                    matriz[position.getRow()][position.getColumn()-2]=true;
                }
            }
        }
        return matriz;
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean matriz[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        matriz = listHorizontalMoves(p,matriz);
        matriz = listVerticalMoves(p,matriz);
        matriz = listDiagonalMoves(p,matriz);
        matriz = testKingSmallCastling(matriz);
        matriz = testKingBigCastling(matriz);
        return matriz;
    }

    @Override
    public String toString(){
        return "K";
    }

}
