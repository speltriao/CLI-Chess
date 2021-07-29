package board;

public class BoardException extends RuntimeException{ //Exception that is optionally treated (not necessarily)
    private static final long serialVersionID = 2L;
    public BoardException(String message) {
        super(message);
    }
}
