package wtwd.com.superapp.sweepmap;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class SweepMap {

    static {
        System.loadLibrary("SweepMap");
    }

    public native String getHello(String a);

}
