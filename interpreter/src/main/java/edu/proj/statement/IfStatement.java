package edu.proj.statement;

import edu.proj.expression.Condition;
import edu.proj.interpreter.ProgramState;
import java.util.List;

public class IfStatement extends BlockStatement{
    public IfStatement(Condition condition, List<Statement> body){
        super(condition, body);
    }

    @Override
    public void run(ProgramState programState) {
        if(getCondition().evaluate(programState))
        runBlock(programState);
    }
}
