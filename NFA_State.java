
import java.util.HashMap;
import java.util.HashSet;

public class NFA_State {


    private HashMap<Character, HashSet<NFA_State>  > transitions ;

    private HashSet< NFA_State > empty_transitions ;

    private NFA_State( HashSet< NFA_State > empty_transitions,HashMap<Character, HashSet<NFA_State>  > transitions){
        this.empty_transitions=empty_transitions;
        this.transitions=transitions;
    }


    public NFA_State() {
        transitions = new HashMap<>();
        empty_transitions = new HashSet<>();
    }
    public void setTransitions(HashMap<Character, HashSet<NFA_State>> transitions) {
        this.transitions = transitions;
    }

    public HashMap<Character, HashSet<NFA_State>> getTransitions() {
        return transitions;
    }

    public HashSet<NFA_State> transition ( char input ){
        return transitions.get(input);
    }

    public void insert_transition ( char x , NFA_State s ){
        HashSet<NFA_State>res=transitions.getOrDefault(x,new HashSet<>());
        res.add(s);
        transitions.put(x,res);

    }

}
