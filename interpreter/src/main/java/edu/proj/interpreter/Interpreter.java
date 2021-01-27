package edu.proj.interpreter;

import edu.proj.expression.*;
import edu.proj.statement.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Interpreter {
    // Assorted regular expression patterns for statements we'll be parsing.
    private static final Pattern PRINT_PATTERN = Pattern.compile("^print\\((.+)\\)$");
    private static final Pattern ASSIGN_PATTERN = Pattern.compile("^(.+) = (.+)$");

    private static final Pattern IF_PATTERN = Pattern.compile("^if \\((.+)\\) \\{$");
    private static final Pattern WHILE_PATTERN = Pattern.compile("^while \\((.+)\\) \\{$");
    private static final Pattern FOR_PATTERN = Pattern.compile("^for \\(([^;]+);([^;]+);([^;]+)\\) \\{$");
    private static final Pattern END_BLOCK_PATTERN = Pattern.compile("}");

    private static final Pattern DEFINE_FUNCTION_PATTERN =
            Pattern.compile("^define ([A-Za-z][A-Za-z0-9_]*)\\(([A-Za-z0-9_,\\s]*)\\) \\{$");
    private static final Pattern RETURN_PATTERN = Pattern.compile("^return (.+)$");

    // The list of Statements created from parsing the Strings comprising the program.
    private final List<Statement> statements = new ArrayList<>();

    /**
     * Creates the Interpreter object from an array of Strings representing a program to be run. The constructor will
     * parse the lines into executable Statements, which can be invoked with the runProgram method.
     */
    public Interpreter(List<String> program) {
        Queue<String> lines =
                program.stream()
                        .map(String::strip)
                        .filter(Predicate.not(String::isEmpty))
                        .collect(Collectors.toCollection(LinkedList::new));
        while (!lines.isEmpty()) {
            Statement statement = parseStatement(lines);
            statements.add(statement);
        }
    }

    // Parse a single Statement from the front of the provided deque. Note that with block statements (if statements,
    // while loops), a single Statement may involve multiple lines with multiple contained statements.
    private Statement parseStatement(Queue<String> lines) {
        String line = lines.remove();

        // Multi-line statements.
        Statement statement = parseWhileStatement(line, lines);
        if (statement != null) {
            return statement;
        }
        statement = parseForStatement(line, lines);
        if (statement != null) {
            return statement;
        }
        statement = parseIfStatement(line, lines);
        if (statement != null) {
            return statement;
        }
        statement = parseDefineFunctionStatement(line, lines);
        if (statement != null) {
            return statement;
        }

        // Single line statements.
        statement = parsePrintStatement(line);
        if (statement != null) {
            return statement;
        }
        statement = parseAssignStatement(line);
        if (statement != null) {
            return statement;
        }
        statement = parseReturnStatement(line);
        if (statement != null) {
            return statement;
        }

        throw new RuntimeException("Unrecognized statement: " + line);
    }

    // Attempts to parse the line as a print statement. Returns null if it's not a match.
    private Statement parsePrintStatement(String line) {
        Matcher printMatcher = PRINT_PATTERN.matcher(line);
        if (printMatcher.matches()) {
            String expressionLine = printMatcher.group(1).strip();
            // The line is in the format:
            //
            //     print(<expression>)
            //
            // where <expression> is an expression as a String stored in expressionLine. For example, if the full line
            // was "print(10)", expressionLine would be the String "10".
            return new PrintStatement(Expression.create(expressionLine));
        }
        return null;
    }

    // Attempts to parse the line as an assign statement. Returns null if it's not a match.
    private Statement parseAssignStatement(String line) {
        Matcher assignMatcher = ASSIGN_PATTERN.matcher(line);
        if (assignMatcher.matches()) {
            String variableName = assignMatcher.group(1).strip();
            String expressionLine = assignMatcher.group(2).strip();

            // The line is in the format:
            //
            //     <variable> = <expression>
            //
            // where <variable> is the name of a variable as a String, stored in variableName, and <expression> is an
            // expression as a String, stored in expressionLine. For example, if the full line was "x = y + 5",
            // variableName would be the String "x" and expressionLine would be the String "y + 5".
            return new AssignStatement(variableName, Expression.create(expressionLine));
        }

        return null;
    }

    // Attempts to parse the line as an if statement. Returns null if it's not a match.
    private Statement parseIfStatement(String line, Queue<String> lines) {
        Matcher ifMatcher = IF_PATTERN.matcher(line);
        if (ifMatcher.matches()) {
            String conditionLine = ifMatcher.group(1);
            List<Statement> blockStatements = parseBlockStatements(lines);
            // The lines are in the format:
            //
            //     if (<condition>) {
            //         <statement>
            //         <statement>
            //         ...
            //     }
            //
            // where <condition> is a comparison as a String, stored in conditionLine (e.g. "x < 10"), and each
            // <statement> is parsed as an individual Statement object, collected in the blockStatements list.
            return new IfStatement(Condition.create(conditionLine), blockStatements);
        }

        return null;
    }

    // Attempts to parse the line as a while statement. Returns null if it's not a match.
    private Statement parseWhileStatement(String line, Queue<String> lines) {
        Matcher whileMatcher = WHILE_PATTERN.matcher(line);
        if (whileMatcher.matches()) {
            String conditionLine = whileMatcher.group(1);
            List<Statement> loopStatements = parseBlockStatements(lines);

            // The lines are in the format:
            //
            //     while (<condition>) {
            //         <statement>
            //         <statement>
            //         ...
            //     }
            //
            // where <condition> is a comparison as a String, stored in conditionLine (e.g. "x < 10"), and each
            // <statement> is parsed as an individual Statement object, collected in the loopStatements list.
            return new WhileStatement(Condition.create(conditionLine), loopStatements);
        }

        return null;
    }

    // Attempts to parse the line as a for statement. Returns null if it's not a match.
    private Statement parseForStatement(String line, Queue<String> lines) {
        Matcher forMatcher = FOR_PATTERN.matcher(line);
        if (forMatcher.matches()) {
            String initializationLine = forMatcher.group(1);
            String conditionLine = forMatcher.group(2);
            String updateLine = forMatcher.group(3);
            List<Statement> loopStatements = parseBlockStatements(lines);

            // The lines are in the format:
            //
            //     for (<initialization>; <condition>; <update>) {
            //         <statement>
            //         <statement>
            //         ...
            //     }
            //
            // where <initialization> is an assign statement as a String, stored in initializationLine; <condition> is a
            // comparison as a String, stored in conditionLine; <update> is an assign statement as a String, stored in
            // updateLine; and each <statement> is parsed as an individual Statement object, collected in the
            // loopStatements list. For example, suppose we have the following loop:
            //
            //     for (x = 0; x < 10; x = x + 1) {
            //         print(x);
            //     }
            //
            // - initializationLine is the String "x = 0"
            // - conditionLine is the String "x < 10"
            // - updateLine is the String "x = x + 1"
            // - loopStatements is a List with one Statement, the PrintStatement created from "print(x)"
            Statement init = parseAssignStatement(initializationLine);
            Statement update = parseAssignStatement(updateLine);
            return new ForStatement(init, Condition.create(conditionLine), update, loopStatements);
        }

        return null;
    }

    // Attempts to parse the line as a function definition. Returns null if it's not a match.
    private Statement parseDefineFunctionStatement(String line, Queue<String> lines) {
        Matcher defineFunctionMatcher = DEFINE_FUNCTION_PATTERN.matcher(line);
        if (defineFunctionMatcher.matches()) {
            String functionName = defineFunctionMatcher.group(1);
            List<String> parameterNames =
                    Arrays.stream(defineFunctionMatcher.group(2).split(","))
                            .map(String::strip)
                            .collect(Collectors.toList());
            if (parameterNames.size() == 1 && parameterNames.get(0).isEmpty()) {
                parameterNames = new ArrayList<>();
            }
            for (String parameterName: parameterNames) {
                if (!parameterName.matches(Expression.VARIABLE_NAME_PATTERN.pattern())) {
                    throw new RuntimeException("Invalid parameter name: " + parameterName);
                }
            }

            List<Statement> functionStatements = parseBlockStatements(lines);

            // The lines are in the format:
            //
            //     define <function>(<parameter>, <parameter>, ...) {
            //         <statement>
            //         <statement>
            //         ...
            //     }
            //
            // where <function> is the name of the function as a String, stored in functionName; each <parameter> is the
            // name of a parameter as a String, collected in the parameterNames list; and each <statement> is parsed as
            // an individual Statement object, collected in the functionStatements list. For example, suppose we have
            // the following function definition:
            //
            //     define sum(a, b, c) {
            //         print(a)
            //         print(b)
            //         print(c)
            //         return a + b + c
            //     }
            //
            // - functionName is the String "sum"
            // - parameterNames is a list with three Strings, "a", "b", and "c"
            // - functionStatements is a list with four Statement objects: three PrintStatements and one ReturnStatement

            return new DefineFunctionStatement(functionName, parameterNames, functionStatements);
        }

        return null;
    }

    // Attempts to parse the line as a return statement. Returns null if it's not a match.
    private Statement parseReturnStatement(String line) {
        Matcher returnMatcher = RETURN_PATTERN.matcher(line);
        if (returnMatcher.matches()) {
            String returnLine = returnMatcher.group(1);

            // The line is in the format:
            //
            //     return <expression>
            //
            // where <expression> is an expression as a String stored in returnLine. For example, if the full line was
            // "return x + y", expressionLine would be the String "x + y".
            return new ReturnStatement(Expression.create(returnLine));
        }

        return null;
    }

    // parseBlockStatements is called when parsing any statement type with multiple lines.
    private List<Statement> parseBlockStatements(Queue<String> lines) {
        List<Statement> blockStatements = new ArrayList<>();
        while (!lines.isEmpty()) {
            String nextLine = lines.peek();
            if (nextLine.matches(END_BLOCK_PATTERN.pattern())) {
                lines.remove();
                return blockStatements;
            }

            // If not, we parse it as a normal Statement. Note that the next line can itself be a multi-line statement,
            // like an if statement or a while loop. The recursive call to parseStatement will handle the intricacies of
            // checking for the correct balancing of block delimiters ("{" and "}").
            blockStatements.add(parseStatement(lines));
        }

        throw new RuntimeException("Block was started but never finished.");
    }

    public void runProgram() {
        ProgramState programState = new ProgramState();
        for (Statement statement: statements) {
            statement.run(programState);
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new RuntimeException("Must provide the program file name to run.");
        }
        if (args.length > 1) {
            throw new RuntimeException("Only one argument expected (program file name).");
        }

        ArrayList<String> programLines = new ArrayList<>();
        try {
            String programFileName = args[0];
            BufferedReader bufferedReader = new BufferedReader(new FileReader(programFileName));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                programLines.add(line);
            }
            bufferedReader.close();
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }

        Interpreter interpreter = new Interpreter(programLines);
        interpreter.runProgram();
    }
}
