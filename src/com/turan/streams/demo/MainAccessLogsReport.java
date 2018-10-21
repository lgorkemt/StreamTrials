package com.turan.streams.demo;


public class MainAccessLogsReport {

    public static void main(String[] args){
        try {
            com.turan.streams.demo.LogsReport logsReport = new com.turan.streams.demo.LogsReport(args);
            logsReport.executeReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
