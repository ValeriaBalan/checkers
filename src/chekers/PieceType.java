package chekers;

/**
 * Created by Rumpi on 6/2/2017.
 */
public enum PieceType {
    BLACK(1), WHITE(-1);

    final int moveDir;

    PieceType (int moveDir){
        this.moveDir = moveDir;
    }

}
