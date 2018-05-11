/*
* class : MapUpdate
* describe: 地图更新相关操作
*/
#include "vector"
#include "map"
#include "stdlib.h"
#include "stdio.h"
#include <string>
struct Point
{
    float x;
    float y;
};
struct MapLocation
{
    unsigned int x;
    unsigned int y;
};
enum SensorType
{
    NONE,           /**< 无传感器触发*/
    LEFT_BUMPER,    /**< 左碰撞*/
    RIGHT_BUMPER,   /**< 右碰撞*/
    BOTH_BUMPER,    /**< 中间碰撞*/
    LEFT_RANG,      /**< 左沿墙*/
    RIGHT_RANG      /**< 右沿墙*/
};
class MapUpdate
{
public:
	MapUpdate(float resolution, unsigned int size_x, 
		unsigned int size_y, float origin_x, float origin_y, float robot_radius):
		resolution_(resolution),
		size_x_(size_x),
		size_y_(size_y),
		origin_x_(origin_x),
		origin_y_(origin_y),
		robot_radius_(robot_radius)
	{
		footprint_ = makeFootprintFromRadius(robot_radius_);
	}
	~MapUpdate(){};
    static std::string getHello(std::string a);
    void getFillIndex(float x, float y, float theta, std::vector<MapLocation> &fill_index , SensorType sensor_type);
	std::vector<Point> makeFootprintFromRadius(const float robot_radius);
	void padFootprint(std::vector<Point>& footprint, float width_padding , float length_padding);
	void transformFootprint(float x, float y, float theta, std::vector<Point>& footprint_spec ,std::vector<Point>& oriented_footprint);
	bool getConvexPolygon(const std::vector<Point>& polygon,  std::vector<unsigned int>  &index_container);
	void convexFillCells(const std::vector<MapLocation>& polygon, std::vector<MapLocation>& polygon_cells);
	void polygonOutlineCells(const std::vector<MapLocation>& polygon, std::vector<MapLocation>& polygon_cells);
	void raytraceLine(std::vector<MapLocation>& polygon_cells, unsigned int x0, unsigned int y0, unsigned int x1, unsigned int y1,
                         unsigned int max_length = 0xffffffff) ;//max_length = UINT_MAX
	void bresenham2D(std::vector<MapLocation>& polygon_cells, unsigned int abs_da, unsigned int abs_db, int error_b, int offset_a,
                        int offset_b, unsigned int offset, unsigned int max_length);

	void setFootprint(std::vector<Point> footprint)
	{
		footprint = footprint_;
	}

	inline bool worldToMap(float wx, float wy, unsigned int& mx, unsigned int& my) const
	{
		if (wx < origin_x_ || wy < origin_y_)
		    return false;

		mx = (int)((wx - origin_x_) / resolution_);
		my = (int)((wy - origin_y_) / resolution_);
		if (mx < size_x_ && my < size_y_)
		    return true;
		else
		    return false;
	}
	/*
	* function : getIndex
	* describe: 地图坐标转下标
	* param: 
	* param: 
	* return 
	*/
	inline unsigned int getIndex(unsigned int mx, unsigned int my) const
	{
		return my * size_x_ + mx;
	}
	/*
	* function : indexToCells
	* describe: 下标转地图坐标
	* param: 
	* param: 
	* return 
	*/   
	inline void indexToCells(unsigned int index, unsigned int& mx, unsigned int& my) const
	{
	    my = index / size_x_;
	    mx = index - (my * size_x_);
	}
	inline int sign(int x)
	{
		return x > 0 ? 1.0 : -1.0;
	}
	inline float sign0(float x)
	{
	    return x < 0.0f ? -1.0f : (x > 0.0f ? 1.0f : 0.0f);
	}

	inline float getRobotRadius()
	{
		return robot_radius_;
	}


private:
	float resolution_; 	/**<地图分辨率 - 0.2*/
	unsigned int size_x_;	/**<地图大小 -110*110*/
	unsigned int size_y_;	
	float origin_x_;		/**<原点- -8*/ 
	float origin_y_;		/**<原点- -8*/ 
	float robot_radius_;	/**<机器人半径- 0.1*/ 
	std::vector<Point> footprint_;
};
