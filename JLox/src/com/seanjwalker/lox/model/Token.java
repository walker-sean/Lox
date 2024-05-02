package com.seanjwalker.lox.model;

/**
 * Represents a token in the Lox language
 */
public class Token {
    private final TokenType type;
    private final String lexeme;
    private final Object literal;
    private final int line;

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
