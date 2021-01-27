package edu.proj.expression;

import edu.proj.interpreter.ProgramState;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * An expression that can be understood by the interpreter and evaluated to a single integer value. Expressions may
 * consist of integer constants, references to variables, or combinations of those using arithmetic operators. At the
 * time when an Expression object is created, we simply store those individual parts of the expression as-is. They are
 * not resolved and reduced to a single integer until evaluate is called during program runtime.
 */
public abstract class Expression {
    // Regular expression patterns for recognized Expression types.
    private static final Pattern CONSTANT_PATTERN = Pattern.compile("^-?[0-9_]+$");
    public static final Pattern VARIABLE_NAME_PATTERN = Pattern.compile("^[A-Za-z][A-Za-z0-9_]*$");
    private static final Pattern FUNCTION_CALL_PATTERN = Pattern.compile("^([A-Za-z][A-Za-z0-9_]*)\\(([^()]*)\\)$");

    /**
     * Returns the integer value to which this expression evaluates.
     *
     * @param programState An object representing the state of the running program. Expressions require knowledge of
     *                     program state to evaluate to actual integer values.
     */
    public abstract int evaluate(ProgramState programState);

    /**
     * Creates the appropriate Expression object from the given string. If none of the Expression subtypes match the
     * String provided, this method will throw a RuntimeException.
     *
     * @param expressionString The String to parse into an Expression
     * @return An instantiation of one of Expression's subclasses based on expressionString
     */
    public static Expression create(String expressionString) {
        Expression expression = parseConstantExpression(expressionString);
        if (expression != null) {
            return expression;
        }
        expression = parseVariableExpression(expressionString);
        if (expression != null) {
            return expression;
        }
        expression = parseFunctionExpression(expressionString);
        if (expression != null) {
            return expression;
        }
        return parseArithmeticExpression(expressionString);
    }

    // Attempts to parse expressionString as an integer constant.
    private static Expression parseConstantExpression(String expressionString) {
        if (expressionString.matches(CONSTANT_PATTERN.pattern())) {
            int value = Integer.parseInt(expressionString);
            return new ConstantExpression(value);
        }
        return null;
    }

    // Attempts to parse expressionString as a variable name.
    private static Expression parseVariableExpression(String expressionString) {
        if (expressionString.matches(VARIABLE_NAME_PATTERN.pattern())) {
            return new VariableExpression(expressionString);
        }

        return null;
    }

    // Attempts to parse expressionString as a function call.
    private static Expression parseFunctionExpression(String expressionString) {
        Matcher functionCallMatcher = FUNCTION_CALL_PATTERN.matcher(expressionString);
        if (functionCallMatcher.matches()) {
            String functionName = functionCallMatcher.group(1);
            List<String> parameterList =
                    Arrays.stream(functionCallMatcher.group(2).split(","))
                            .map(String::strip)
                            .collect(Collectors.toList());
            List<Expression> parameterValues;
            if (parameterList.size() == 1 && parameterList.get(0).isEmpty()) {
                parameterValues = new ArrayList<>();
            } else {
                parameterValues = parameterList.stream().map(Expression::create).collect(Collectors.toList());
            }

            return new FunctionExpression(functionName, parameterValues);
        }

        return null;
    }

    // Attempts to parse expressionString as an arithmetic combination of other known Expression types.
    private static Expression parseArithmeticExpression(String expressionString) {

        // If the expression string doesn't appear to be a constant, variable, or function call, we will attempt to
        // interpret it as an ArithmeticExpression. To translate from a String token representing an operator (e.g. "+",
        // "-") to the actual Operator enum, we build a map and iterate through Operator.values()
        Map<String, ArithmeticExpression.Operator> operatorsBySymbol = new HashMap<>();
        for (ArithmeticExpression.Operator operator: ArithmeticExpression.Operator.values()) {
            operatorsBySymbol.put(operator.getSymbol(), operator);
        }

        Stack<Expression> operands = new Stack<>();
        Stack<ArithmeticExpression.Operator> operators = new Stack<>();

        // The string of delimiters is automatically constructed from the symbols of the available Operators.
        String delimiters =
                Arrays.stream(ArithmeticExpression.Operator.values())
                        .map(ArithmeticExpression.Operator::getSymbol)
                        .collect(Collectors.joining());
        StringTokenizer tokenizer = new StringTokenizer(expressionString, delimiters, true);
        if (tokenizer.countTokens() == 1) {
            throw new RuntimeException("Unrecognized expression: " + expressionString);
        }

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().strip();
            ArithmeticExpression.Operator nextOperator = operatorsBySymbol.get(token);
            if (nextOperator == null) {
                if (token.contains("(") && !token.contains(")")) {
                    StringBuilder functionExpressionBuilder = new StringBuilder(token);
                    while (tokenizer.hasMoreTokens() && !token.contains(")")) {
                        token = tokenizer.nextToken();
                        functionExpressionBuilder.append(token);
                    }
                    token = functionExpressionBuilder.toString().strip();
                    if (!token.contains(")")) {
                        throw new RuntimeException("Missing closing ')' for function call: " + token);
                    }
                }

                operands.add(create(token));
            } else {
                while (!operators.isEmpty() && operators.peek().getPrecedence() >= nextOperator.getPrecedence()) {
                    processOperator(operands, operators, expressionString);
                }
                operators.push(nextOperator);
            }
        }

        while (!operators.isEmpty()) {
            processOperator(operands, operators, expressionString);
        }
        if (operands.size() != 1) {
            throw new RuntimeException("Invalid expression due to too many operands: " + expressionString);
        }
        return operands.pop();
    }

    // Process one iteration of the operator stack.
    private static void processOperator(
            Stack<Expression> operands,
            Stack<ArithmeticExpression.Operator> operators,
            String expressionString) {
        if (operands.size() < 2) {
            throw new RuntimeException("Invalid expression due to missing operands: " + expressionString);
        }

        ArithmeticExpression.Operator operator = operators.pop();
        Expression rhs = operands.pop();
        Expression lhs = operands.pop();

        ArithmeticExpression combined = new ArithmeticExpression(lhs, operator, rhs);

        operands.push(combined);
    }
}
