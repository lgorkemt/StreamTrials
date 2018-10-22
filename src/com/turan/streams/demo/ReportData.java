package com.turan.streams.demo;

import java.time.Duration;

public class ReportData {

    private String user;
    private int numberOfSessions;
    private long totalSeconds;

    public ReportData() {
    }

    public ReportData(String user, int numberOfSessions, long totalSeconds) {
        this.user = user;
        this.numberOfSessions = numberOfSessions;
        this.totalSeconds = totalSeconds;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public long getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(long totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    @Override
    public String toString() {
        return  user + ' ' +  numberOfSessions  + ' ' + totalSeconds;
    }

    public void incrementSessionAndTotalSeconds(Duration duration) {
        this.totalSeconds +=  duration.getSeconds();
        this.numberOfSessions += 1;
    }

}
