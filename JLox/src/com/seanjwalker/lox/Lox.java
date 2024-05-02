package com.seanjwalker.lox;

import com.seanjwalker.lox.controller.AppController;
import com.seanjwalker.lox.controller.ErrorController;
import com.seanjwalker.lox.controller.OutputController;

import java.io.IOException;

/**
 * Driver class for the Lox interpreter
 */
public class Lox {
    /**
     * Runs the interpreter
     * @param args a script to run or nothing to run REPL
     * @throws IOException if there is an error reading the script
     */
    public static void main(String[] args) throws IOException {
        OutputController outputController = new OutputController(System.out, System.err);
        ErrorController errorController = new ErrorController(outputController);
        AppController appController = new AppController(outputController, errorController);

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