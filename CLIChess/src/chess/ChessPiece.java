package chess;

import board.Board;
import board.Piece;
import board.Position;

public abstract class ChessPiece extends Piece { //A chess piece is a generic piece with two colors (white or blacks)

    private Color color;
    int moveCount;

    public ChessPiece(Board board, Color color) {
        super(board);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    protected boolean[][] opponentOnNextPosition(Position position, boolean[][] matriz) {//Check if the next position is an enemy piece
        if (getBoard().positionExists(position) && isThereOpponentPiece(position))  matriz[position.getRow()][position.getColumn()] = true;
        return matriz;
    }


    protected boolean isThereOpponentPiece(Position position){
        ChessPiece p = (ChessPiece) getBoard().pieceOnPosition(position);
        return (p!=null && p.getColor()!=color);
    }

    protected void increaseMoveCount(){moveCount++;}

    protected void decreaseMoveCount(){moveCount--;}

    public int getMoveCount(){return moveCount;}


    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }

}
