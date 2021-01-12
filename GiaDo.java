/*
    @ Author         : Truong Cong Huy
    @ Class          : CS 311 G
    @ Student Number : 2521 1212 029
*/

public class GiaDo {
    private int xGiaDo;
    private int yGiaDo;

    public GiaDo(int xGiaDo, int yGiaDo) {
        this.xGiaDo = xGiaDo;
        this.yGiaDo = yGiaDo;
    }

    public int getxGiaDo() {
        return xGiaDo;
    }

    public void setxGiaDo(int xGiaDo) {
        this.xGiaDo = xGiaDo;
    }

    public int getyGiaDo() {
        return yGiaDo;
    }

    public void setyGiaDo(int yGiaDo) {
        this.yGiaDo = yGiaDo;
    }

    @Override
    public String toString() {
        return "Toa do: (" + getxGiaDo() + ", " + getyGiaDo() + ")";
    }

}
