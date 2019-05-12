package alghoritms.actions;

import java.util.Arrays;

public enum Action {
    FRONT_RIGHT(0, false, 1, 1, "Front right : 0"),
    FRONT_LEFT(1, false, -1, 1, "Front left : 1"),
    BACK_RIGHT(2, false, 1, -1, "Back right : 2"),
    BACK_LEFT(3, false, -1, -1, "Back left : 3"),
    TAKE_PIECE_FRONT_RIGHT(4, true, 2, 2, "Attack front right : 4"),
    TAKE_PIECE_FRONT_LEFT(5, true, -2, 2, "Attack front left : 5"),
    TAKE_PIECE_BACK_RIGHT(6, true, 2, -2,"Attack back right : 6"),
    TAKE_PIECE_BACK_LEFT(7, true, -2, -2,"Attack back left : 7");

    Action(int intValue, boolean attackAction, int horizontalMove, int verticalMove, String stringValue){
        this.intValue = intValue;
        this.attackAction = attackAction;
        this.horizontalMove = horizontalMove;
        this.verticalMove = verticalMove;
        this.stringValue = stringValue;
    }

    private int intValue;
    private boolean attackAction;
    private int horizontalMove;
    private int verticalMove;
    private String stringValue;

    public int toInt(){
        return intValue;
    }

    public String stringValue(){
        return stringValue;
    }

    public boolean isAttackAction(){
        return attackAction;
    }

    public int getHorizontalMove() {
        return horizontalMove;
    }

    public int getVerticalMove() {
        return verticalMove;
    }

    public static Action getActionByIntValue(int intValue){
        Action[] actions = Action.values();
        return Arrays.stream(actions).filter(action -> action.intValue == intValue).findFirst().orElse(null);
    }

    public Action getOpositeAtackAction(){
        switch (this){
            case TAKE_PIECE_BACK_LEFT:
                return TAKE_PIECE_FRONT_RIGHT;
            case TAKE_PIECE_FRONT_LEFT:
                return TAKE_PIECE_BACK_RIGHT;
            case TAKE_PIECE_BACK_RIGHT:
                return TAKE_PIECE_FRONT_LEFT;
            case TAKE_PIECE_FRONT_RIGHT:
                return TAKE_PIECE_BACK_LEFT;
        }
        return null;
    }



}
