package com.seanjwalker.lox.model;

/**
 * Represents a Lox expression
 */
public abstract class Expression {
    /**
     * Expression Visitor interface
     * @param <R> the type produced by the visit() method
     */
    public interface Visitor<R> {
        /**
         * Visit method for a Binary expression
         * @param expression the binary expression visited
         * @return some result of type R
         */
        R visit(Binary expression);

        /**
         * Visit method for a Grouping
         * @param expression the grouping expression visited
         * @return some result of type R
         */
        R visit(Grouping expression);

        /**
         * Visit method for a Literal expression
         * @param expression the literal expression visited
         * @return some result of type R
         */
        R visit(Literal expression);

        /**
         * Visit method for a Unary expression
         * @param expression the unary expression visited
         * @return some result of type R
         */
        R visit(Unary expression);
    }

    /**
     * An expression consisting of two operands
     */
    public static class Binary extends Expression {
        public final Expression left;
        public final Token operator;
        public final Expression right;

        /**
         * Constructor
         * @param left the first operand, written typically to the left of the operator
         * @param operator the operator representing an action on the operands
         * @param right the second operand, written typically to the right of the operator
         */
        public Binary(Expression left, Token operator, Expression right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        /**
         * Accepts an Expression Visitor for the visitor pattern
         * @param visitor the expression visitor
         * @return the result of visiting the expression
         * @param <R> the type produced by the visitor
         */
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    /**
     * Represents a grouping to contain an operation to preserve order of operations
     */
    public static class Grouping extends Expression {
        public final Expression expression;

        /**
         * Constructor
         * @param expression the expression contained in the grouping
         */
        public Grouping(Expression expression) {
            this.expression = expression;
        }

        /**
         * Accepts an Expression Visitor for the visitor pattern
         * @param visitor the expression visitor
         * @return the result of visiting the expression
         * @param <R> the type produced by the visitor
         */
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    /**
     * Represents a literal value
     */
    public static class Literal extends Expression {
        public final Object value;

        /**
         * Constructor
         * @param value the value of the literal
         */
        public Literal(Object value) {
            this.value = value;
        }

        /**
         * Accepts an Expression Visitor for the visitor pattern
         * @param visitor the expression visitor
         * @return the result of visiting the expression
         * @param <R> the type produced by the visitor
         */
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    /**
     * Represents one operand paired with an operator
     */
    public static class Unary extends Expression {
        public final Token operator;
        public final Expression right;

        /**
         * Constructor
         * @param operator the operator representing an action on the operand
         * @param right the operand being acted upon
         */
        public Unary(Token operator, Expression right) {
            this.operator = operator;
            this.right = right;
        }

        /**
         * Accepts an Expression Visitor for the visitor pattern
         * @param visitor the expression visitor
         * @return the result of visiting the expression
         * @param <R> the type produced by the visitor
         */
        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.visit(this);
        }
    }

    /**
     * Accepts an Expression Visitor for the visitor pattern
     * @param visitor the expression visitor
     * @return the result of visiting the expression
     * @param <R> the type produced by the visitor
     */
    public abstract <R> R accept(Visitor<R> visitor);
}
