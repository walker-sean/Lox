package com.seanjwalker.lox.model;

/**
 * Represents a token in the Lox language
 */
public class Token {
    private final TokenType type;
    public final String lexeme;
    private final Object literal;
    private final int line;

    /**
     * Constructor
     * @param type the TokenType of the token
     * @param lexeme the String representing how the token is presented in Lox code
     * @param literal the value of the token
     * @param line where the token is located
     */
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Provides a string representation of the token
     * @return the token as a string
     */
    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
