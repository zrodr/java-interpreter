package edu.proj.statement;

import edu.proj.interpreter.ProgramState;
/**
 * A single runnable program statement. All Statements must support the run method, which defines the behavior carried
 * out when the statement is executed.
 */
public abstract class Statement {
    /**
     * Runs the statement.
     *
     * @param programState An object representing the state of the running program. Statements require knowledge of
     *                     program state in order to execute.
     */
    public abstract void run(ProgramState programState);
}
