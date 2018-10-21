package com.turan.streams.demo.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(com.turan.streams.demo.test.LogDataTest.class, com.turan.streams.demo.test.LogsReportTest.class);

        if (result.wasSuccessful()) {
            System.out.println("All the " + result.getRunCount()+" tests passed!");
            return;
        }
        else {
            System.out.println("ERROR :  " + result.getRunCount()+" tests failedß!");
        }

        result.getFailures()
                .stream()
                .forEach(System.out::println);
    }
}
