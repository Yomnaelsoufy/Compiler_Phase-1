import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Main {

    static HashMap < String , NFA > reg_exp ;

    HashMap< String , NFA > reg_def ;

    HashMap < String , Integer > priorities ;

    HashMap < NFA_State,String > acceptence ;

    public static void main(String[] args)  {
        Main m=new Main();
        NFA a=m.convert_to_NFA('a');
        NFA b=m.convert_to_NFA('b');
        NFA aORb=m.or_NFA(a,b);
        NFA cloned=m.deepClone(aORb);
        NFA kleene=m.kleene_closure(a);
        NFA and=m.AND_NFA(a,kleene);
        reg_exp=new HashMap<>();
        reg_exp.put("a",a);
        reg_exp.put("b",b);
        reg_exp.put("aorb",aORb);
        NFA combined=m.combine_NFA();
    }

    public void parse_file(){

    }



    public NFA convert_to_NFA ( Character s ){

        NFA_State start=new NFA_State();
        NFA_State end=new NFA_State();

        if (s==null)
            start.getEmpty_transitions().add(end);
        else
            start.insert_transition(s,end);

        NFA res=new NFA();

        res.add_accepting_state(end);
        res.getStates().add(end);
        res.getStates().add(start);
        res.setStart_state(start);
        return res;
    }

    public void convert_to_DFA ( NFA nfa ){

    }

    public void minimize_DFA ( DFA dfa){

    }

    public void scan_input(File file , DFA dfa){
        // don't forget symbol table
    }


    public NFA kleene_closure(NFA nfa) {
        /* new start --(^)-> old start done
         * old final --(^)-> old start
         * old final --(^)-> new final
         * new start --(^)-> new final
         * set nfa start to be new start
         * set in nfa accepting states new final
         * remove old final from accepting states
         * set new start and new final in states set
         *  */
        NFA res= deepClone(nfa);
        NFA_State new_start=new NFA_State();
        NFA_State new_final=new NFA_State();
        NFA_State old_start=  res.getStart_state();
        HashSet<NFA_State> accept= res.getAccepting_states();
        NFA_State old_final= accept.iterator().next();
        HashSet<NFA_State> states=res.getStates();

        new_start.getEmpty_transitions().add(old_start);
        old_final.getEmpty_transitions().add(old_start);
        old_final.getEmpty_transitions().add(new_final);
        new_start.getEmpty_transitions().add(new_final);

        res.setStart_state(new_start);
        accept.remove(old_final);
        accept.add(new_final);
        res.setAccepting_states(accept);
        states.add(new_start);
        states.add(new_final);

        return  res;
    }

    public NFA or_NFA (NFA nfa1, NFA nfa2){
        /*
         * new start --(^)->old start1
         * new start --(^)->old start2
         * old final1 --(^)->new final
         * old final2 --(^)->new final
         * create NFA result & set start -->new start
         * move states in nfa1 and nfa2 to result nfa
         * set accepting state --> new final
         * */
        NFA clone_nfa1= deepClone(nfa1);
        NFA clone_nfa2=deepClone(nfa2);

        NFA_State new_start=new NFA_State();
        NFA_State new_final=new NFA_State();

        NFA_State old_start1=clone_nfa1.getStart_state();
        NFA_State old_final1=clone_nfa1.getAccepting_states().iterator().next();
        NFA_State old_start2=clone_nfa2.getStart_state();
        NFA_State old_final2=clone_nfa2.getAccepting_states().iterator().next();

        new_start.getEmpty_transitions().add(old_start1);
        new_start.getEmpty_transitions().add(old_start2);
        old_final1.getEmpty_transitions().add(new_final);
        old_final2.getEmpty_transitions().add(new_final);

        NFA res_nfa=new NFA(new_start);
        HashSet<NFA_State> res_states=res_nfa.getStates();
        res_states.add(new_start);
        res_states.addAll(clone_nfa1.getStates());
        res_states.addAll(clone_nfa2.getStates());
        res_states.add(new_final);
        res_nfa.add_accepting_state(new_final);

        return res_nfa;
    }


    public NFA AND_NFA (NFA nfa1, NFA nfa2){

        /*
         *get final of nfa1 (final1)and the start of nfa2(start2)
         * remove the accepting states from nfa1
         * merge the two nfas by make final1 same as start2
         * add states from nfa2 to nfa
         * */
        NFA clone_nfa1= deepClone(nfa1);
        NFA clone_nfa2=deepClone(nfa2);

        NFA_State start2=clone_nfa2.getStart_state();
        NFA_State final1=clone_nfa1.getAccepting_states().iterator().next();

        clone_nfa1.getAccepting_states().remove(final1);
        final1.setTransitions(start2.getTransitions());
        final1.setEmpty_transitions(start2.getEmpty_transitions());
        clone_nfa2.getStates().remove(clone_nfa2.getStart_state());
        clone_nfa1.getStates().addAll(clone_nfa2.getStates());
        clone_nfa1.getAccepting_states().addAll(clone_nfa2.getAccepting_states());

        return clone_nfa1;
    }

    public NFA combine_NFA(){
        NFA_State start =new NFA_State();
        NFA combined=new NFA(start);
    acceptence=new HashMap<>();
        for (String st:reg_exp.keySet()){
            NFA nfa=reg_exp.get(st);
            NFA_State nfa_start=nfa.getStart_state();
            start.getEmpty_transitions().add(nfa_start);
            combined.add_accepting_state(nfa.getAccepting_states().iterator().next());
            combined.getStates().addAll(nfa.getStates());
            NFA_State n=nfa.getAccepting_states().iterator().next();
            acceptence.put(n,st);
        }
        combined.getStates().add(start);
        return combined;
    }

    public NFA positive_closure(NFA nfa){
        NFA cloned=deepClone(nfa);
        return AND_NFA(cloned,kleene_closure(cloned));
    }

    private NFA deepClone(NFA nfa)
    {
        Map<NFA_State,NFA_State>created=new HashMap<>();
        NFA cloned=new NFA(dfs(nfa.getStart_state(),created));

        cloned.getStates().addAll(created.values());
        for (NFA_State nfa_state:nfa.getAccepting_states())
        {
          cloned.add_accepting_state(created.get(nfa_state));
        }
        return cloned;
     }

    private NFA_State dfs(NFA_State start_state, Map<NFA_State,NFA_State> created)
    {
        if (start_state==null)return null;


        if(created.containsKey(start_state)) {
            return created.get(start_state);
        }
        NFA_State currentNfa = new NFA_State();
        created.put(start_state, currentNfa);
        for(Character c:start_state.getTransitions().keySet()) {
            for (NFA_State n:start_state.getTransitions().get(c)){
                currentNfa.insert_transition(c,dfs(n,created));}
    }
        for (NFA_State n:start_state.getEmpty_transitions()){
            currentNfa.getEmpty_transitions().add(dfs(n,created));}
        return currentNfa;
    }
}
