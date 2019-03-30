package alghoritms.print;

import alghoritms.model.PieceColor;
import alghoritms.model.agent.Piece;
import alghoritms.model.environment.Table;
import alghoritms.model.environment.TableQuadrant;


public class Printer {

    public static void printTable(Table table){
        for (int j = 0; j < 34; j++){
            System.out.print("-");
        }
        System.out.println();
        for (int i = 7; i >= 0; i--){
            System.out.print(i + " |");
            for (int j = 0; j < 8; j++){
                printQuadrant(table.getTable()[i][j]);
            }
            System.out.println();
            for (int j = 0; j < 34; j++){
                System.out.print("-");
            }
            System.out.println();

        }
        System.out.print("   ");
        for (int j = 0; j < 8; j++){
            System.out.print(" " + j + "  ");
        }
        System.out.println();
    }

    public static void printRevertIndexes(){
        for (int i = 7; i >= 0; i--){
            for (int j = 0; j < 8; j++){
                System.out.print( "i = " + i + "j = " + j + "; ");
            }
            System.out.println();
        }
    }

    public static void printIndexes(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                System.out.print( "i = " + i + "j = " + j + "; ");
            }
            System.out.println();
        }
    }

    private static void printQuadrant(TableQuadrant tableQuadrant){
        Piece piece = tableQuadrant.getPiece();
        if (piece != null){
            if (piece.isQueen()){
                System.out.print(piece.getPieceColor() == PieceColor.WHITE ? " WQ|" : " BQ|");
            }else{
                System.out.print(piece.getPieceColor() == PieceColor.WHITE ? " W |" : " B |");
            }

        }else {
            System.out.print(tableQuadrant.getPieceColor() == PieceColor.WHITE ? "   |" : "   |");
        }
    }
}
