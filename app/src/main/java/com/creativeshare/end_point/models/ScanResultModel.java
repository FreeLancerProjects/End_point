package com.creativeshare.end_point.models;

import java.io.Serializable;
import java.util.List;

public class ScanResultModel implements Serializable {
private String attendance_time;
        private String date;
private String departure_time;
    public String getAttendance_time() {
        return attendance_time;
    }

    public String getDate() {
        return date;
    }

    public String getDeparture_time() {
        return departure_time;
    }
}
