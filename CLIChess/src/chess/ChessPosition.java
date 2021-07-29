package chess;

import board.Position;

public class ChessPosition { //Position using the Chess notation: a->h (row) : 1->8 (column)
    private int row;
    private char column;

    public ChessPosition(int row, char column){
        if(row>8 || row<1 || column<'a' || column>'h') throw new ChessException("Invalid chess position!");
        this.row = row;
        this.column=column;
    }

    public int getRow() {
        return row;
    }

    public char getcolumn() {
        return column;
    }

    public Position toPosition(){ //Convert ChessPosition to a generic Position (X,Y)
        int ascii = (int)  column - 97; // A->0, B->1, C->2 .....
        return new Position(8-row, ascii);
    }

    public static ChessPosition fromPosition(Position position){ //Convert a generic Position to a ChessPosition
        int number = 97 + position.getColumn(); //ASCII: A=97, B=98, C=99 ......
        char c = (char) number; // 97->A, 98->B, 99->C .....
        return new ChessPosition(8-position.getRow(), c);
    }

    @Override
    public String toString(){
        return (""  + row  + column);
    }
}
