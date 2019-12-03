package com.creativeshare.end_point.models;

import java.io.Serializable;
import java.util.List;

public class ScanResultModel implements Serializable {
private String attendance_time;
        private String date;

    public String getAttendance_time() {
        return attendance_time;
    }

    public String getDate() {
        return date;
    }
}
