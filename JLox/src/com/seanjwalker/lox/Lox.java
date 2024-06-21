package com.seanjwalker.lox;

import com.seanjwalker.lox.controller.AppController;
import com.seanjwalker.lox.view.ErrorReporter;
import com.seanjwalker.lox.view.OutputPrinter;

/**
 * Driver class for the Lox interpreter
 */
public class Lox {
    /**
     * Runs the interpreter
     * @param args a script to run or nothing to run REPL
     */
    public static void main(String[] args) {
        OutputPrinter outputPrinter = new OutputPrinter(System.out, System.err);
        ErrorReporter errorReporter = new ErrorReporter(outputPrinter);
        AppController appController = new AppController(outputPrinter, errorReporter);

        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            appController.runFile(args[0]);
        } else {
            appController.runPrompt(System.in);
        }
    }
}