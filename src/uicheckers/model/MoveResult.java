package uicheckers.model;

public class MoveResult {
    private MoveType type;
    private UIPiece UIPiece;

    public MoveResult(MoveType type, UIPiece UIPiece) {
        this.type = type;
        this.UIPiece = UIPiece;
    }

    public MoveResult (MoveType type){
        this(type, null);
    }

    public MoveType getType() {
        return type;
    }

    public UIPiece getUIPiece() {
        return UIPiece;
    }

    public void setUIPiece(UIPiece UIPiece) {
        this.UIPiece = UIPiece;
    }

    public void setType(MoveType type) {
        this.type = type;
    }
}
