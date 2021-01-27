package statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.ReturnStatement;
import edu.proj.statement.Statement;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ReturnStatementTest {
    private final ProgramState state = new ProgramState();

    @Test
    public void returnConstant(){
        Expression exp = Expression.create("500");
        ReturnStatement st = new ReturnStatement(exp);
        st.run(state);

        assertThat(state.hasReturnValue(), equalTo(true));
        assertThat(state.getReturnValue(), equalTo(500));
    }

    @Test
    public void returnVariable(){
        state.setVariable("X", 13);
        Expression exp = Expression.create("X");
        ReturnStatement st = new ReturnStatement(exp);
        st.run(state);

        assertThat(state.hasReturnValue(), equalTo(true));
        assertThat(state.getReturnValue(), equalTo(13));
    }

    @Test
    public void returnArithmeticExp(){
        Expression exp = Expression.create("60+4*2");
        ReturnStatement st = new ReturnStatement(exp);
        st.run(state);

        assertThat(state.hasReturnValue(), equalTo(true));
        assertThat(state.getReturnValue(), equalTo(68));
    }

    @Test
    public void returnFunctionExp(){
        List<String> params = new ArrayList<>();
        params.add("a");
        params.add("b");

        List<Statement> body = new ArrayList<>();
        Expression returnExp = Expression.create("a * b");
        body.add(new ReturnStatement(returnExp));

        state.setFunctionParams("testFunc", params);
        state.setFunctionStatements("testFunc", body);

        Expression exp = Expression.create("testFunc(5, 6)");

        ReturnStatement st = new ReturnStatement(exp);
        st.run(state);

        assertThat(state.hasReturnValue(), equalTo(true));
        assertThat(state.getReturnValue(), equalTo(30));
    }

    @Test
    public void returnInvalidExp(){
        try{
            Expression exp = Expression.create("15 X 50");
            ReturnStatement st = new ReturnStatement(exp);
            st.run(state);
        }catch(RuntimeException e){
            assertThat(state.hasReturnValue(), equalTo(false));
        }
    }
}
