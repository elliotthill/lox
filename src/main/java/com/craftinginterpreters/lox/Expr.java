package com.craftinginterpreters.lox;
import java.util.List;

abstract class Expr {

    protected interface Visitor<R> {

        R visitLiteralExpr(Literal expr);

        R visitAssignExpr(Assign expr);

        R visitUnaryExpr(Unary expr);

        R visitBinaryExpr(Binary expr);

        R visitTernaryExpr(Ternary expr);

        R visitGroupingExpr(Grouping expr);

        R visitVariableExpr(Variable expr);

        R visitLogicalExpr(Logical expr);

        R visitCallExpr(Call expr);

    }

    /*
     * NUMBER | STRING |  "true" |  "false" | "nil"
     *
     * Examples:
     * 99
     * testing
     * "testing"
     */
    protected static class Literal extends Expr {

        private final Object value;

        protected Literal(Object value) {
            this.value = value;
        }

        public Object value() {
            return this.value;
        }

        public String toString() {

            return Lox.stringify(this.value);
        }
        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }

    }

    /*
     * EQUAL | expression
     *
     * Examples
     * = 2
     * = (1 + 2)
     * = 1 != 2
     */
    protected static class Assign extends Expr {

        private final Token name;

        private final Expr value;

        protected Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        public Token name() {
            return this.name;
        }

        public Expr value() {
            return this.value;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }

    }

    /*
     *  ( "!" | "-") unary | primary
     *
     * Examples
     * !1
     * !(1==1)
     * --1
     */
    protected static class Unary extends Expr {

        private final Token operator;

        private final Expr right;

        protected Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        public Token operator() {
            return this.operator;
        }

        public Expr right() {
            return this.right;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }

    }

    /*
     * expression operator expression
     *
     * Examples
     * 1 == 1
     * 1 + 1
     */
    protected static class Binary extends Expr {

        private final Expr left;

        private final Token operator;

        private final Expr right;

        protected Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public Expr left() {
            return this.left;
        }

        public Token operator() {
            return this.operator;
        }

        public Expr right() {
            return this.right;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }

    }

    protected static class Ternary extends Expr {

        private final Expr left;

        private final Token leftOperator;

        private final Expr middle;

        private final Token rightOperator;

        private final Expr right;

        protected Ternary(Expr left, Token leftOperator, Expr middle, Token rightOperator, Expr right) {
            this.left = left;
            this.leftOperator = leftOperator;
            this.middle = middle;
            this.rightOperator = rightOperator;
            this.right = right;
        }

        public Expr left() {
            return this.left;
        }

        public Token leftOperator() {
            return this.leftOperator;
        }

        public Expr middle() {
            return this.middle;
        }

        public Token rightOperator() {
            return this.rightOperator;
        }

        public Expr right() {
            return this.right;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }

    }

    /*
     *  "(" expression ")"
     */
    protected static class Grouping extends Expr {

        private final Expr expression;

        protected Grouping(Expr expression) {
            this.expression = expression;
        }

        public Expr expression() {
            return this.expression;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }

    }

    protected static class Variable extends Expr {

        private final Token name;

        protected Variable(Token name) {
            this.name = name;
        }

        public Token name() {
            return this.name;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }

    }

    protected static class Logical extends Expr {

        private final Expr left;

        private final Token operator;

        private final Expr right;

        protected Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public Expr left() {
            return this.left;
        }

        public Token operator() {
            return this.operator;
        }

        public Expr right() {
            return this.right;
        }

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }

    }

    protected static class Call extends Expr {

        private final Expr callee;
        private final Token paren;
        private final List<Expr> arguments;

        protected Call(Expr callee, Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;

        }

        public Expr callee() {return this.callee;}

        public Token paren() {return this.paren;}

        public List<Expr> arguments() {return this.arguments;}

        protected <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }

    }

    protected abstract <R> R accept(Visitor<R> visitor);

}
