package wtwd.com.superapp.sweepmap;

import java.util.ArrayList;

import wtwd.com.superapp.entity.SweepMapEntity;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class SweepMap {

    static {
        System.loadLibrary("SweepMap");
    }

    /**
     * 获取扫地机坐标
     *
     * @param x          设备上传点X坐标
     * @param y          设备上传点Y坐标
     * @param raw        设备上传点坐标角度
     * @param sensorType 设备上传碰撞位置
     * @return 扫地机通用坐标点位置及是否为碰撞状态
     */
    public native ArrayList<SweepMapEntity> getSweepArray(float x, float y, float raw, int sensorType);

}
