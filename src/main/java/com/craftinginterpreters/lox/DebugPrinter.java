package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Assign;
import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Logical;
import com.craftinginterpreters.lox.Expr.Ternary;
import com.craftinginterpreters.lox.Expr.Unary;
import com.craftinginterpreters.lox.Expr.Variable;

import java.util.List;

public class DebugPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    public String print(Stmt statement) {
        return statement.accept(this);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        return expr.value().toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator().toString(), expr.right());
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator().toString(), expr.left(), expr.right());
    }

    @Override
    public String visitTernaryExpr(Ternary expr) {
        return parenthesize(expr.leftOperator().toString()
                        + expr.rightOperator().toString(), expr.left(), expr.middle(),
                expr.right());
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression());
    }

    @Override
    public String visitLogicalExpr(Logical expr) {
        return parenthesize(expr.operator().toString(), expr.left(), expr.right());
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        return "var " + expr.name().toString();
    }

    @Override
    public String visitAssignExpr(Assign expr) {
        return "var " + expr.name() + " = " + expr.value();
    }

    /*
     * Statement printers
     */
    @Override
    public String visitBlockStmt(Stmt.Block block) {
        StringBuilder builder = new StringBuilder();
        builder.append("{block ");

        for (Stmt statement : block.statements()) {
            builder.append(statement.accept(this));
        }

        builder.append("}");
        return builder.toString();
    }

    @Override
    public String visitExpressionStmt(Stmt.Expression stmt) {
        return parenthesize(";", stmt.expression());
    }

    @Override
    public String visitPrintStmt(Stmt.Print stmt) {
        return parenthesize("print", stmt.expression());
    }

    @Override
    public String visitVarStmt(Stmt.Var stmt) {
        if (stmt.initializer() == null) {
            return parenthesize2("var", stmt.name());
        }
        return parenthesize2("var", stmt.name(), "=", stmt.initializer());
    }

    @Override
    public String visitIfStmt(Stmt.If stmt) {
        if (stmt.elseBranch() == null) {
            return parenthesize2("if", stmt.condition(), stmt.thenBranch());
        }

        return parenthesize2("if-else", stmt.condition(), stmt.thenBranch(),
                stmt.elseBranch());
    }

    @Override
    public String visitWhileStmt(Stmt.While stmt) {
        return parenthesize2("while", stmt.condition(), stmt.body());
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ").append(expr.accept(this));
        }
        builder.append(")");

        return builder.toString();
    }

    // Note: AstPrinting other types of syntax trees is not shown in the
    // book, but this is provided here as a reference for those reading
    // the full code.
    //https://github.com/munificent/craftinginterpreters/blob/master/java/com/craftinginterpreters/lox/AstPrinter.java#L61
    private String parenthesize2(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        transform(builder, parts);
        builder.append(")");

        return builder.toString();
    }

    private void transform(StringBuilder builder, Object... parts) {
        for (Object part : parts) {
            builder.append(" ");
            if (part instanceof Expr) {
                builder.append(((Expr)part).accept(this));
            } else if (part instanceof Stmt) {
                builder.append(((Stmt) part).accept(this));
            } else if (part instanceof Token) {
                builder.append(((Token) part).lexeme);
            } else if (part instanceof List) {
                transform(builder, ((List) part).toArray());
            } else {
                builder.append(part);
            }
        }
    }
}
