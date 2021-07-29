package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {
    public Queen(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString(){
        return "Q";
    }


    private boolean[][] listHorizontalMoves(Position p, boolean[][] matriz) {//Just like the rook
        int x = 1;
        for (int i = 0; i < 2 ; i++) {
            p.setValues(position.getRow() + x, position.getColumn());
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                matriz[p.getRow()][p.getColumn()] = true;
                p.setRow(p.getRow() + x);
            }
            matriz = opponentOnNextPosition(p,matriz);
            x*=-1;
        }
        return matriz;
    }

    private boolean[][] listVerticalMoves(Position p, boolean[][] matriz){//Just like the rook
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

    private boolean[][] listDiagonalMove(Position p, boolean[][] matriz){ //Just like the bishop
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

    @Override
    public boolean[][] possibleMoves() {
        Position p = new Position(0,0);
        boolean matriz[][] = new boolean[getBoard().getRows()][getBoard().getColumns()];
        matriz = listHorizontalMoves(p,matriz);
        matriz = listVerticalMoves(p,matriz);
        matriz = listDiagonalMove(p,matriz);
        return matriz;
    }
}
