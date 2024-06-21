package com.seanjwalker.lox.view;

import java.io.IOException;

/**
 * Controller for handling output
 */
public class OutputPrinter {
    private final Appendable output;
    private final Appendable errorOutput;

    /**
     * Constructor
     * @param output the standard output location
     * @param errorOutput the error output location
     */
    public OutputPrinter(Appendable output, Appendable errorOutput) {
        this.output = output;
        this.errorOutput = errorOutput;
    }

    /**
     * Prints the String representation of a value to output
     * @param value the value to print
     */
    public void print(Object value) {
        try {
            output.append(value.toString());
        } catch (IOException e) {
            throw new RuntimeException("Standard output cannot be written to.", e);
        }
    }

    /**
     * Prints the String representation of a value to output as a line
     * @param value the value to print
     */
    public void println(Object value) {
        try {
            output.append(value.toString()).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("Standard output cannot be written to.", e);
        }
    }

    /**
     * Prints the String representation of a value to error output
     * @param value the value to print
     */
    public void printError(Object value) {
        try {
            errorOutput.append(value.toString()).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("Error output cannot be written to.", e);
        }
    }
}
