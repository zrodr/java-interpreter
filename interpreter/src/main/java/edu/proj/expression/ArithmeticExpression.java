package edu.proj.expression;

import edu.proj.interpreter.ProgramState;


public class ArithmeticExpression extends Expression {
    public enum Operator {
        ADD("+", 1),
        SUBTRACT("-", 1),
        MULTIPLY("*", 2),
        DIVIDE("/", 2),
        REMAINDER("%", 2),
        POWER("^", 3);

        private final String symbol;
        private final int precedence;

        Operator(String symbol, int precedence) {
            this.symbol = symbol;
            this.precedence = precedence;
        }

        String getSymbol() {
            return symbol;
        }

        int getPrecedence() {
            return precedence;
        }
    }

    private final Operator operator;
    private final Expression lhs;
    private final Expression rhs;

    public ArithmeticExpression(Expression lhs, Operator op, Expression rhs){
        this.lhs = lhs;
        this.operator = op;
        this.rhs = rhs;
    }

    @Override
    public int evaluate(ProgramState programState) {
        return switch (operator){
            case ADD -> lhs.evaluate(programState) + rhs.evaluate(programState);
            case SUBTRACT -> lhs.evaluate(programState) - rhs.evaluate(programState);
            case MULTIPLY -> lhs.evaluate(programState) * rhs.evaluate(programState);
            case DIVIDE -> lhs.evaluate(programState) / rhs.evaluate(programState);
            case REMAINDER -> lhs.evaluate(programState) % rhs.evaluate(programState);
            case POWER -> (int)Math.pow(lhs.evaluate(programState), rhs.evaluate(programState));
        };
    }
}
