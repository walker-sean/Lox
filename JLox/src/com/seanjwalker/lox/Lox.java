package com.seanjwalker.lox;

import com.seanjwalker.lox.controller.AppController;
import com.seanjwalker.lox.controller.ErrorController;
import com.seanjwalker.lox.controller.OutputController;

/**
 * Driver class for the Lox interpreter
 */
public class Lox {
    /**
     * Runs the interpreter
     * @param args a script to run or nothing to run REPL
     */
    public static void main(String[] args) {
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