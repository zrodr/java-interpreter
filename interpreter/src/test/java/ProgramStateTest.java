import edu.proj.interpreter.ProgramState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.Test;

public class ProgramStateTest {
    @Test
    public void checkReturnValue(){
        ProgramState state = new ProgramState();
        state.setReturnValue(50);

        assertThat(state.getReturnValue(), equalTo(50));
        assertThat(state.hasReturnValue(), equalTo(true));
    }

    @Test
    public void checkEmptyReturnValue(){
        ProgramState state = new ProgramState();

        assertThat(state.hasReturnValue(), equalTo(false));
    }

    @Test
    public void alteringCallStack(){
        ProgramState state = new ProgramState();
        state.addNewCallFrame();
        assertThat(state.getStackSize(), equalTo(2));
        state.addNewCallFrame();
        state.addNewCallFrame();
        state.removeCurrentCallFrame();

        assertThat(state.getStackSize(), equalTo(3));
    }

    @Test
    public void popEmptyCallStack(){
        ProgramState state = new ProgramState(); //callStack initialized with one frame
        state.removeCurrentCallFrame();
        state.removeCurrentCallFrame();
        //if we reach this line with no exception then the stack did not allow
        //us to pop, since size would be = 0.
        assertThat(true, equalTo(true));
    }

    @Test
    public void storeVariables(){
        ProgramState state = new ProgramState();
        state.setVariable("Var", 65);
        state.setVariable("X", 43);

        assertThat(state.getVariable("Var"), equalTo(65));
        assertThat(state.getVariable("X"), equalTo(43));
    }

    @Test
    public void accessInvalidVariables(){
        ProgramState state = new ProgramState();
        assertThat(state.getVariable("Yikes"), equalTo(-1));
        assertThat(state.getVariable("oof"), equalTo(-1));
    }

    @Test
    public void accessEmptyFunctionParams(){
        ProgramState state = new ProgramState();
        assertThat(state.getFunctionParams("IDon'tExist"), equalTo(null));
    }

    @Test
    public void accessEmptyFunctionStatements(){
        ProgramState state = new ProgramState();
        assertThat(state.getFunctionStatements("fakeFunction"), equalTo(null));
    }
}
