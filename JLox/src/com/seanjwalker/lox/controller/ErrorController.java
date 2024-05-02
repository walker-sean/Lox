package com.seanjwalker.lox.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ErrorController {
    private final OutputController output;
    boolean hadError = false;

    public ErrorController(OutputController output) {
        this.output = output;
    }

    /**
     * Handles an error
     * @param line the line where the error occurred
     * @param message the error message
     * @throws IOException if there is an error writing the error message
     */
    void error(int line, String message) throws IOException {
        report(line, "", message);
    }

    /**
     * Reports an error to the user
     * @param line the line where the error occurred
     * @param where the location of the error (user-friendly)
     * @param message the error message
     * @throws IOException if there is an error writing the error message
     */
    private void report(int line, String where,
                        String message) throws IOException {
        this.output.printError("[line " + String.valueOf(line) + "] Error" + where + ": " + message +"\n");
    }
}
