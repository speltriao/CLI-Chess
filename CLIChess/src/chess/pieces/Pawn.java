package chess.pieces;

import board.Board;
import board.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {
    private ChessMatch chessMatch;
    public Pawn(Board board, Color color, ChessMatch chessMatch) {
        super(board, color);
        this.chessMatch = chessMatch;
    }

    private boolean[][] possibleEnPassant(boolean[][] matriz){ //Check if this pawn can make an En Passant
        int row = 3, aux =-1;
        if(getColor()==Color.BLACK){
            row = 4;
            aux = 1;
        }
        if(position.getRow()==row ){

            Position left = new Position(position.getRow(), position.getColumn()-1);
            if (getBoard().positionExists(left) && isThereOpponentPiece(left) && getBoard().pieceOnPosition(left) == chessMatch.getEnPassantVulnerable()){
                matriz[left.getRow() + aux][left.getColumn()] = true;
            }

            Position right = new Position(position.getRow(), position.getColumn()+1);
            if (getBoard().positionExists(right) && isThereOpponentPiece(right) && getBoard().pieceOnPosition(right) == chessMatch.getEnPassantVulnerable()){
                matriz[right.getRow() + aux ][right.getColumn()] = true;
            }
        }
        return matriz;
    }

    @Override
    public String toString() { return "P"; }

    @Override
    public boolean[][] possibleMoves() {
        boolean canMoveFront=false;
        boolean[][] matriz = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        int aux = 1; //Black Pawn
        if (getColor()==Color.WHITE) aux = -1; //White pawn

        p.setValues(position.getRow()+aux, position.getColumn()); //Check horizontal moves
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)){
            matriz[p.getRow()][p.getColumn()] = true;
            canMoveFront=true;
        }

        for (int i = -1; i < 2; i+=2) { //Check diagonal moves
            p.setValues(position.getRow()+aux, position.getColumn()+i);
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)){
                matriz[p.getRow()][p.getColumn()] = true;
            }
        }

        p.setValues(position.getRow() + (2*aux), position.getColumn()); //Check move two positions (special move) only on first move
        if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getMoveCount()==0 && canMoveFront){//To move 2 positions, both must be empty!
            matriz[p.getRow()][p.getColumn()] = true; //Test for special move on the first move of the pawn (where it moves 2 positions)//
        }

        matriz = possibleEnPassant(matriz);
        return matriz;
    }

   
}

