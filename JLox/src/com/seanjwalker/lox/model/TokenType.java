package com.seanjwalker.lox.model;

import java.util.*;

/**
 * Represents the types of tokens in the Lox language
 */
public enum TokenType {
    // Single-character tokens
    LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACE("{"), RIGHT_BRACE("}"),
    COMMA(","), DOT("."), MINUS("-"), PLUS("+"), SEMICOLON(";"), SLASH("/"),
    STAR("*"),

    // One or two character tokens
    BANG("!"), BANG_EQUAL("!="),
    EQUAL("="), EQUAL_EQUAL("=="),
    GREATER(">"), GREATER_EQUAL(">="),
    LESS("<"), LESS_EQUAL("<="),

    // Literals
    IDENTIFIER, STRING, NUMBER,

    // Keywords
    AND("and"), CLASS("class"), ELSE("else"), FALSE("false"), FUN("fun"),
    FOR("for"), IF("if"), NIL("nil"), OR("or"),
    PRINT("print"), RETURN("return"), SUPER("super"), THIS("this"), TRUE("true"),
    VAR("var"), WHILE("while"),

    // End of file
    EOF;

    public String literal;

    // Special TokenTypes that are used as keywords
    public static final Set<TokenType> keywords;

    // Keywords that mark the beginning of a statement
    public static final Set<TokenType> statementStarters;

    public static final Map<String, TokenType> literalMap;

    static {
        keywords = new HashSet<>();
        keywords.addAll(List.of(AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
                PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE));

        statementStarters = new HashSet<>();
        statementStarters.addAll(List.of(CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN));

        literalMap = new HashMap<>();
        for (TokenType type : values()) {
            literalMap.put(type.literal, type);
        }
    }

    TokenType(String literal) {
        this.literal = literal;
    }

    TokenType() {}

    public static TokenType of(String literal) {
        return literalMap.get(literal);
    }
}
