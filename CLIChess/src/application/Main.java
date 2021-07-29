package application;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static void welcomeMsg(){
        System.out.println(UI.ANSI_CYAN + " __          __       __  __  __" +UI.ANSI_RESET);
        System.out.println(UI.ANSI_GREEN+"/   |   |   /   |__| |_  (_  (_"+UI.ANSI_RESET);
        System.out.println(UI.ANSI_PURPLE+"\\__ |__ |   \\__ |  | |__ __) __)\n"+UI.ANSI_RESET);

        System.out.println(UI.ANSI_RED+"\nPRESS [ENTER] TO START! \n" + UI.ANSI_RESET);

        Scanner scan = new Scanner(System.in);
        scan.nextLine();
    }

    private static void runChess(){
        welcomeMsg();
        ChessMatch chessMatch = new ChessMatch();
        Scanner scan = new Scanner(System.in);
        List<ChessPiece> captured = new ArrayList<>();

        while (!chessMatch.getCheckMate()){
            try{
                UI.clearScreen();
                UI.printMatch(chessMatch, captured);

                System.out.println();
                System.out.print("Type the origin: ");
                ChessPosition source = UI.readChessPosition(scan);

                UI.clearScreen();
                boolean[][] possiblemoves = chessMatch.possibleMoves(source);
                UI.printBoard(chessMatch.getPieces(), possiblemoves);

                System.out.println();
                System.out.print("Type the destination: ");
                ChessPosition target = UI.readChessPosition(scan);

                ChessPiece capturedpiece = chessMatch.performChessMove(source,target);
                if (capturedpiece != null) captured.add(capturedpiece);


                if(chessMatch.getPromoted()!=null){ //A piece has been promoted!
                    System.out.println("Enter piece for promotion (B/N/Q/R): ");
                    String promotionType = scan.nextLine().toUpperCase();
                    while (!promotionType.equals("B") && !promotionType.equals("N") && !promotionType.equals("R") & !promotionType.equals("Q")) {
                        System.out.println(UI.ANSI_RED + "Invalid piece!" + UI.ANSI_RESET);
                        System.out.println("Enter piece for promotion (B/N/Q/R): ");
                        promotionType = scan.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(promotionType);
                }
            }
            catch (ChessException e){
                System.out.println(e.getMessage());
                scan.nextLine();
            }

            catch (InputMismatchException e){
                System.out.println(e.getMessage());
                scan.nextLine();
            }
        }
        UI.clearScreen();
        UI.printMatch(chessMatch,captured);
    }

    public static void main(String[] args) {
        runChess();
    }
}
