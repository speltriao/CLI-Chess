package board;

public abstract class Piece { //Generic piece, not necessarily a chess one.

    protected Position position;
    private Board board;

    public Piece (Board board) { //Every piece belongs to a board
        this.board = board;
        position=null; //Null means that the piece was not yet initialized/ not on the board!
    }

    public abstract boolean[][] possibleMoves(); //List all posible moves for the piece

    public boolean isThereAnyPossibleMove(){
        boolean matrix[][] = possibleMoves();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j]) return true;
            }
        }
        return false;
    }

    public boolean possibleMove (Position position){//Check if it's possible the piece to be moved to a given position//
        return possibleMoves()[position.getRow()][position.getColumn()]; /*On the boolean matrix of possible moves, check
        if at the given position, the value is true*/
    }

    protected Board getBoard(){
        return board;
    }
}
