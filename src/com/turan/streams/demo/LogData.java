package com.turan.streams.demo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LogData{

    private LocalTime localTime;
    private String user;
    private String action;
    private boolean matched;

    public LogData() {
    }

    public LogData(LocalTime localTime, String user, String action) {
        this.localTime = localTime;
        this.user = user;
        this.action = action;
        this.matched = false;
    }

    public static boolean isLineValid(String[] strings) {
        if(strings.length != 3){
            return false;
        }

        try {
            LocalTime.parse(strings[0], DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (DateTimeParseException e) {
            return false;
        }

        if(!strings[1].matches("^[a-zA-Z0-9]*$")){
            return false;
        }

        if(!strings[2].matches("Start|End")){
            return false;
        }

        return true;
    }

    public LocalTime getLocalTime() {
        return localTime;
    }

    public void setLocalTime(LocalTime localTime) {
        this.localTime = localTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    @Override
    public String toString() {
        return "LogData{" +
                "localTime=" + localTime +
                ", user='" + user + '\'' +
                ", action='" + action + '\'' +
                ", matched=" + matched +
                '}';
    }

}
