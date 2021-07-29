package chess;

import board.Board;
import board.Piece;
import board.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChessMatch { //Ches Match rules, initial setup, how chess pieces are displayed on chess board etc
    private int turn;
    private Color currentplayer;
    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();
    private Board board;
    private boolean check, checkMate;
    private ChessPiece enPassantVulnerable, promoted;

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessMatch(){
        board = new Board(8,8); //This class that defines that this is a chess board! (8x8)
        turn = 1;
        currentplayer = Color.WHITE;
        initialSetup(); //Put the pieces on the default position
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
    }

    public ChessPiece[][] getPieces(){
        ChessPiece[][] matriz = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows() ; i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                matriz[i][j]= (ChessPiece) board.pieceOnPosition(i,j); //Each found piece is added to our ChessPiece matrix

            }
        }
        return matriz;
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentplayer;
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){return checkMate;}

    private void placeNewPiece(char column, int row, ChessPiece piece){ //Place piece on a chess position (a-h) (1-8)//
        board.placePiece(piece, new ChessPosition(row,column).toPosition()); //Recieves coordinates from ChessPosition
        piecesOnTheBoard.add(piece); //Add the piece to our list of pieces
    }

    /*****SPECIAL MOVES*******/
    private void checkPawnEnPassant(ChessPiece movedPiece, Position source, Position target){//Check if this pawn is vunerable to taking an en passant next round!
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        }
        else enPassantVulnerable = null;
    }

    private void promotion(ChessPiece p, Position t){ //PROMOTION Special move
        promoted =null;
        if(p instanceof Pawn){
            if ((p.getColor() == Color.WHITE && t.getRow() == 0) || (p.getColor() == Color.BLACK && t.getRow() == 7)) { //Check if pawn reached the end of the board ->
                promoted = (ChessPiece) board.pieceOnPosition(t);
                promoted= replacePromotedPiece("Q");
            }
        }
    }

    public ChessPiece replacePromotedPiece(String type) { //Replace the piece for the promoted one.
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.removePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String s, Color c){ //Create a new piece for Promotion, set by the user
        switch (s){
            case "B": return new Bishop(board,c);
            case "Q": return new Queen(board,c);
            case "N": return new Knight(board,c);
            default: return new Rook(board,c); //If it's not a bishop, queen or knight, it's a rook.
        }
    }

    private void smallCastling(ChessPiece p, Position s, Position t){ //Kingside Rook : special movement
        if (p instanceof King && s.getColumn() == s.getColumn() + 2){ //Small Rook (the king moved 2 spaces at once to the right)
            Position sourceRook = new Position(s.getRow(), s.getColumn()+3);
            Position targetRook = new Position (s.getRow(), s.getColumn()+1);
            //We have to move the rook, because of the special movement Small Castling

            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook); //Remove the rook
            board.placePiece(rook,targetRook); //Put the rook at it's new position
            rook.increaseMoveCount();
        }
    }

    private void bigCastling(ChessPiece p, Position s, Position t){ //Queen Rook : special movement
        if (p instanceof King && s.getColumn() == s.getColumn() - 2){ //Small Rook (the king moved 2 spaces at once to the left)
            Position sourceRook = new Position(s.getRow(), s.getColumn()-4);
            Position targetRook = new Position (s.getRow(), s.getColumn()-1);
            //We have to move the rook, because of the special movement Small Castling

            ChessPiece rook = (ChessPiece) board.removePiece(sourceRook); //Remove the rook
            board.placePiece(rook,targetRook); //Put the rook at it's new position
            rook.increaseMoveCount();
        }
    }

    private Piece enPassant(ChessPiece p, Piece capturedPiece, Position s, Position t){ //Queen Rook : special movement
        if (p instanceof Pawn) {
            if (s.getColumn() != t.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(t.getRow() + 1, t.getColumn());
                }
                else {
                    pawnPosition = new Position(t.getRow() - 1, t.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
                capturedPieces.add(capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }
        return capturedPiece;
    }

    private void undoSmallCastling(ChessPiece p, Position s, Position t){ //Kingside Rook : special movement
        if (p instanceof King && s.getColumn() == s.getColumn() + 2){ //Small Rook (the king moved 2 spaces at once to the right)
            Position sourceRook = new Position(s.getRow(), s.getColumn()+3);
            Position targetRook = new Position (s.getRow(), s.getColumn()+1);
            //We have to move the rook, because of the special movement Small Castling

            ChessPiece rook = (ChessPiece) board.removePiece(targetRook); //Remove the rook
            board.placePiece(rook,sourceRook); //Put the rook at it's new position
            rook.decreaseMoveCount();
        }
    }

    private void undoBigCastling(ChessPiece p, Position s, Position t){ //Queen Rook : special movement
        if (p instanceof King && s.getColumn() == s.getColumn() - 2){ //Small Rook (the king moved 2 spaces at once to the left)
            Position sourceRook = new Position(s.getRow(), s.getColumn()-4);
            Position targetRook = new Position (s.getRow(), s.getColumn()-1);
            //We have to move the rook, because of the special movement Small Castling

            ChessPiece rook = (ChessPiece) board.removePiece(targetRook); //Remove the rook
            board.placePiece(rook,sourceRook); //Put the rook at it's new position
            rook.decreaseMoveCount();
        }
    }

    private void undoEnPassant(ChessPiece p, Piece capturedPiece,  Position s, Position t){ //Queen Rook : special movement
        if (p instanceof Pawn){ //Only pawns make and take en passant
            if (s.getColumn() != t.getColumn() && capturedPiece == enPassantVulnerable){ //It was a diagonal move and the pawn was vunarable to en passant = en passant happened

                ChessPiece pawn = (ChessPiece) board.removePiece(t);

                Position pawnToBeRestitutedPosition;
                if(p.getColor() == Color.WHITE){
                    pawnToBeRestitutedPosition = new Position(3,t.getColumn());
                }
                else pawnToBeRestitutedPosition = new Position(4,t.getColumn()); //Is black

                board.placePiece(pawn,pawnToBeRestitutedPosition);
            }
        }
    }

    /*****DEFAULT MOVES*******/
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source); //Validate if the position exist + there is a piece at source position
        validateTargetPosition(source,target);
        Piece capturedPiece = makeMove(source,target); //Captured piece, if there is any (null otherwise)
        ChessPiece movedPiece =  (ChessPiece) board.pieceOnPosition(target);

        promotion(movedPiece,target); //Check if there was a promotion

        if(testCheck(currentplayer)){
            undoMove(source,target,capturedPiece);
            throw new ChessException("You cannot put yourself in check");
        }

        check = testCheck(opponent(currentplayer))? true : false; //Check if this move putted the adversary in check

        if(testCheckMate(opponent(currentplayer)))  checkMate=true; //Ends the match
        else nextTurn(); //Only changes turns if there is NOT a checkmate!
        checkPawnEnPassant(movedPiece,source,target); //Check if there was an En Passant
        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target){ //Move a piece from a source to a target position
        Piece sourcePiece = board.removePiece(source);

        ChessPiece sourceChessPiece = (ChessPiece) sourcePiece; //Downcasting to allow access to the increaseMove method
        sourceChessPiece.increaseMoveCount(); //Increase the move count for this piece

        Piece capturedPiece = board.removePiece(target); //Possible captured piece
        board.placePiece(sourcePiece,target); //Put the source position on its destination
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        smallCastling(sourceChessPiece,source,target); //Special Move
        bigCastling(sourceChessPiece,source,target); //Special Move
        capturedPiece = enPassant(sourceChessPiece,capturedPiece,source,target); //Special move
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturePiece){
        Piece p = board.removePiece(target); //Remove the piece from the target position
        ChessPiece copyP = (ChessPiece) p;   //Downcasting to allow access to the decreaseMove method
        ((ChessPiece) p).decreaseMoveCount(); //Decrease the move count for this piece

        board.placePiece(p,source); //Put the piece back to the source position
        if ( capturePiece != null) {
            board.placePiece(capturePiece,target); //Readd the captured piece to the board
            piecesOnTheBoard.add(capturePiece); //Readd the piece from the 'pieces on the board' list
            capturedPieces.remove(capturePiece); //Remove the piece from the 'captured pieces' list
        }
        undoSmallCastling(copyP,source,target); //Undo special moves
        undoBigCastling(copyP,source,target);
        enPassant(copyP, capturePiece, source,target);
        undoEnPassant(copyP, capturePiece, source,target);
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) throw new ChessException("No piece at the source position");
        if (!board.pieceOnPosition(position).isThereAnyPossibleMove()) throw new ChessException("No possible moves");
        ChessPiece p = (ChessPiece)board.pieceOnPosition(position);
        if (currentplayer !=p.getColor()) throw new ChessException("The selected piece is not yours to be moved");
    }

    private void validateTargetPosition(Position source, Position target){ /*Validate if the current piece can move to
        the target position*/
        if(!board.pieceOnPosition(source).possibleMove(target)) throw new ChessException("The chosen piece cannot be moved to destination.");
    }

    public boolean[][] possibleMoves(ChessPosition source){
        Position position = source.toPosition();
        validateSourcePosition(position);
        return board.pieceOnPosition(position).possibleMoves();
    }

    private void nextTurn(){
        turn++;
        currentplayer = (currentplayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color){
        if (color  == Color.WHITE) return Color.BLACK;
        else return Color.WHITE;
    }

    private List<Piece> getAllPiecesFromColor(Color color){ //Get all pieces on the board from a given color
        List <Piece> allPiecesFromColor = piecesOnTheBoard.stream().filter(
                x -> ((ChessPiece) x).getColor() == color
        ).collect(Collectors.toList());
        return allPiecesFromColor;
    }

    public ChessPiece getKing(Color color){ //Locate and return the king of a given color
        List<Piece> list = getAllPiecesFromColor(color);
        for (Piece p:list) {
            if (p instanceof King)  return ((ChessPiece) p);
        }
        throw new IllegalStateException("There is a king missing on the board!");
    }

    private boolean testCheck(Color color) {
        Position kingPosition = getKing(color).getChessPosition().toPosition();

        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }

        return false;
    }

    private boolean testCheckMate(Color color){
        if (!testCheck(color)) return false; //To be at check mate, it needs firstly be at check!
        List<Piece> list = getAllPiecesFromColor(color);

        for (Piece p:list) { //Check if any piece of the current color can take the king out of check
            boolean[][] matrix = p.possibleMoves();
            for (int i = 0; i < board.getRows() ; i++) { //Running throught the rows of the possible move matrix
                for (int j = 0; j < board.getColumns(); j++) {//Running throught the columns of the possible move matrix
                    if (matrix[i][j])  { //Make sure that this is a valid movement
                        Position source = ((ChessPiece) p).getChessPosition().toPosition(); //Get the pice current position
                        Position target = new Position(i,j); //Get the piece future position
                        Piece capturedPiece = makeMove(source,target); //Make the move and store a possible captured piece
                        boolean testCheck = testCheck(color); //Check if after the move, the king is still at check
                        undoMove(source,target,capturedPiece); //Undo the movement, it was made only for seeing if the king would still be at check //
                        if(!testCheck) return false; //If a piece can remove the king out of a check, is NOT check mate
                    }
                }
            }
        }
        return true;
    }

}
