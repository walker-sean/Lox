package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Token;
import com.seanjwalker.lox.model.TokenType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for scanning the source code
 */
class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    private final ErrorController errorController;
    private boolean inBlockComment = false;

    public Scanner(String source, ErrorController errorController) {
        this.source = source;
        this.errorController = errorController;
    }

    /**
     * Scans all tokens in the source
     * @return the source as a list of tokens
     * @throws IOException if there is an error writing an unexpected character message
     */
    List<Token> scanTokens() throws IOException {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Adds a token to the list of tokens based on what is encountered
     * @throws IOException if there is an error writing the unexpected character message
     */
    private void scanToken() throws IOException {
        // Map each literal to its TokenType
        Map<String, TokenType> literalMap = new HashMap<>();
        for (TokenType type : TokenType.values()) {
            if (type.literal != null) {
                literalMap.put(type.literal, type);
            }
        }

        char c = advance();
        switch (c) {
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;

            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;

            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;

            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else if (match('*')) {
                    inBlockComment = true;
                    while (peek() != '*' && peekNext() != '/') {
                        if (peek() == '\n') line++;
                        if (isAtEnd()) {
                            errorController.error(line, "Unterminated block comment.");
                            return;
                        }
                        advance();
                    }
                }
                else {
                    addToken(TokenType.SLASH);
                }
                break;

            case '*':
                if (match('/')) {
                    if (inBlockComment) {
                        inBlockComment = false;
                        if (!isAtEnd()) advance();
                    } else errorController.error(line, "Unexpected block comment end");
                } else {
                    addToken(TokenType.STAR);
                }
                break;

            // Ignore whitespace
            case ' ':
            case '\r':
            case '\t':
                break;

            case '\n':
                line++;
                break;

            case '"': string(); break;

            default:
                // if the character is an accepted single-character token
                if (literalMap.containsKey(String.valueOf(c))) addToken(literalMap.get(String.valueOf(c)));
                else if (isDigit(c)) number();
                else if (isAlpha(c)) identifier();
                else errorController.error(line, "Unexpected character.");
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        try {
            TokenType type = TokenType.valueOf(text.toUpperCase());
            if (TokenType.keywords.contains(type)) addToken(type);
            else addToken(TokenType.IDENTIFIER);
        } catch (IllegalArgumentException e) {
            addToken(TokenType.IDENTIFIER);
        }
    }

    /**
     * Scans through a number literal and adds it to the list of tokens
     */
    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "." with first advance()
            do advance();
            while (isDigit(peek()));
        }

        addToken(TokenType.NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Scans through a string literal and adds it to the list of tokens
     * @throws IOException if there is an error writing an error message
     */
    private void string() throws IOException {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            errorController.error(line, "Unterminated string.");
            return;
        }

        // Advances past the closing quote
        advance();

        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    /**
     * Determines if the current character matches the expected character
     * @param expected the character to match
     * @return true if the characters match, false otherwise
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Returns the current character without advancing the index
     * @return the character at the current index
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Returns the next character without advancing the index
     * @return the character at the next index
     */
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /**
     * Determines if a character is an alphabetic character (a-z, A-Z, or _)
     * @param c the character to check
     * @return true if the character is alphabetic, false otherwise
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    /**
     * Determines if a character is alphanumeric
     * @param c the character to check
     * @return true if the character is alphanumeric, false otherwise
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Determines if a character is a digit
     * @param c the character to check
     * @return true if the character is a digit, false otherwise
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Determines if all characters have been consumed
     * @return true if all characters have been consumed, false otherwise
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Returns the current character and advances the index
     * @return the character at the current index
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Adds a token to the list of tokens with a null literal
     * @param type the TokenType of the token
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Adds a token to the list of tokens
     * @param type the TokenType of the token
     * @param literal the literal expression of the token
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}

