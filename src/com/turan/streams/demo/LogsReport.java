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

        // Write the output data into a report
        FileWriter fw = new FileWriter(outputFileName);
        reportDataList.stream()
                        .forEach(row-> writeToFile(fw, String.valueOf(row)));
        fw.close();

        reportDataList.stream()
                .forEach(System.out::println);

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
        //logs.sort(Comparator.comparing(LogData::getUser).thenComparing(LogData::getLocalTime));
        //startTime = Collections.min(logs, Comparator.comparing(s -> s.getLocalTime())).getLocalTime();
        //endTime = Collections.max(logs, Comparator.comparing(s -> s.getLocalTime())).getLocalTime();
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
                Duration duration  = null;
                if("End".equals(((LogData)item).getAction())){
                    LocalTime previousLogDataLocalTime = getPreviousLogDataLocalTime(logList, logList.indexOf(item));
                    duration = Duration.between(previousLogDataLocalTime, item.getLocalTime());
                }
                else if(logList.indexOf(item) == logList.size() -1 && "Start".equals(((LogData)item).getAction())){
                    duration = Duration.between(item.getLocalTime(), endTime);
                }

                if(duration!=null) {
                    reportData.incrementSessionAndTotalSeconds(duration);
                    item.setMatched(true);
                }
            });

            // create the user report object which will be displayed as a row in the output file
            reportDataList.add(reportData);
        });
        reportDataList.sort(Comparator.comparing(ReportData::getUser));
        return reportDataList;
    }

    // Gets the matching start record for an end record and returns the time value of that start record
    public LocalTime getPreviousLogDataLocalTime(List<LogData> v, int indexOf) {
        LocalTime previousLogLocalTime = startTime;
        for(int i = indexOf-1; i>=0; i--){
            LogData logData = v.get(i);
            if("Start".equals(logData.getAction()) && !logData.isMatched()) {
                logData.setMatched(true);
                previousLogLocalTime = logData.getLocalTime();
                break;
            }
        }
        return previousLogLocalTime;
    }

    private void writeToFile(FileWriter fw, String row) {
        try {
            fw.write(String.format("%s%n", row));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createEmptyFile(String fileName) throws IOException {
        deleteFile(fileName);
        Files.write(Paths.get(fileName), new byte[0], StandardOpenOption.CREATE);
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
