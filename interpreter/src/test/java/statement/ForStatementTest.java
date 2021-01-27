package statement;

import edu.proj.expression.Condition;
import edu.proj.expression.Expression;
import edu.proj.interpreter.ProgramState;
import edu.proj.statement.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ForStatementTest {
    private final ProgramState state = new ProgramState();
    @Test
    public void instantiation(){
        List<Statement> loopBody = new ArrayList<>();
        ForStatement st = new ForStatement(new AssignStatement("z", Expression.create("0")),
                                            Condition.create("z == 0"),
                                            new AssignStatement("z", Expression.create("z + 1")),
                                            loopBody);

        assertThat(st instanceof BlockStatement, equalTo(true));
        assertThat(st instanceof LoopStatement, equalTo(true));
    }

    @Test
    public void breakOutLoop_VariableValue(){
        List<Statement> loopBody = new ArrayList<>();
        loopBody.add(new PrintStatement(Expression.create("x")));
        ForStatement st = new ForStatement(new AssignStatement("x", Expression.create("0")),
                                            Condition.create("x == 0"),
                                            new AssignStatement("x", Expression.create("x + 1")),
                                            loopBody);
        st.run(state);

        assertThat(state.getVariable("x"), equalTo(1));
    }

    @Test
    public void returnFromLoopEarly(){
        List<Statement> loopBody = new ArrayList<>();
        loopBody.add(new ReturnStatement((Expression.create("abc"))));
        ForStatement st = new ForStatement(new AssignStatement("abc", Expression.create("0")),
                                            Condition.create("abc < 5"),
                                            new AssignStatement("abc", Expression.create("abc + 1")),
                                            loopBody);
        st.run(state);

        assertThat(state.getVariable("abc"), equalTo(1));
    }

    @Test
    public void bodyNotExecuted(){
        List<Statement> loopBody = new ArrayList<>();
        loopBody.add(new PrintStatement(Expression.create("a")));
        ForStatement st = new ForStatement(new AssignStatement("a", Expression.create("9")),
                                            Condition.create("a == 57"),
                                            new AssignStatement("a", Expression.create("a + 1")),
                                            loopBody);
        st.run(state);

        assertThat(state.getVariable("a"), equalTo(9));
    }
}
