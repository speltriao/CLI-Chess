package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "N";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean matriz[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        for (int i = 2; i > -3 ; i-=4) {
            for (int j = 1; j > -2 ; j-=2) {
                p.setValues(position.getRow() + i , position.getColumn()+j);
                if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) matriz[p.getRow()][p.getColumn()] = true;
            }
        }

        for (int i = 1; i > -2 ; i-=2) {
            for (int j = 2; j > -3 ; j-=4) {
                p.setValues(position.getRow() + i , position.getColumn()+j);
                if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) matriz[p.getRow()][p.getColumn()] = true;
            }
        }

        matriz = opponentOnNextPosition(p, matriz);
        return matriz;
    }
}
