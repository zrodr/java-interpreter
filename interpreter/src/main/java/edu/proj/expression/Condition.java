package edu.proj.expression;

import edu.proj.interpreter.ProgramState;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Condition {
    public enum Operator {
        EQUALS("=="),
        NOT_EQUALS("!="),
        LESS_THAN_EQUALS("<="),
        LESS_THAN("<"),
        GREATER_THAN_EQUALS(">="),
        GREATER_THAN(">");

        private final String symbol;

        Operator(String symbol) {
            this.symbol = symbol;
        }

        String getSymbol() {
            return symbol;
        }
    }

    private final Operator operator;
    private final Expression lhs;
    private final Expression rhs;

    public Condition(Operator operator, Expression lhs, Expression rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public boolean evaluate(ProgramState programState) {
        int lhsValue = lhs.evaluate(programState);
        int rhsValue = rhs.evaluate(programState);
        return switch (operator) {
            case EQUALS -> lhsValue == rhsValue;
            case NOT_EQUALS -> lhsValue != rhsValue;
            case LESS_THAN_EQUALS -> lhsValue <= rhsValue;
            case LESS_THAN -> lhsValue < rhsValue;
            case GREATER_THAN_EQUALS -> lhsValue >= rhsValue;
            case GREATER_THAN -> lhsValue > rhsValue;
        };
    }

    public static Condition create(String conditionString) {
        for (Operator operator: Operator.values()) {
            Matcher matcher =
                    Pattern.compile(String.format("^(.+)%s(.+)$", operator.getSymbol())).matcher(conditionString);
            if (matcher.matches()) {
                return new Condition(
                        operator,
                        Expression.create(matcher.group(1).strip()),
                        Expression.create(matcher.group(2).strip()));
            }
        }
        throw new RuntimeException("Invalid condition: " + conditionString);
    }
}
