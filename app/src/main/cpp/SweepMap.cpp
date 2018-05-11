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

extern "C" {

unsigned char map_data[6400];    /*创建地图, 并初始化地图值为未知(即255)*/
static const unsigned char NO_INFORMATION = 255;
static const unsigned char LETHAL_OBSTACLE = 254;
static const unsigned char FREE_SPACE = 0;


JNIEXPORT jstring JNICALL
Java_wtwd_com_superapp_sweepmap_SweepMap_getHello(JNIEnv *env, jobject object, jstring a) {


    return a;
}


JNIEXPORT std::vector<MapLocation> JNICALL
Java_wtwd_com_superapp_sweepmap_SweepMap_get(JNIEnv *env, jobject object, jstring a) {

//    //1.创建MapUpdate对象
//    MapUpdate map_update(0.2, 80, 80, -8.0, -8.0, 0.1);
//    //模拟数据
//    float x = 0, y = 0, yaw = 0.5;                       /**< 接收到的坐标 */
//    SensorType sensor = LEFT_RANG;                      /**< 传感器类型 */
//    unsigned int tmp_index;
//    float bumper_sensors_theta = M_PI / 4;      /**<碰撞传感器角度*/
//    float robot_radius = map_update.getRobotRadius();
//
//    //2.根据数据计算要更新的位置
//    std::vector<MapLocation> map_index;
//    map_index.clear();
//    //根据传感器类型更新对应位置
//    //sensor == NONE,即上报点状态为空闲,此时更新当前上报位置周围的点
//    if (sensor == NONE)    //1).无障碍物时更新当前坐标和周围的点为空闲区
//    {
//        map_update.getFillIndex(x, y, yaw, map_index, sensor);
//        int size = map_index.size(); //map_index存储的即为要更新的地图点
//        for (int i = 0; i < size; i++) {
//            tmp_index = map_update.getIndex(map_index[i].x, map_index[i].y);
//            std::cout << "x = " << map_index[i].x << ",y = " << map_index[i].y << std::endl;
//            map_data[tmp_index] = FREE_SPACE; //对应位置填充为0-空闲
//        }
//    } else {
//        std::cout << "sensor = " << sensor << std::endl;
//        if (sensor == BOTH_BUMPER) //2).根据碰撞传感器值来更新对应位置为障碍物区
//        {
//            float xx = (robot_radius * 1.5 + 0.2);
//            x += cosf(yaw) * xx;
//            y += sinf(yaw) * xx;
//        } else if (sensor == LEFT_BUMPER) {
//            float xx = (robot_radius * 1.5 + 0.2) * cos(bumper_sensors_theta);
//            float yy = (robot_radius * 1.5 + 0.2) * sin(bumper_sensors_theta);
//            x += xx * cos(yaw) - yy * sin(yaw);
//            y += yy * cos(yaw) + xx * sin(yaw);
//        } else if (sensor == RIGHT_BUMPER) {
//            float xx = (robot_radius * 1.5 + 0.2) * cos(-bumper_sensors_theta);
//            float yy = (robot_radius * 1.5 + 0.2) * sin(-bumper_sensors_theta);
//            x += xx * cos(yaw) - yy * sin(yaw);
//            y += yy * cos(yaw) + xx * sin(yaw);
//        } else if (sensor == LEFT_RANG) //3).根据沿墙传感器值更新对应位置为障碍物区
//        {
//            float xx = (robot_radius * 1.5 + 0.2) * cos(1.1938);
//            float yy = (robot_radius * 1.5 + 0.2) * sin(1.1938);
//            x += xx * cos(yaw) - yy * sin(yaw);
//            y += yy * cos(yaw) + xx * sin(yaw);
//        } else if (sensor == RIGHT_RANG) {
//            float xx = (robot_radius * 1.5 + 0.2) * cos(-1.1938);
//            float yy = (robot_radius * 1.5 + 0.2) * sin(-1.1938);
//            x += xx * cos(yaw) - yy * sin(yaw);
//            y += yy * cos(yaw) + xx * sin(yaw);
//        }
//        map_update.getFillIndex(x, y, yaw, map_index, sensor);
//        int size = map_index.size();
//        std::cout << "************" << std::endl;
//        for (int i = 0; i < size; i++) {
//            std::cout << "x = " << map_index[i].x << ",y = " << map_index[i].y << std::endl;
//            tmp_index = map_update.getIndex(map_index[i].x, map_index[i].y);
//            map_data[tmp_index] = LETHAL_OBSTACLE;//对应位置填充为254-障碍物
//        }
//    }



}


}

