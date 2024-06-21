package com.seanjwalker.lox.view;

public class ErrorReporter {
    private final OutputPrinter outputPrinter;
    public boolean hadError = false;

    /**
     * Constructor
     * @param outputPrinter the output controller for the app
     */
    public ErrorReporter(OutputPrinter outputPrinter) {
        this.outputPrinter = outputPrinter;
    }

    /**
     * Handles an error
     * @param line the line where the error occurred
     * @param message the error message
     */
    public void error(int line, String message) {
        report(line, "", message);
    }

    /**
     * Reports an error to the user
     * @param line the line where the error occurred
     * @param where the location of the error (user-friendly)
     * @param message the error message
     */
    public void report(int line, String where,
                       String message) {
        this.outputPrinter.printError("[line " + line + "] Error" + where + ": " + message +"\n");
    }
}
