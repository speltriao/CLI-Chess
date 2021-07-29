package board;

public class Board { //Generic Board! Not necessarily a chess board
    private int rows, columns;
    private Piece[][] pieces; //Pieces pieceOnPositionMatrix

    public Board(int rows, int columns){
        if (rows < 1 || columns < 1) throw new BoardException("A board needs at least 1 row and 1 column!");
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece [rows][columns];
    }

    public Piece pieceOnPosition(int row, int column){ //Return the piece found on a given X and Y
        if(!positionExists(row,column)) throw new BoardException("Position does not exist!");
        return pieces[row][column];
    }

    public Piece pieceOnPosition(Position position){ //Return the piece found on a given position
        if(!positionExists(position)) throw new BoardException("Position does not exist!");
        return pieces[position.getRow()][position.getColumn()];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)) throw new BoardException("There is already a piece at " + position);
        pieces[position.getRow()][position.getColumn()] = piece; //Setting the given piece at the given position
        piece.position = position; //This piece position is not Null anymore, since it has been initialized (See Piece class constructor)
    }

    public Piece removePiece(Position position){
        if(!positionExists(position)) throw new BoardException("Poisition does not exist!");
        if (pieceOnPosition(position)==null) return null; //There are no pieces at the given position

        Piece aux = pieceOnPosition(position); //Get the piece at the given position and save it to 'aux'
        aux.position = null; //Null here means that the piece is not on the board anymore!
        pieces[position.getRow()][position.getColumn()] = null;  //Removing piece from matrix

        return aux;
    }

    private boolean positionExists(int row, int column){
        return ((row>=0 && row<rows)&&(column>=0 && column<columns));
    }

    public boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());
    }

    public boolean thereIsAPiece(Position position){
        if(!positionExists(position)) throw new BoardException("Position does not exist!");
        return (pieceOnPosition(position) !=null);
    }
}
