package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Expression;

public class Interpreter implements Expression.Visitor<Object> {
    /**
     * Evaluates a literal expression via the Visitor Pattern.
     * This is the same as the value of the literal
     * @param expression the literal expression visited
     * @return the evaluation of the literal
     */
    @Override
    public Object visit(Expression.Literal expression) {
        return expression.value;
    }

    /**
     * Evaluates a grouping expression via the Visitor Pattern.
     * This is the same as the evaluation of its inner expression
     * @param expression the grouping expression visited
     * @return the evaluation of the expression
     */
    @Override
    public Object visit(Expression.Grouping expression) {
        return evaluate(expression.expression);
    }

    /**
     * Evaluates a unary expression via the Visitor Pattern
     * @param expression the unary expression visited
     * @return the evaluation of the expression
     */
    @Override
    public Object visit(Expression.Unary expression) {
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case MINUS -> {
                return -(double)right;
            }
            case BANG -> {
                return !isTruthy(right);
            }
        }

        throw new IllegalStateException("Unary exception must be made using the - or ! operator");
    }

    /**
     * Evaluates a binary expression via the Visitor Pattern
     * @param expression the binary expression visited
     * @return the evaluation of the expression
     */
    @Override
    public Object visit(Expression.Binary expression) {
        Object left = evaluate(expression.left);
        Object right = evaluate(expression.right);

        switch (expression.operator.type) {
            case MINUS -> {
                return (double)left - (double)right;
            }
            case PLUS -> {
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }

                if (left instanceof String && right instanceof String) {
                    return left + (String)right;
                }
            }
            case SLASH -> {
                return (double)left / (double)right;
            }
            case STAR -> {
                return (double)left * (double)right;
            }
            case GREATER -> {
                return (double)left > (double)right;
            }
            case GREATER_EQUAL -> {
                return (double)left >= (double)right;
            }
            case LESS -> {
                return (double)left < (double)right;
            }
            case LESS_EQUAL -> {
                return (double)left <= (double)right;
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right);
            }
        }

        throw new IllegalStateException(expression.operator.lexeme
                + " is not a valid operator for a binary expression");
    }

    /**
     * Determines whether an object is truthy in Lox
     * @param object the object to evaluate
     * @return true if the object is truthy, false if it's falsey
     */
    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean)object;
        return true;
    }

    /**
     * Determines whether two objects are equal or both null
     * @param object1 the first object to compare
     * @param object2 the second object to compare
     * @return if the objects are equal or both null
     */
    private boolean isEqual(Object object1, Object object2) {
        if (object1 == null && object2 == null) return true;
        if (object1 == null) return false;

        return object1.equals(object2);
    }

    /**
     * Helper method for evaluating inner expressions
     * @param expression the expression to evaluate
     * @return the evaluation of the expression
     */
    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }
}
