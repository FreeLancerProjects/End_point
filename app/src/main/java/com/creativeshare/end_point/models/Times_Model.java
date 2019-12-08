package com.creativeshare.end_point.models;

import java.io.Serializable;
import java.util.List;

public class Times_Model implements Serializable {
     private int current_page;
     private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private String date;
        private String attendance_time;
        private String departure_time;
        private String time_plus;
        private String status;
        private String number_of_mins;
        private String reason_title;
        private String late_status;

        public String getDate() {
            return date;
        }

        public String getAttendance_time() {
            return attendance_time;
        }

        public String getDeparture_time() {
            return departure_time;
        }

        public String getTime_plus() {
            return time_plus;
        }

        public String getStatus() {
            return status;
        }

        public String getNumber_of_mins() {
            return number_of_mins;
        }

        public String getReason_title() {
            return reason_title;
        }

        public String getLate_status() {
            return late_status;
        }
    }
}
