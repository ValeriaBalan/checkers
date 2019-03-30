package alghoritms.model;

public enum PieceColor {
    BLACK(1, "Black"),
    WHITE(-1, "White");

    PieceColor(int intValue, String stringValue){
        this.moveDir = intValue;
        this.stringValue = stringValue;
    }

    private int moveDir;
    private String stringValue;

    public int moveDir(){
        return moveDir;
    }
    public String stringValue(){
        return stringValue;
    }

    public PieceColor oppositeColor(){
        return this.moveDir == 1 ? WHITE : BLACK;
    }

}
