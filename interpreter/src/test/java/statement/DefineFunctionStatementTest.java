package statement;

import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;

import edu.proj.statement.DefineFunctionStatement;
import edu.proj.statement.ReturnStatement;
import edu.proj.statement.Statement;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class DefineFunctionStatementTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void storeParamsAndBody(){
        List<String> params = new ArrayList<>();
        params.add("a");
        List<Statement> body = new ArrayList<>();
        body.add(new ReturnStatement(Expression.create("a * a * a")));

        DefineFunctionStatement st = new DefineFunctionStatement("Func", params, body);
        st.run(state);
        assertThat(state.getFunctionParams("Func"), equalTo(params));
        assertThat(state.getFunctionStatements("Func"), equalTo(body));
    }

    @Test
    public void declareFunctionNoParams(){
        List<String> params = new ArrayList<>();
        List<Statement> body = new ArrayList<>();
        body.add(new ReturnStatement(Expression.create("2 + 2")));

        DefineFunctionStatement st = new DefineFunctionStatement("test", params, body);
        st.run(state);

        assertThat(state.getFunctionParams("test"), equalTo(params));
    }

    @Test
    public void declareFunctionNoBody(){
        List<String> params = new ArrayList<>();
        params.add("a");
        List<Statement> body = new ArrayList<>();

        DefineFunctionStatement st = new DefineFunctionStatement("iDoNothing", params, body);
        st.run(state);

        assertThat(state.getFunctionStatements("iDoNothing"), equalTo(body));
    }
}
