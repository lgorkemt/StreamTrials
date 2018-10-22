package com.turan.streams.demo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

import com.turan.streams.demo.ReportData;
import com.turan.streams.demo.LogData;

public class LogsReport {

    private LocalTime startTime;
    private LocalTime endTime;
    private String inputFileName = "";
    private String outputFileName = "Resources/report.txt";

    public LogsReport(String[] args) throws FileNotFoundException, IOException {
        // read the file name from the first parameter
        if(args.length > 0) {
            inputFileName = args[0];
        }
        else  {
            inputFileName = "Resources/logs.log";
        }

        File f = new File(inputFileName);
        if(!f.exists() || f.isDirectory()) {
            throw new FileNotFoundException();
        }

        if(f.length() == 0){
            throw new IOException("File is empty!");
        }

        createEmptyFile(outputFileName);
    }

    public LogsReport() {

    }

    // Reads the log file and generates the output
    public void executeReport() throws IOException {
        // Read the lines of the input file into a stream
        Stream<String> lines = Files.lines(Paths.get(inputFileName));

        // Parse the stream of lines with filtering only correct data into a List
        List<LogData> logs = parse(lines);

        // Genarete the output data from the logs and store into a List
        List<ReportData> reportDataList = calculateLogs(logs);

        // Write the output data into a report and to the console
        reportDataList.stream()
                .forEach(System.out::println);

        FileWriter fw = new FileWriter(outputFileName);
        reportDataList.stream()
                .forEach(row -> writeToFile(fw, String.valueOf(row)));
        fw.close();

        lines.close();
    }

    // Parses the log file, filters the corrupted data and returns a list object of valid logs
    public List<LogData> parse(Stream<String> lines) throws IOException {
        List<LogData> logs = lines.map(line -> line.split(" "))
                                    .filter(LogData::isLineValid)
                                    .map(a -> new LogData(LocalTime.parse(a[0]), a[1], a[2]))
                                    .collect(Collectors.toList());

        startTime = logs.get(0).getLocalTime();
        endTime = logs.get(logs.size()-1).getLocalTime();
        return logs;
    }

    // Calculates the session count and total seconds per user and returns the values in a list
    public List<ReportData> calculateLogs(List<LogData> logs){
        List<ReportData> reportDataList = new ArrayList<ReportData>();
        // group the log records in a map by user
        Map<String, List<LogData>> logsGrouped = logs.stream()
                                                        .collect(Collectors.groupingBy(l -> l.getUser()));
        // iterate the log values in the map for each user
        logsGrouped.forEach((user,logList)->{
            ReportData reportData = new ReportData(user,0,0);
            // calculate the duration between start - end pairs by handling the cases where a record does not have a matching start or end
            logList.forEach(item->{
                Duration duration  = getMatchingLogDataLocalTime(item, logList);;
                if(duration!=null) {
                    reportData.incrementSessionAndTotalSeconds(duration);
                }
            });
            // create the user report object which will be displayed as a row in the output file
            reportDataList.add(reportData);
        });
        reportDataList.sort(Comparator.comparing(ReportData::getUser));
        return reportDataList;
    }

    // Gets the matching record and returns the duration value of the session
    public Duration getMatchingLogDataLocalTime(LogData logData, List<LogData> logDataList) {
        Duration duration = null;
        int indexOf = logDataList.indexOf(logData);

        if(!logData.isMatched()){
            if("End".equals(logData.getAction())){
                duration =  Duration.between(startTime, logData.getLocalTime());
            }
            else {
                for(int i =indexOf+1; i<logDataList.size();i++){
                    LogData nextLog = logDataList.get(i);
                    if(!nextLog.isMatched() && "End".equals(nextLog.getAction())) {
                        nextLog.setMatched(true);
                        logData.setMatched(true);
                        duration = Duration.between(logData.getLocalTime(), nextLog.getLocalTime());
                        break;
                    }
                }
                if(!logData.isMatched()) {
                    duration = Duration.between(logData.getLocalTime(), endTime);
                    logData.setMatched(true);
                }
            }
        }
        return duration;
    }

    private void writeToFile(FileWriter fw, String row) {
        try {
            fw.write(String.format("%s%n", row));
        } catch (IOException e) {
            System.out.println("Exception in writing to a file : " + e.getMessage());
        }
    }

    private void createEmptyFile(String fileName){
        try {
            deleteFile(fileName);
            Files.write(Paths.get(fileName), new byte[0], StandardOpenOption.CREATE);
        }
        catch (IOException e){
            System.out.println("Exception in creating file " + e.getMessage());
        }
    }

    private void deleteFile(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
    }

    public String getInputFileName() {
        return inputFileName;
    }

    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

}
