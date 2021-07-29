package chess;

import board.BoardException;

public class ChessException extends BoardException { //Exception that is optionally treated (not necessarily)
    private static final long serialVersionID = 1L;

    public ChessException(String msg){
        super(msg);
    }
}
