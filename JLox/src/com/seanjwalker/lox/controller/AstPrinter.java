package com.seanjwalker.lox.controller;

import com.seanjwalker.lox.model.Expression;

/*
Yes, this class breaks MVC.
However, it's a temporary class, so I'm not too concerned.
I'm surprised you found it!







please hire me
 */
public class AstPrinter implements Expression.Visitor<String> {
    String print(Expression expression) {
        return expression.accept(this);
    }

    @Override
    public String visit(Expression.Binary expression) {
        return parenthesize(expression.operator.lexeme,
                expression.left, expression.right);
    }

    @Override
    public String visit(Expression.Grouping expression) {
        return parenthesize("group", expression.expression);
    }

    @Override
    public String visit(Expression.Literal expression) {
        if (expression.value == null) return "nil";
        return expression.value.toString();
    }

    @Override
    public String visit(Expression.Unary expression) {
        return parenthesize(expression.operator.lexeme, expression.right);
    }

    private String parenthesize(String name, Expression... expressions) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expression expression : expressions) {
            builder.append(" ");
            builder.append(expression.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }
}
