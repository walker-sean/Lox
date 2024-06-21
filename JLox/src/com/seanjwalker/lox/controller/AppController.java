package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Expression;
import com.seanjwalker.lox.model.Token;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Controller for the Lox interpreter
 */
public class AppController {
    private final OutputController outputController;
    private final ErrorController errorController;

    /**
     * Constructor
     * @param outputController the output controller for the app
     * @param errorController the error controller for the app
     */
    public AppController(OutputController outputController, ErrorController errorController) {
        this.outputController = outputController;
        this.errorController = errorController;
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
            this.outputController.printError(e);
        }
        assert bytes != null;
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (errorController.hadError) System.exit(65);
    }

    /**
     * Runs the REPL
     */
    public void runPrompt(InputStream input) {
        Reader inputReader = new InputStreamReader(input);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        while (true) {
            outputController.print("> ");
            String line = null;
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                outputController.printError(e.getStackTrace());
            }
            if (line == null) break;
            run(line);
            errorController.hadError = false;
        }
    }

    /**
     * Parses tokens from the source and runs instructions
     * @param source the source code
     */
    private void run(String source)  {
        Scanner scanner = new Scanner(source, this.errorController);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens, this.errorController);
        Expression expression = parser.parse();

        if (errorController.hadError) return;

        this.outputController.println(new AstPrinter().print(expression));
    }
}
