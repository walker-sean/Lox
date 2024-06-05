package com.seanjwalker.lox.controller;

import java.io.IOException;

/**
 * Controller for handling output
 */
public class OutputController {
    private final Appendable output;
    private final Appendable errorOutput;

    /**
     * Constructor
     * @param output the standard output location
     * @param errorOutput the error output location
     */
    public OutputController(Appendable output, Appendable errorOutput) {
        this.output = output;
        this.errorOutput = errorOutput;
    }

    /**
     * Prints the String representation of a value to output
     * @param value the value to print
     * @throws IOException if there is an error writing the message
     */
    public void print(Object value) throws IOException {
        output.append(value.toString());
    }

    /**
     * Prints the String representation of a value to output as a line
     * @param value the value to print
     * @throws IOException if there is an error writing the message
     */
    public void println(Object value) throws IOException {
        output.append(value.toString()).append("\n");
    }

    /**
     * Prints the String representation of a value to error output
     * @param value the value to print
     * @throws IOException if there is an error writing the message
     */
    public void printError(Object value) throws IOException {
        errorOutput.append(value.toString()).append("\n");
    }
}
