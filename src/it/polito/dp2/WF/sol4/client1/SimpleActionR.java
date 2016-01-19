package it.polito.dp2.WF.sol4.client1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionType;

/**
 * This is a concrete implementation of abstract class AbstractActionReader 
 * (that implements the interface ActionReader) based on JAX-WS framework.<BR>
 * Another implementation of that abstract class is {@link ProcessActionR}<br>
 * 
 * @see {@link ActionReader}, {@link AbstractActionReader}, {@link ProcessActionR}
 * @author Luca
 */
public class SimpleActionR extends AbstractActionReader implements SimpleActionReader {

	private HashMap<String, ActionReader> nextPossibleActions;

	public SimpleActionR(ActionType action, WorkflowReader workflowReader) {
		super(action, workflowReader);
		nextPossibleActions = new HashMap<String, ActionReader>();
		
		if(action.getSimpleAction() == null)
			System.err.println("Error! The simpleAction is null... Something wrong happens!\n");
		
	}

	@Override
	public Set<ActionReader> getPossibleNextActions() {
		return new TreeSet<ActionReader>(nextPossibleActions.values());
	}
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("NextActions: ");
		for(ActionReader ar : nextPossibleActions.values())
			buf.append(ar.getName());
		
		return super.toString()+"\n\t\t\t"+buf.toString();
	}

	/**
	 * This method, given all the {@link ActionType} and all the {@link ActionReader} of a certain workflow,
	 * will set al the possible next actions inside this action.<br>
	 * <b>Important:</b> this action <b>MUST</b> be present inside the "actions" Map!
	 * 
	 * @param nextActions - A List of {@link ActionType} (other elements will be ignored).
	 * @param actions - A {@link Map} containing all the {@link ActionReader} of one {@link WorkflowReader} element.
	 */
	public void setPossibleNextActions(List<Object> nextActions, Map<String,ActionReader> actions) {
		for(Object o : nextActions)
		{
			if(o instanceof ActionType)
			{	
				ActionType action = (ActionType) o;
				ActionReader nextAction = actions.get(action.getName());
				
				if(nextAction == null)
					System.err.println("Error! This action is not present inside the actions Map!");
				else
					nextPossibleActions.put(nextAction.getName(), nextAction);
			}
			else {
				System.err.println("Error! An object is not an ActionType! It will be ignored");
			}
		}
	}

}
