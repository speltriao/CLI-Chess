package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece {
    public Rook(Board board, Color color) {
        super(board, color);
    }

    private boolean[][] listHorizontalMoves(Position p, boolean[][] matriz) {//List possible moves at bellow of the rook
        int x = 1;
        for (int i = 0; i < 2 ; i++) {
            p.setValues(position.getRow() + x, position.getColumn());
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                matriz[p.getRow()][p.getColumn()] = true;
                p.setRow(p.getRow() + x);
            }
            x*=-1;
            matriz = opponentOnNextPosition(p,matriz);

        }
        return matriz;
    }

    private boolean[][] listVerticalMoves(Position p, boolean[][] matriz){//List possible moves at right of the rook
        int x = 1;
        for (int i = 0; i < 2 ; i++) {
            p.setValues(position.getRow() , position.getColumn()+x);
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                matriz[p.getRow()][p.getColumn()] = true;
                p.setColumn(p.getColumn() + x);
            }
            matriz = opponentOnNextPosition(p,matriz);
            x*=-1;
        }
        return matriz;
    }

    @Override
    public boolean[][] possibleMoves() { //List all possible moves for the rook
        boolean[][] matriz = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);
        matriz = listHorizontalMoves(p,matriz);
        matriz = listVerticalMoves(p,matriz);
        return matriz;
    }

    @Override
    public String toString(){
        return "R";
    }
}
