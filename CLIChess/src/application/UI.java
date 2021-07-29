package application;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UI { //Printing the CLI UI

    //FOR COLOR PRINTING!
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

    //Text Colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //Background Colors
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";


    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static ChessPosition readChessPosition(Scanner scan){ //Read user input
        try {
            String str = scan.nextLine();
            char column = str.charAt(0);
            int row = Integer.parseInt(str.substring(1));
            return new ChessPosition(row,column);
        }
        catch (RuntimeException e){
            throw new InputMismatchException("Input error! Valid values are from a1 to h8");
        }
    }

    private static void printLetters(){ //Print  from A to H for the board
        System.out.print("  ");
        for(char ch = 'a' ; ch <= 'h' ; ch++ ){
            System.out.print(ch + " ");
        }
        System.out.println();
    }

    public static void printBoard (ChessPiece[][] pieces){ //Print the current board
        for (int i = 0; i < pieces.length ; i++) {
            System.out.print((8 - i) + " "); // print 8 -> 1
            for (int j = 0; j < pieces.length ; j++) { //Square matrix!
                printPiece(pieces[i][j],false); //Print the piece at index[i][j]
            }
            System.out.println(" ");
        }
        printLetters();
    }

    public static void printBoard (ChessPiece[][] pieces, boolean[][] possiblemoves){ //Print board listing possible moves!
        for (int i = 0; i < pieces.length ; i++) {
            System.out.print((8 - i) + " "); // print 8 -> 1
            for (int j = 0; j < pieces.length ; j++) { //Square matrix
                printPiece(pieces[i][j],possiblemoves[i][j]); //Print the piece at index[i][j] and its possible moves
            }
            System.out.println(" ");
        }
        printLetters();
    }

    private static void printPiece(ChessPiece piece, boolean colored) { //Prints piece with color
        if (colored) System.out.print(ANSI_BLUE_BACKGROUND);
        if (piece == null) System.out.print("-"+ANSI_RESET); //Null -> no piece
        else {
            if (piece.getColor() == Color.WHITE) System.out.print(ANSI_WHITE + piece + ANSI_RESET); //Is a white piece
            else System.out.print(ANSI_YELLOW + piece + ANSI_RESET); //Is a black piece
        }
        System.out.print(" ");
    }

    public static void printMatch(ChessMatch chessMatch,  List<ChessPiece> captured){
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn : " + chessMatch.getTurn());
        System.out.println("Waiting player : "+ chessMatch.getCurrentPlayer());
        if (!chessMatch.getCheckMate()){
            if (chessMatch.getCheck()){
                if (chessMatch.getCurrentPlayer() == Color.WHITE)   System.out.println(ANSI_WHITE + "\nWHITES IS ON CHECK!" + ANSI_RESET);
                else System.out.println(ANSI_YELLOW + "\nBLACK IS ON CHECK!" + ANSI_RESET);
            }
        }

        else {
            System.out.println(ANSI_RED + "CHECKMATE!" + ANSI_RESET);
            System.out.println("Winner: " +  chessMatch.getCurrentPlayer());
        }

    }

    private static void printCapturedPieces(List<ChessPiece> captured){
        List<ChessPiece> white = captured.stream() //Put all white captured pieces on the 'white' list
                .filter(w -> w.getColor() == Color.WHITE)
                .collect(Collectors.toList());

        List<ChessPiece> black = captured.stream() //Put all black captured pieces on the 'black' list
                .filter(b -> b.getColor() == Color.BLACK)
                .collect(Collectors.toList());
        System.out.println("Captured pieces : ");
        System.out.print(ANSI_WHITE); //Make the white pieces be printed on the white color
        System.out.print("WHITE: ");
        System.out.println(Arrays.toString(white.toArray())); //Print the full list, like python
        System.out.print(ANSI_RESET);

        System.out.print(ANSI_YELLOW); //Make the black pieces be printed on the yellow color
        System.out.print("BLACK: ");
        System.out.println(Arrays.toString(black.toArray())); //Print the full list
        System.out.print(ANSI_RESET);
    }
}
