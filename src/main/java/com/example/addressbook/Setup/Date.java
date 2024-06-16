package com.example.addressbook.schedule.Setup;
public class Date {
    private String day;
    private int hour;
    private String purpose;


    public Date(String day, int hour, String purpose) {
        this.day = day;
        this.hour = hour;
        this.purpose = purpose;
    }

    public String getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public String getPurpose() {
        return purpose;
    }

}


