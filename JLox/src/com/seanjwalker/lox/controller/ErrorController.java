package com.seanjwalker.lox.controller;

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
     */
    void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * Reports an error to the user
     * @param line the line where the error occurred
     * @param where the location of the error (user-friendly)
     * @param message the error message
     */
    void report(int line, String where,
                String message) {
        this.outputController.printError("[line " + line + "] Error" + where + ": " + message +"\n");
    }
}
