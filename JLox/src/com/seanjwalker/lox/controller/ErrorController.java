package com.seanjwalker.lox.controller;

import java.io.IOException;

public class ErrorController {
    private final OutputController outputController;
    boolean hadError = false;

    /**
     * Constructor
     * @param outputController the output controller for the app
     */
    public ErrorController(OutputController outputController) {
        this.outputController = outputController;
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
        this.outputController.printError("[line " + line + "] Error" + where + ": " + message +"\n");
    }
}
