package com.turan.streams.demo.test;

import static org.junit.Assert.*;
import com.turan.streams.demo.LogData;
import com.turan.streams.demo.LogsReport;
import com.turan.streams.demo.ReportData;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.util.stream.Stream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class LogsReportTest {

    Stream<String> lines ;
    private static final String TEST_LOGS_NAME = "Resources/test_logs.log";
    private static final String TEST_REPORT_NAME = "Resources/test_report.txt";
    LogsReport logsReport;

    @Before
    public void setUp() throws Exception {
        logsReport = new LogsReport();
        logsReport.setInputFileName(TEST_LOGS_NAME);
        logsReport.setOutputFileName(TEST_REPORT_NAME);
        createEmptyFile(TEST_LOGS_NAME);
        createEmptyFile(TEST_REPORT_NAME);
    }

    // unit test of LogsReport->parse method
    @Test
    public void test_parse_method() throws IOException {
        String[] lines = {
                "14:02:59 ALICE99 End",
                "14:03:00 ALICE99 Start",
                "14:03:02 CHARLIE End",
                "14:03:03 BECKY Start",
                "14:03:04 CHARLIE Start",
                "14:03:33 ALICE99 End"
        };

        List<LogData> expectedLogDataList = new ArrayList<LogData>();
        expectedLogDataList.add(new LogData(LocalTime.parse("14:02:59"), "ALICE99","End"));
        expectedLogDataList.add(new LogData(LocalTime.parse("14:03:00"), "ALICE99","Start"));
        expectedLogDataList.add(new LogData(LocalTime.parse("14:03:02"), "CHARLIE","End"));
        expectedLogDataList.add(new LogData(LocalTime.parse("14:03:03"), "BECKY","Start"));
        expectedLogDataList.add(new LogData(LocalTime.parse("14:03:04"), "CHARLIE","Start"));
        expectedLogDataList.add(new LogData(LocalTime.parse("14:03:33"), "ALICE99","End"));

        writeToFile(TEST_LOGS_NAME, lines);
        List<LogData> actualLogDataList =  logsReport.parse(Files.lines(Paths.get(TEST_LOGS_NAME)));
        int i =0;

        for(LogData logData : expectedLogDataList){
            assertEquals(logData.toString(), actualLogDataList.get(i++).toString());
        }
        assertEquals(logsReport.getStartTime(), LocalTime.parse("14:02:59"));
        assertEquals(logsReport.getEndTime(), LocalTime.parse("14:03:33"));
        assertEquals(6, actualLogDataList.size());
    }

    // unit test of LogsReport->calculateLogs method
    @Test
    public void test_calculate_logs_method() throws IOException {
        String[] lines = {
                "14:02:59 ALICE99 End",
                "14:03:00 ALICE99 Start",
                "14:03:02 CHARLIE End",
                "14:03:03 BECKY Start",
                "14:03:04 CHARLIE Start",
                "14:03:33 ALICE99 End"
        };

        List<ReportData> expectedReportList = new ArrayList<ReportData>();
        expectedReportList.add(new ReportData("ALICE99", 2, 33));
        expectedReportList.add(new ReportData("BECKY", 1, 30));
        expectedReportList.add(new ReportData("CHARLIE", 2, 32));

        writeToFile(TEST_LOGS_NAME, lines);

        List<LogData> actualLogDataList =  logsReport.parse(Files.lines(Paths.get(TEST_LOGS_NAME)));
        List<ReportData> actualReportList = logsReport.calculateLogs(actualLogDataList);
        int i =0;

        for(ReportData reportData : expectedReportList){
            assertEquals(reportData.toString(), actualReportList.get(i++).toString());
        }
        assertEquals(3, actualReportList.size());
    }

    // testing execute method (the whole process)
    @Test
    public void test_execute_method_random_file() throws IOException {
        // generate an input file
        String[] lines = {
                "14:02:59 ALICE99 End",
                "14:03:00 ALICE99 Start",
                "14:03:19 ALICE99 End",
                "14:03:02 CHARLIE End",
                "14:03:03 BECKY Start",
                "14:03:13 BECKY Start",
                "14:03:04 CHARLIE Start",
                "14:03:33 ALICE99 Start"
        };
        System.out.println("test_execute_method_random_file output :" );
        setUpFiles(lines);

        List<String> expectedReportOutput = new ArrayList<String>();
        expectedReportOutput.add("ALICE99 3 19");
        expectedReportOutput.add("BECKY 2 50");
        expectedReportOutput.add("CHARLIE 2 32");

        Stream<String> streamLines = Files.lines(Paths.get(TEST_REPORT_NAME));
        int[] iarr = {0};
        streamLines.forEach(s->{
            assertEquals(expectedReportOutput.get(iarr[0]++), s);
        });
    }

    // testing execute method (the whole process)
    @Test
    public void test_execute_method_standart_file() throws IOException {
        // generate an input file
        String[] lines = {
                "14:02:03 ALICE99 Start",
                "14:02:05 CHARLIE End",
                "14:02:34 ALICE99 End",
                "14:02:58 ALICE99 Start",
                "14:03:02 CHARLIE Start",
                "14:03:33 ALICE99 Start",
                "14:03:35 ALICE99 End",
                "14:03:37 CHARLIE End",
                "14:04:05 ALICE99 End",
                "14:04:23 ALICE99 End",
                "14:04:41 CHARLIE Start"
        };
        System.out.println("test_execute_method_standart_file output :" );
        setUpFiles(lines);

        List<String> expectedReportOutput = new ArrayList<String>();
        expectedReportOutput.add("ALICE99 4 240");
        expectedReportOutput.add("CHARLIE 3 37");

        Stream<String> streamLines = Files.lines(Paths.get(TEST_REPORT_NAME));
        int[] iarr = {0};
        streamLines.forEach(s->{
            assertEquals(expectedReportOutput.get(iarr[0]++), s);
        });
    }

    // testing execute method (the whole process)
    @Test
    public void test_execute_method_with_only_start_records() throws IOException {
        // generate an input file
        String[] lines = {
                "14:03:00 ALICE99 Start",
                "14:03:05 ALICE99 Start",
                "14:03:10 CHARLIE Start",
                "14:03:18 BECKY Start",
                "14:03:29 CHARLIE Start",
                "14:04:00 ALICE99 Start"
        };
        System.out.println("test_execute_method_with_only_start_records output :" );
        setUpFiles(lines);

        List<String> expectedReportOutput = new ArrayList<String>();
        expectedReportOutput.add("ALICE99 3 115");
        expectedReportOutput.add("BECKY 1 42");
        expectedReportOutput.add("CHARLIE 2 81");

        Stream<String> streamLines = Files.lines(Paths.get(TEST_REPORT_NAME));

        int[] iarr = {0};
        streamLines.forEach(s->{
            assertEquals(expectedReportOutput.get(iarr[0]++), s);
        });
    }

    // testing execute method (the whole process)
    @Test
    public void test_execute_method_with_only_end_records() throws IOException {
        // generate an input file
        String[] lines = {
                "14:03:00 ALICE99 End",
                "14:03:05 ALICE99 End",
                "14:03:10 CHARLIE End",
                "14:03:18 BECKY End",
                "14:03:29 CHARLIE End",
                "14:04:00 ALICE99 End"
        };
        System.out.println("test_execute_method_with_only_end_records output :" );
        setUpFiles(lines);

        List<String> expectedReportOutput = new ArrayList<String>();
        expectedReportOutput.add("ALICE99 3 65");
        expectedReportOutput.add("BECKY 1 18");
        expectedReportOutput.add("CHARLIE 2 39");

        Stream<String> streamLines = Files.lines(Paths.get(TEST_REPORT_NAME));
        int[] iarr = {0};
        streamLines.forEach(s->{
            assertEquals(expectedReportOutput.get(iarr[0]++), s);
        });
    }

    @Test (expected= FileNotFoundException.class)
    public void test_if_file_not_found_throws_exception() throws IOException
    {
        String[] args = new String[1];
        args[0] = "Resources/non_existing_file.log";
        LogsReport logsReport = new LogsReport(args);
    }

    @Test (expected= IOException.class)
    public void test_if_file_is_empty_throws_exception() throws IOException
    {
        createEmptyFile(TEST_LOGS_NAME);
        String[] args = new String[1];
        args[0] = "TEST_LOGS_NAME";
        LogsReport logsReport = new LogsReport(args);
    }

    private void setUpFiles(String[] lines) throws IOException {
        writeToFile(TEST_LOGS_NAME, lines);
        logsReport.executeReport();
    }

    private void writeToFile(String fileName, String[] lines) throws IOException {
        Files.write(Paths.get(fileName), Arrays.asList(lines), Charset.defaultCharset(), StandardOpenOption.CREATE);
    }

    private void createEmptyFile(String fileName) throws IOException{
        deleteFile(fileName);
        Files.write(Paths.get(fileName), new byte[0], StandardOpenOption.CREATE);
    }

    private void deleteFile(String fileName) throws IOException {
        Files.deleteIfExists(Paths.get(fileName));
    }
}
