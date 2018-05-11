//robot_radius = 0.1;
#include "HT_map_update.h"
#include <math.h>
#include <algorithm>    // std::unique
#include <string>

/*
* function : getFillIndex
* describe: 获得要填充的点的地图坐标
* param: x
* param: y
* param: theta
* param: fill_index 要填充的点的地图坐标
* return: 
*/
void MapUpdate::getFillIndex(float x, float y, float theta, std::vector<MapLocation> &fill_index,
                             SensorType sensor_type) {
    std::vector<Point> footprint_polygon, polygen;
    footprint_polygon = footprint_;
    if (sensor_type != NONE) {
        padFootprint(footprint_polygon, -0.2, 0);
    }
    transformFootprint(x, y, theta, footprint_polygon, polygen);
    std::vector<unsigned int> index_list;
    getConvexPolygon(polygen, index_list);
    //去除重复点
    std::sort(index_list.begin(), index_list.end());
    index_list.erase(std::unique(index_list.begin(), index_list.end()), index_list.end());
    //转成坐标
    int list_size = index_list.size();
    unsigned int loc_x = 0, loc_y = 0;
    MapLocation tmp_loc;
    for (int i = 0; i < index_list.size(); i++) {
        indexToCells(index_list[i], loc_x, loc_y);
        tmp_loc.x = loc_x;
        tmp_loc.y = loc_y;
        fill_index.push_back(tmp_loc);
    }
}

/*
* function : makeFootprintFromRadius
* describe: 获得机器的边界点footprint
* param: robot_radius 机器人半径
* return: 机器人边界点
*/
std::vector<Point> MapUpdate::makeFootprintFromRadius(const float robot_radius) {
    std::vector<Point> points;
    int N = 16;
    Point pt;
    for (int i = 0; i < N; ++i) {
        float angle = i * 2 * M_PI / N;
        pt.x = cosf(angle) * robot_radius;
        pt.y = sinf(angle) * robot_radius;
        points.push_back(pt);
    }
    return points;
}

/*
* function : transformFootprint
* describe: 将footprint变换到当前坐标
* param: x  机器人x坐标
* param: y  机器人y坐标
* param: theta 机器人朝向theta
* param: footprint_spec 原始footprint  
* param: oriented_footprint 输出变换后footprint
*/
void
MapUpdate::transformFootprint(float x, float y, float theta, std::vector<Point> &footprint_spec,
                              std::vector<Point> &oriented_footprint) {
    // build the oriented footprint at a given location
    oriented_footprint.clear();
    float cos_th = cosf(theta);
    float sin_th = sinf(theta);
    for (unsigned int i = 0; i < footprint_spec.size(); ++i) {
        Point new_pt;
        new_pt.x = x + (footprint_spec[i].x * cos_th - footprint_spec[i].y * sin_th);
        new_pt.y = y + (footprint_spec[i].x * sin_th + footprint_spec[i].y * cos_th);
        oriented_footprint.push_back(new_pt);
    }
}

void
MapUpdate::padFootprint(std::vector<Point> &footprint, float width_padding, float length_padding) {
    // pad footprint in place
    for (unsigned int i = 0; i < footprint.size(); i++) {
        Point &pt = footprint[i];
        pt.x += sign0(pt.x) * width_padding;
        pt.y += sign0(pt.y) * length_padding;
    }
}

/*
* function : getConvexPolygonCost
* describe: 获得要更新点的下标
* param: polygon 机器人边界点
* return: 机器人边界点
*/
bool MapUpdate::getConvexPolygon(const std::vector<Point> &polygon,
                                 std::vector<unsigned int> &index_container) {
    std::vector<MapLocation> map_polygon;
    MapLocation loc;
    for (unsigned int i = 0; i < polygon.size(); ++i) {
        if (!worldToMap(polygon[i].x, polygon[i].y, loc.x, loc.y)) //将边界世界坐标转为地图坐标
        {
            return false;
        }
        map_polygon.push_back(loc);
    }
    if (map_polygon.size() == 0) {
        // DEBUG_LOG(DEBUG_THIS_FILE, "have no polygon update\r\n");
        return false;
    }
    std::vector<MapLocation> polygon_cells;
    // 获得填充footprint多边形的地图坐标
    convexFillCells(map_polygon, polygon_cells);
    // index_container = polygon_cells;
    //将坐标转换为数组下标并输出
    for (unsigned int i = 0; i < polygon_cells.size(); ++i) {
        unsigned int index = getIndex(polygon_cells[i].x, polygon_cells[i].y);
        index_container.push_back(index);
    }
    return true;
}

/*
* function : convexFillCells
* describe: 获得填充点坐标
* param: polygon 机器人边界点地图坐标
* param: polygon_cells 输出要填充的地图坐标
*/
void MapUpdate::convexFillCells(const std::vector<MapLocation> &polygon,
                                std::vector<MapLocation> &polygon_cells) {
    if (polygon.size() < 3)
        return;
    // 获得边界点的地图坐标
    polygonOutlineCells(polygon, polygon_cells);
    MapLocation swap;
    unsigned int i = 0;
    while (i < polygon_cells.size() - 1)//快速冒泡排序
    {
        if (polygon_cells[i].x > polygon_cells[i + 1].x) {
            swap = polygon_cells[i];
            polygon_cells[i] = polygon_cells[i + 1];
            polygon_cells[i + 1] = swap;
            if (i > 0)
                --i;
        } else
            ++i;
    }
    i = 0;
    MapLocation min_pt;
    MapLocation max_pt;
    unsigned int min_x = polygon_cells[0].x;
    unsigned int max_x = polygon_cells[polygon_cells.size() - 1].x;
    for (unsigned int x = min_x; x <= max_x; ++x) {
        if (i >= polygon_cells.size() - 1)
            break;

        if (polygon_cells[i].y < polygon_cells[i + 1].y) {
            min_pt = polygon_cells[i];
            max_pt = polygon_cells[i + 1];
        } else {
            min_pt = polygon_cells[i + 1];
            max_pt = polygon_cells[i];
        }

        i += 2;
        while (i < polygon_cells.size() && polygon_cells[i].x == x)//一列上
        {
            if (polygon_cells[i].y < min_pt.y)
                min_pt = polygon_cells[i];
            else if (polygon_cells[i].y > max_pt.y)
                max_pt = polygon_cells[i];
            ++i;
        }

        MapLocation pt;
        // loop though cells in the column
        for (unsigned int y = min_pt.y; y < max_pt.y; ++y) {
            pt.x = x;
            pt.y = y;
            polygon_cells.push_back(pt);
        }
    }
}

/*
* function : polygonOutlineCells
* describe : 获取机器人边界
* param: polygon 地图坐标系下坐标
* param: polygon_cells
*/
void MapUpdate::polygonOutlineCells(const std::vector<MapLocation> &polygon,
                                    std::vector<MapLocation> &polygon_cells) {
    for (unsigned int i = 0; i < polygon.size() - 1; ++i) {
        raytraceLine(polygon_cells, polygon[i].x, polygon[i].y, polygon[i + 1].x, polygon[i + 1].y);
    }
    if (!polygon.empty()) {
        unsigned int last_index = polygon.size() - 1;
        // we also need to close the polygon by going from the last point to the first
        raytraceLine(polygon_cells, polygon[last_index].x, polygon[last_index].y, polygon[0].x,
                     polygon[0].y);
    }
}

/*
* function : raytraceLine
* describe : 
* param: 
* param: 
*/
void
MapUpdate::raytraceLine(std::vector<MapLocation> &polygon_cells, unsigned int x0, unsigned int y0,
                        unsigned int x1, unsigned int y1,
                        unsigned int max_length) //max_length = UINT_MAX
{
    int dx = x1 - x0;
    int dy = y1 - y0;

    unsigned int abs_dx = abs(dx);
    unsigned int abs_dy = abs(dy);

    int offset_dx = sign(dx);
    int offset_dy = sign(dy) * size_x_;

    unsigned int offset = y0 * size_x_ + x0;

    // we need to chose how much to scale our dominant dimension, based on the maximum length of the line
    float dist = hypotf(dx, dy); // hypot
    float scale = (dist < 0.0f) ? 1.0f : fminf(1.0f, max_length / dist);

    // if x is dominant
    if (abs_dx >= abs_dy) {
        int error_y = abs_dx / 2;
        bresenham2D(polygon_cells, abs_dx, abs_dy, error_y, offset_dx, offset_dy, offset,
                    (unsigned int) (scale * abs_dx));
        return;
    }

    // otherwise y is dominant
    int error_x = abs_dy / 2;
    bresenham2D(polygon_cells, abs_dy, abs_dx, error_x, offset_dy, offset_dx, offset,
                (unsigned int) (scale * abs_dy));
}

/*
* function : bresenham2D
* describe : 
* param: 
* param: 
*/
void MapUpdate::bresenham2D(std::vector<MapLocation> &polygon_cells, unsigned int abs_da,
                            unsigned int abs_db, int error_b, int offset_a,
                            int offset_b, unsigned int offset, unsigned int max_length) {
    unsigned int end = max_length < abs_da ? max_length : abs_da;
    MapLocation loc = {0, 0};
    for (unsigned int i = 0; i < end; ++i) {
        // 
        indexToCells(offset, loc.x, loc.y);
        polygon_cells.push_back(loc);
        // 
        offset += offset_a;
        error_b += abs_db;
        if ((unsigned int) error_b >= abs_da) {
            offset += offset_b;
            error_b -= abs_da;
        }
    }
    indexToCells(offset, loc.x, loc.y);
    polygon_cells.push_back(loc);
}

std::string MapUpdate::getHello(std::string a) {
    return a;
}