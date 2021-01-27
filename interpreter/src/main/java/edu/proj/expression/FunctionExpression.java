package edu.proj.expression;

import edu.proj.interpreter.ProgramState;
import edu.proj.statement.Statement;
import java.util.ArrayList;
import java.util.List;

public class FunctionExpression extends Expression{
    private final String functionName;
    private final List<Expression> paramValues;

    public FunctionExpression(String functionName, List<Expression> paramValues){
        this.functionName = functionName;
        this.paramValues = paramValues;
    }

    @Override
    public int evaluate(ProgramState programState) {
        List<String> paramNames = programState.getFunctionParams(functionName);
        List<Statement> functionBody = programState.getFunctionStatements(functionName);
        List<Integer> paramVal = new ArrayList<>();

        for(int i = 0; i < paramValues.size(); i++){
            paramVal.add(paramValues.get(i).evaluate(programState));
        }
        Integer returnVal = null;

        programState.addNewCallFrame();

        for(int i = 0; i < paramNames.size(); i++){
            programState.setVariable(paramNames.get(i), paramVal.get(i));
        }

        for(Statement line : functionBody) {
            line.run(programState);
            if(programState.hasReturnValue()){
                returnVal = programState.getReturnValue();
                programState.removeCurrentCallFrame();
                programState.clearReturnValue();
                return returnVal;
            }
        }

        return returnVal;
    }
}
