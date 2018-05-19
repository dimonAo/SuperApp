package wtwd.com.superapp.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/19 0019.
 */

public class AppointCleanTaskEntity {
    private String id;
    private String name;
    private String desc;
    private String status;
    private String execute_time;
    private String owner_type;
    private String owner_id;
    private String create_time;
    private List<ActionEntity> actions;
    private TimerEntity timer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecute_time() {
        return execute_time;
    }

    public void setExecute_time(String execute_time) {
        this.execute_time = execute_time;
    }

    public String getOwner_type() {
        return owner_type;
    }

    public void setOwner_type(String owner_type) {
        this.owner_type = owner_type;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public List<ActionEntity> getActions() {
        return actions;
    }

    public void setActions(List<ActionEntity> actions) {
        this.actions = actions;
    }

    public TimerEntity getTimer() {
        return timer;
    }

    public void setTimer(TimerEntity timer) {
        this.timer = timer;
    }

    private class ActionEntity {
        private String type;
        private String name;
        private String moment;
        private ConfigEntigy config;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoment() {
            return moment;
        }

        public void setMoment(String moment) {
            this.moment = moment;
        }

        public ConfigEntigy getConfig() {
            return config;
        }

        public void setConfig(ConfigEntigy config) {
            this.config = config;
        }
    }

    /**
     * ConfigEntity DataPoint为actions字段
     */
    private class ConfigEntigy {
        private long device_id;
        private List<DataPoint> datapoint;

        public long getDevice_id() {
            return device_id;
        }

        public void setDevice_id(long device_id) {
            this.device_id = device_id;
        }

        public List<DataPoint> getDatapoint() {
            return datapoint;
        }

        public void setDatapoint(List<DataPoint> datapoint) {
            this.datapoint = datapoint;
        }
    }

    private class DataPoint {
        private String index;
        private String value;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    /**
     * TimerEntity ScheduleEntity为timer字段
     */
    private class TimerEntity {
        private String type;
        private String first_execute_time;
        private String last_execute_time;
        private ScheduleEntity schedule;
        private List<String> exclude_day;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getFirst_execute_time() {
            return first_execute_time;
        }

        public void setFirst_execute_time(String first_execute_time) {
            this.first_execute_time = first_execute_time;
        }

        public String getLast_execute_time() {
            return last_execute_time;
        }

        public void setLast_execute_time(String last_execute_time) {
            this.last_execute_time = last_execute_time;
        }

        public ScheduleEntity getSchedule() {
            return schedule;
        }

        public void setSchedule(ScheduleEntity schedule) {
            this.schedule = schedule;
        }

        public List<String> getExclude_day() {
            return exclude_day;
        }

        public void setExclude_day(List<String> exclude_day) {
            this.exclude_day = exclude_day;
        }
    }


    private class ScheduleEntity {
        private String unit;
        private String interval;
        private List<String> include_weekday;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getInterval() {
            return interval;
        }

        public void setInterval(String interval) {
            this.interval = interval;
        }

        public List<String> getInclude_weekday() {
            return include_weekday;
        }

        public void setInclude_weekday(List<String> include_weekday) {
            this.include_weekday = include_weekday;
        }
    }


}
