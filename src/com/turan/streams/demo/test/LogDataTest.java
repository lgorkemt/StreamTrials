package com.turan.streams.demo.test;

import static org.junit.Assert.*;
import com.turan.streams.demo.LogData;
import org.junit.Test;


public class LogDataTest {

    @Test
    public void test_is_valid_line_returns_false()
    {
        assertFalse("wrong name format", LogData.isLineValid("14:02:03 GEORGE HAGI Start".split( " ")));
        assertFalse("wrong time format", LogData.isLineValid("14.02.03 GEORGEHAGI Start".split( " ")));
        assertFalse("not alpha-numeric name format", LogData.isLineValid("14.02.03 GEORGEHAGI! Start".split( " ")));
        assertFalse("wrong Start/End value",LogData.isLineValid("14:02:03 GEORGEHAGI Starts".split( " ")));
        assertFalse("missing one parameter", LogData.isLineValid(("14:02:03 GEORGEHAGI").split( " ")));
        assertFalse("missing two parameters", LogData.isLineValid("14:02:03".split( " ")));
        assertFalse("blank line", LogData.isLineValid("".split( " ")));
    }

    @Test
    public void test_is_valid_line_returns_true() {
        assertTrue("Error parsing line", LogData.isLineValid("14:02:03 GEORGEHAGI Start".split(" ")));
        assertTrue("Error parsing line", LogData.isLineValid("14:02:03 GEORGEHAGI End".split(" ")));
    }
}