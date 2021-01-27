package statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.AssignStatement;
import edu.proj.statement.ReturnStatement;
import edu.proj.statement.Statement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AssignStatementTest {
    private final ProgramState state = new ProgramState();

    @Test
    public void assignConstantValue(){
        Expression exp = Expression.create("65");
        AssignStatement st = new AssignStatement("var", exp);
        st.run(state);
        assertThat(state.getVariable("var"), equalTo(65));
    }

    @Test
    public void assignVariableValue(){
        state.setVariable("X", 20);
        Expression exp = Expression.create("X");
        AssignStatement st = new AssignStatement("var", exp);
        st.run(state);
        assertThat(state.getVariable("var"), equalTo(20));
    }

    @Test
    public void assignArithmeticExpression(){
        AssignStatement st = new AssignStatement("var", Expression.create("100*4+3"));
        st.run(state);
        assertThat(state.getVariable("var"), equalTo(403));
    }

    @Test
    public void assignFunctionExpression(){
        List<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");

        List<Statement> body = new ArrayList<>();
        body.add(new ReturnStatement(Expression.create("a * b")));

        state.setFunctionParams("testFunc", params);
        state.setFunctionStatements("testFunc", body);

        AssignStatement st = new AssignStatement("var", Expression.create("testFunc(20, 20)"));
        st.run(state);
        assertThat(state.getVariable("var"), equalTo(400));
    }

    @Test
    public void AssignInvalidExpression(){
        try {
            Expression exp = Expression.create("These are words.");
            AssignStatement pst = new AssignStatement("Var", exp);
        }catch(RuntimeException e){ //exception expected for invalid expression
            assertThat(state.getVariable("Var"), equalTo(-1));
        }
    }
}
