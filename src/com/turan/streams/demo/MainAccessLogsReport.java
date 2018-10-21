package com.turan.streams.demo;

import com.turan.streams.demo.LogsReport;


public class MainAccessLogsReport {

    public static void main(String[] args){
        try {
            LogsReport logsReport = new LogsReport(args);
            logsReport.executeReport();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
