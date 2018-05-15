//
// Created by Administrator on 2018/5/11 0011.
//
#include <jni.h>
#include "HT_map_update.h"
#include <vector>
#include <string>
#include <math.h>
#include <stdlib.h>
#include <iostream>

#define CLASSNAME_PATH "wtwd/com/superapp/entity/SweepMapEntity"

extern "C" {

unsigned char map_data[110 * 110];    /*创建地图, 并初始化地图值为未知(即255)*/
static const unsigned char NO_INFORMATION = 255;
static const unsigned char LETHAL_OBSTACLE = 254;
static const unsigned char FREE_SPACE = 0;


//int main(int argc, char* argv[]){
//
//}



/**
 *
 * @param env
 * @param object
 * @param classobj
 * @param x 传感器X坐标
 * @param y 传感器Y坐标
 * @param yaw 传感器坐标角度
 * @param sensorType 传感器碰撞
 * @return 传感器周边坐标
 */
JNIEXPORT jobject JNICALL
Java_wtwd_com_superapp_sweepmap_SweepMap_getSweepArray(JNIEnv *env, jobject object,
                                                       jfloat x, jfloat y, jfloat yaw,
                                                       jint sensorType) {

    jclass list_cls = env->FindClass("java/util/ArrayList");//获得ArrayList类引用

    if (list_cls == NULL) {
//        cout << "listcls is null \n";
    }
    jmethodID list_costruct = env->GetMethodID(list_cls, "<init>", "()V"); //获得得构造函数Id

    jobject list_obj = env->NewObject(list_cls, list_costruct); //创建一个Arraylist集合对象
    //或得Arraylist类中的 add()方法ID，其方法原型为： boolean add(Object object) ;
    jmethodID list_add = env->GetMethodID(list_cls, "add", "(Ljava/lang/Object;)Z");


    jclass sweep_obj = env->FindClass(CLASSNAME_PATH);
    jmethodID methodid = env->GetMethodID(sweep_obj, "<init>", "(IIZ)V");
//    jobject sweepmap = (env)->NewObject(obj, methodid);


    //1.创建MapUpdate对象
    MapUpdate map_update(0.2, 80, 80, -8.0f, -8.0f, 0.1);
    SensorType sensor;
    //模拟数据
//    float x = 0, y = 0, yaw = 0.5;                       /**< 接收到的坐标 */
    switch (sensorType) {
        case 0:
            sensor = NONE;
            break;

        case 1:
            sensor = LEFT_BUMPER;
            break;
        case 2:
            sensor = RIGHT_BUMPER;
            break;

        case 3:
            sensor = BOTH_BUMPER;
            break;

        case 4:
            sensor = LEFT_RANG;
            break;
        case 5:
            sensor = RIGHT_RANG;
            break;
        default:
            sensor = NONE;
            break;
    }

    /**< 传感器类型 */
    unsigned int tmp_index;
    float bumper_sensors_theta = (float) (M_PI / 4);      /**<碰撞传感器角度*/
    float robot_radius = map_update.getRobotRadius();

    //2.根据数据计算要更新的位置
    std::vector<MapLocation> map_index;
    map_index.clear();
    //根据传感器类型更新对应位置
    //sensor == NONE,即上报点状态为空闲,此时更新当前上报位置周围的点
    if (sensor == NONE)    //1).无障碍物时更新当前坐标和周围的点为空闲区
    {
        map_update.getFillIndex(x, y, yaw, map_index, sensor);
        int size = map_index.size(); //map_index存储的即为要更新的地图点
        for (int i = 0; i < size; i++) {
            tmp_index = map_update.getIndex(map_index[i].x, map_index[i].y);
            std::cout << "x = " << map_index[i].x << ",y = " << map_index[i].y << std::endl;
            map_data[tmp_index] = FREE_SPACE; //对应位置填充为0-空闲

            jobject sweep_o = env->NewObject(sweep_obj, methodid, map_index[i].x, map_index[i].y,
                                             false);  //构造一个对象
            env->CallBooleanMethod(list_obj, list_add, sweep_o);

        }
    } else {
        std::cout << "sensor = " << sensor << std::endl;
        if (sensor == BOTH_BUMPER) //2).根据碰撞传感器值来更新对应位置为障碍物区
        {
            float xx = (float) (robot_radius * 1.5 + 0.2);
            x += cosf(yaw) * xx;
            y += sinf(yaw) * xx;
        } else if (sensor == LEFT_BUMPER) {
            float xx = (float) ((robot_radius * 1.5 + 0.2) * cos(bumper_sensors_theta));
            float yy = (float) ((robot_radius * 1.5 + 0.2) * sin(bumper_sensors_theta));
            x += xx * cos(yaw) - yy * sin(yaw);
            y += yy * cos(yaw) + xx * sin(yaw);
        } else if (sensor == RIGHT_BUMPER) {
            float xx = (float) ((robot_radius * 1.5 + 0.2) * cos(-bumper_sensors_theta));
            float yy = (float) ((robot_radius * 1.5 + 0.2) * sin(-bumper_sensors_theta));
            x += xx * cos(yaw) - yy * sin(yaw);
            y += yy * cos(yaw) + xx * sin(yaw);
        } else if (sensor == LEFT_RANG) //3).根据沿墙传感器值更新对应位置为障碍物区
        {
            float xx = (float) ((robot_radius * 1.5 + 0.2) * cos(1.1938));
            float yy = (float) ((robot_radius * 1.5 + 0.2) * sin(1.1938));
            x += xx * cos(yaw) - yy * sin(yaw);
            y += yy * cos(yaw) + xx * sin(yaw);
        } else if (sensor == RIGHT_RANG) {
            float xx = (float) ((robot_radius * 1.5 + 0.2) * cos(-1.1938));
            float yy = (float) ((robot_radius * 1.5 + 0.2) * sin(-1.1938));
            x += xx * cos(yaw) - yy * sin(yaw);
            y += yy * cos(yaw) + xx * sin(yaw);
        }
        map_update.getFillIndex(x, y, yaw, map_index, sensor);
        int size = (int) map_index.size();
        std::cout << "************" << std::endl;
        for (int i = 0; i < size; i++) {
            std::cout << "x = " << map_index[i].x << ",y = " << map_index[i].y << std::endl;
            tmp_index = map_update.getIndex(map_index[i].x, map_index[i].y);
            map_data[tmp_index] = LETHAL_OBSTACLE;//对应位置填充为254-障碍物

//            env->SetIntField(sweepmap, jX, map_index[i].x);
//            env->SetIntField(sweepmap, jY, map_index[i].y);
//            env->SetBooleanField(sweepmap, jbumper, (jboolean) true);
//            env->SetObjectArrayElement(maps, i, sweepmap);
            jobject sweep_o = env->NewObject(sweep_obj, methodid, map_index[i].x, map_index[i].y,
                                             true);  //构造一个对象
            env->CallBooleanMethod(list_obj, list_add, sweep_o);
        }
    }

    return list_obj;
}


}

