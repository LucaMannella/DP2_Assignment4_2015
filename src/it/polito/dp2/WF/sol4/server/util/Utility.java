package it.polito.dp2.WF.sol4.server.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.Workflow;

public class Utility {

	/**
	 * This method create a Map of {@link Workflow} starting from a Set.
	 * The name of the workflow will be the key of the Map.
	 * 
	 * @param workflowsSet
	 * @return A workflow {@link HashMap} that use the workflow's name as key
	 */
	public static Map<String, Workflow> buildWFMap(Set<Workflow> workflowsSet) {
		Map<String, Workflow> workflowsMap = new HashMap<String, Workflow>();
		for(Workflow wf : workflowsSet) {
			workflowsMap.put(wf.getName(), wf);
		}
		return workflowsMap;
	}
	
	/**
	 * This method create a Map of {@link ActionType} starting from a List.
	 * The name of Action will be the key of the Map.
	 * 
	 * @param actionsList
	 * @return An ActionType HashMap that use the action's name as key
	 */
	public static Map<String, ActionType> buildWFActionsMap(List<ActionType> actionsList) {
		Map<String, ActionType> wfActionsMap = new HashMap<String, ActionType>();
		
		for(ActionType act : actionsList) {
			wfActionsMap.put(act.getName(), act);
		}
		return wfActionsMap;
	}
}
