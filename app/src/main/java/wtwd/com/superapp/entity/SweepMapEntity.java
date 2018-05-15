package wtwd.com.superapp.entity;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class SweepMapEntity {

    private int x;
    private int y;
    private boolean bumper;

    public SweepMapEntity(){

    }

    public SweepMapEntity(int x,int y,boolean bumper){
        this.x = x;
        this.y = y;
        this.bumper = bumper;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isBumper() {
        return bumper;
    }

    public void setBumper(boolean bumper) {
        this.bumper = bumper;
    }


    @Override
    public String toString() {
        return "SweepMapEntity{" +
                "x=" + x +
                ", y=" + y +
                ", bumper=" + bumper +
                '}';
    }
}
