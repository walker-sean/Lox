package com.seanjwalker.lox.controller;

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
    private final OutputController output;
    private final ErrorController errorController;

    public AppController(OutputController output, ErrorController errorController) {
        this.output = output;
        this.errorController = errorController;
    }

    /**
     * Runs the given script
     * @param path the path to the script
     * @throws IOException if there is an error reading the script
     */
    public void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (errorController.hadError) System.exit(65);
    }

    /**
     * Runs the REPL
     * @throws IOException if there is an error reading the input
     */
    public void runPrompt(InputStream input) throws IOException {
        Reader inputReader = new InputStreamReader(input);
        BufferedReader bufferedReader = new BufferedReader(inputReader);

        while (true) {
            output.print("> ");
            String line = bufferedReader.readLine();
            if (line == null) break;
            run(line);
            errorController.hadError = false;
        }
    }

    /**
     * Parses tokens from the source and runs instructions
     * @param source the source code
     * @throws IOException if there is an error writing a message
     */
    private void run(String source) throws IOException {
        Scanner scanner = new Scanner(source, this.errorController);
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens.
        for (Token token : tokens) {
            this.output.println(token);
        }
    }
}
