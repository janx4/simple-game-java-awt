/*
    @ Author         : Truong Cong Huy
    @ Class          : CS 311 G
    @ Student Number : 2521 1212 029
*/

public class Ball {
    private int ballx, bally;
    private int ballxSpeed, ballySpeed;

    // CONSTRUCTOR
    public Ball(int ballx, int bally, int ballxSpeed, int ballySpeed) {
        this.ballx = ballx;
        this.bally = bally;
        this.ballxSpeed = ballxSpeed;
        this.ballySpeed = ballySpeed;
    }

    public Ball() {
    }

    // GETTER, SETTER
    public int getBallx() {
        return this.ballx;
    }

    public void setBallx(int ballx) {
        this.ballx = ballx;
    }

    public int getBally() {
        return this.bally;
    }

    public void setBally(int bally) {
        this.bally = bally;
    }

    public int getBallxSpeed() {
        return this.ballxSpeed;
    }

    public void setBallxSpeed(int ballxSpeed) {
        this.ballxSpeed = ballxSpeed;
    }

    public int getBallySpeed() {
        return this.ballySpeed;
    }

    public void setBallySpeed(int ballySpeed) {
        this.ballySpeed = ballySpeed;
    }

    // TOSTING METHOD
    @Override
    public String toString() {
        return "Toa do: (" + getBallx() + ", " + getBally() + ")";
    }

}
