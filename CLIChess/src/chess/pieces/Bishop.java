package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "B"; //B from Bishop
    }



    @Override
    public boolean[][] possibleMoves() {
        boolean[][] matriz = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        for (int i = 1; i > -2 ; i-=2) {
            for (int j = -1; j < 2 ; j+=2) {
                p.setValues(position.getRow()+i , position.getColumn()+j);
                while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                    matriz[p.getRow()][p.getColumn()] = true;
                    p.setValues(p.getRow()+i, p.getColumn()+j);
                }
                matriz = opponentOnNextPosition(p,matriz);
            }
        }
        return matriz;
    }

}
