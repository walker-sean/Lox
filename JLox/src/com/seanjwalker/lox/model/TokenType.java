package com.seanjwalker.lox.model;

import java.util.HashSet;
import java.util.Set;

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

    public static Set<TokenType> keywords;

    static {
        keywords = new HashSet<>();

        keywords.add(AND);
        keywords.add(CLASS);
        keywords.add(ELSE);
        keywords.add(FALSE);
        keywords.add(FUN);
        keywords.add(FOR);
        keywords.add(IF);
        keywords.add(NIL);
        keywords.add(OR);
        keywords.add(PRINT);
        keywords.add(RETURN);
        keywords.add(SUPER);
        keywords.add(THIS);
        keywords.add(TRUE);
        keywords.add(VAR);
        keywords.add(WHILE);
    }

    TokenType(String literal) {
        this.literal = literal;
    }

    TokenType() {}
}
