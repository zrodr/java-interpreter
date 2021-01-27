package statement;

import edu.proj.expression.VariableExpression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.PrintStatement;
import edu.proj.expression.Expression;
import edu.proj.statement.ReturnStatement;
import edu.proj.statement.Statement;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class PrintStatementTest {
    private final ProgramState state = new ProgramState();

    @Test
    public void printConstantExpression(){
        Expression exp = Expression.create("567");
        PrintStatement pst = new PrintStatement(exp);
        assertThat(exp.evaluate(state), equalTo(567));
        pst.run(state);
    }

    @Test
    public void printVariableExpression(){
        state.setVariable("var", 16);
        VariableExpression exp = new VariableExpression("var");
        PrintStatement pst = new PrintStatement(exp);
        assertThat(exp.evaluate(state), equalTo(16));
        pst.run(state);
    }

    @Test
    public void printArithmeticExpression(){
        Expression exp = Expression.create("75/5+16");
        PrintStatement pst = new PrintStatement(exp);
        assertThat(exp.evaluate(state), equalTo(31));
        pst.run(state);
    }

    @Test
    public void printFunctionExpression(){
        List<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");

        List<Statement> body = new ArrayList<>();
        Expression returnExp = Expression.create("a + b");

        body.add(new ReturnStatement(returnExp));
        state.setFunctionParams("testFunction", params);
        state.setFunctionStatements("testFunction", body);

        Expression exp = Expression.create("testFunction(3, 4)");
        PrintStatement pst = new PrintStatement(exp);
        assertThat(exp.evaluate(state), equalTo(7));
        pst.run(state);
    }

    @Test
    public void printInvalidExpression(){
        Expression exp = Expression.create("hagdifnf");
        PrintStatement pst = new PrintStatement(exp);
        assertThat(exp.evaluate(state), equalTo(-1));
        pst.run(state);
    }
}
