package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Expression;
import com.seanjwalker.lox.model.Token;
import com.seanjwalker.lox.view.ErrorReporter;
import com.seanjwalker.lox.view.OutputPrinter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controller for the Lox interpreter
 */
public class AppController {
    private final OutputPrinter outputPrinter;
    private final ErrorReporter errorReporter;

    /**
     * Constructor
     * @param outputPrinter the output controller for the app
     * @param errorReporter the error controller for the app
     */
    public AppController(OutputPrinter outputPrinter, ErrorReporter errorReporter) {
        this.outputPrinter = outputPrinter;
        this.errorReporter = errorReporter;
    }

    /**
     * Runs the given script
     * @param path the path to the script
     */
    public void runFile(String path) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            this.outputPrinter.printError(e);
        }
        assert bytes != null;
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (errorReporter.hadError) System.exit(65);
    }

    /**
     * Runs the REPL
     */
    public void runPrompt(InputStream input) {
        Reader inputReader = new InputStreamReader(input);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        while (true) {
            outputPrinter.print("> ");
            String line = null;
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                outputPrinter.printError(e.getStackTrace());
            }
            if (line == null) break;
            run(line);
            errorReporter.hadError = false;
        }
    }

    /**
     * Parses tokens from the source and runs instructions
     * @param source the source code
     */
    private void run(String source)  {
        Scanner scanner = new Scanner(source, this.errorReporter);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens, this.errorReporter);
        Expression expression = parser.parse();

        if (errorReporter.hadError) return;

        this.outputPrinter.println(new AstMaker().print(expression));
    }
}
