package com.example.gravador_de_chamadas;

public class CallLogItem {
    private String number;
    private String type;
    private String date;
    private String time;
    private String duration;

    public CallLogItem(String number, String type, String date, String time, String duration) {
        this.number = number;
        this.type = type;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getNumber() { return number; }
    public String getType() { return type; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDuration() { return duration; }
}

