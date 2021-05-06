import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class NFA implements Serializable {
    private NFA_State start_state;

    private HashSet<NFA_State> accepting_states;

    private HashSet<NFA_State> states;

    public NFA(NFA_State start_state,HashSet<NFA_State> accepting_states,HashSet<NFA_State> states){
        this.start_state=start_state;
        this.accepting_states=accepting_states;
        this.states=states;
    }


    public HashSet<NFA_State> getStates() {
        return states;
    }

    public void setStates(HashSet<NFA_State> states) {
        this.states = states;
    }

    public NFA(NFA_State start_state) {
        this.start_state = start_state;
        accepting_states = new HashSet<>();
        states = new HashSet<>();
    }

    public NFA_State getStart_state() {
        return start_state;
    }

    public void setStart_state(NFA_State start_state) {
        this.start_state = start_state;
    }

    public HashSet<NFA_State> getAccepting_states() {
        return accepting_states;
    }

    public void setAccepting_states(HashSet<NFA_State> accepting_states) {
        this.accepting_states = accepting_states;
    }

}