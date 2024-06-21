package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Expression;
import com.seanjwalker.lox.model.Token;
import com.seanjwalker.lox.model.TokenType;
import com.seanjwalker.lox.view.ErrorReporter;

import java.util.List;

/**
 * Controller for parsing through tokens.
 * Most methods represents a distinct rule in the expression grammar
 */
class Parser {
    /**
     * A utility class for an error that appears during parsing
     */
    private static class ParseError extends RuntimeException {
        /**
         * Constructor.
         * Reports the error as it is constructed
         * @param errorReporter the Error Controller used to report the error
         * @param token the token where the error occurs
         * @param message the message to serve the user regarding the message
         */
        private ParseError(ErrorReporter errorReporter, Token token, String message) {
            super(message);

            if (token.type == TokenType.EOF) {
                errorReporter.report(token.line, " at end", message);
            } else {
                errorReporter.report(token.line, " at '" + token.lexeme + "'", message);
            }
        }
    }

    private final List<Token> tokens;
    private int current = 0;
    private final ErrorReporter errorReporter;

    /**
     * Constructor
     * @param tokens the tokens consumed by the parser
     */
    Parser(List<Token> tokens, ErrorReporter errorReporter) {
        this.tokens = tokens;
        this.errorReporter = errorReporter;
    }

    /**
     * Parses the input tokens as an expression
     * @return the syntax tree representing the input
     */
    Expression parse() {
        try {
            return expression();
        } catch (ParseError e) {
            return null;
        }
    }


    /**
     * Parses an expression.
     * The expression rule is simply an expansion of the equality rule
     * @return the syntax tree for the expression
     */
    private Expression expression() {
        return equality();
    }

    /**
     * Parses an equality
     * @return the syntax tree for the equality
     */
    private Expression equality() {
        Expression expression = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    /**
     * Parses a comparison
     * @return the syntax tree for the comparison
     */
    private Expression comparison() {
        Expression expression = term();

        while (match(TokenType.GREATER,TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    /**
     * Parses an addition or subtraction
     * @return the syntax tree for the addition or subtraction
     */
    private Expression term() {
        Expression expression = factor();

        while (match(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    /**
     * Parses a multiplication or division
     * @return the syntax tree for the multiplication or division
     */
    private Expression factor() {
        Expression expression = unary();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            Token operator = previous();
            Expression right = unary();
            expression = new Expression.Binary(expression, operator, right);
        }

        return expression;
    }

    /**
     * Parses a unary expression (possibly nested)
     * @return the syntax tree for the expression
     */
    private Expression unary() {
        if (match(TokenType.MINUS, TokenType.BANG)) {
            Token operator = previous();
            Expression right = unary();
            return new Expression.Unary(operator, right);
        }

        return primary();
    }

    /**
     * Parses literals and groupings
     * @return the literal or expression of the grouping
     */
    private Expression primary() {
        if (match(TokenType.FALSE)) return new Expression.Literal(TokenType.FALSE);
        if (match(TokenType.TRUE)) return new Expression.Literal(TokenType.TRUE);
        if (match(TokenType.NIL)) return new Expression.Literal(TokenType.NIL);

        if (match(TokenType.NUMBER, TokenType.STRING)) return new Expression.Literal(previous().literal);

        if (match(TokenType.LEFT_PAREN)) {
            Expression expression = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Expression.Grouping(expression);
        }

        throw new ParseError(this.errorReporter, peek(), "Expect expression.");
    }

    /**
     * Checks if the current token has any of the given types.
     * If so, the token is consumed
     * @param types the types to check the token against
     * @return whether the token has one of the types
     */
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the current token has the given type
     * @param type the type to check the token against
     * @return whether the token is the given type
     */
    private boolean check(TokenType type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    /**
     * Consumes the current token and returns it.
     * Once all tokens are consumed, this will repeatedly return the last token
     * @return the current token
     */
    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    /**
     * Determines whether all tokens have been consumed.
     * This is determined through the notion that the final token should have type EOF
     * @return whether the current token has type EOF
     */
    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    /**
     * Retrieves the next token to be consumed
     * @return the current token
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Retrieves the token most recently consumed
     * @return the previous token
     */
    private Token previous() {
        return tokens.get(current - 1);
    }


    /**
     * Checks the current token against a type while advancing
     * @param type the TokenType to check the token against
     * @param message the error message to use if the token doesn't have the provided type
     * @return the current token, if consumed
     * @throws ParseError if the types do not match
     */
    private Token consume(TokenType type, String message) {
        if (check(type)) return advance();

        throw new ParseError(errorReporter, peek(), message);
    }

    /**
     * On the occasion of an error, advances until the token stream can be parsed again,
     * synchronizing the token stream
     */
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == TokenType.SEMICOLON) return;

            if (TokenType.statementStarters.contains(peek().type)) return;

            advance();
        }
    }
}
