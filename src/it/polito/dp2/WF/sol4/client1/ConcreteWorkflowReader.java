package it.polito.dp2.WF.sol4.client1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.Workflow;

/**
 * This is a concrete implementation of the interface WorkflowReader based on the JAX-WS framework.
 * 
 * @see {@link WorkflowReader}
 * @author Luca
 */
public class ConcreteWorkflowReader implements WorkflowReader, Comparable<WorkflowReader> {

	private String name;
	private Map<String, ActionReader> actionReaderMap;
	private Set<ProcessReader> processes;
	
	public ConcreteWorkflowReader(Workflow workflow) {
		actionReaderMap = new HashMap<String, ActionReader>();
		processes = new HashSet<ProcessReader>();
		
//TODO:	if(workflow == null) return;	//safety lock
		this.name = workflow.getName();
		
		// set the actions inside the object
		for( ActionType azione : workflow.getAction() ){
			ActionReader ar;
			if(azione.getSimpleAction() != null) {
				//it's a simple action
				if (azione.getProcessAction() != null)
					System.err.println("Error! The action has simpleAction and processAction set (it will be treated as simple)\n");
				
				ar = new SimpleActionR(azione, this);
				actionReaderMap.put(ar.getName(), ar);
			}
			else if((azione.getProcessAction() != null) && (azione.getSimpleAction() == null)) {
				//it's a process action
				ar = new ProcessActionR(azione, this);
				actionReaderMap.put(ar.getName(), ar);
			}
			else {
				System.err.println("Error! The action has no simpleAction and no processAction set.\n");
			}
		}
		// This loop is to managing the SimpleActions
		for( ActionType azione : workflow.getAction() ) {
			ActionReader actReader = actionReaderMap.get(azione.getName());
			
			if(actReader instanceof SimpleActionR) {
				List<Object> nextActions = azione.getSimpleAction().getNextActions();
				
				SimpleActionR sar = (SimpleActionR)actReader;
				sar.setPossibleNextActions(nextActions, actionReaderMap);
			}
			else if(actReader instanceof ProcessActionR == false)
				System.err.println("Error! The actionReader: "+actReader.getName()+" has un unknown type!");
			
			//The nextWorkflow for processAction will be set by th WorkflowMonitor
		}
	}

	@Override
	public ActionReader getAction(String actionName) {
		return actionReaderMap.get(actionName);
	}

	@Override
	public Set<ActionReader> getActions() {
		return new LinkedHashSet<ActionReader>(actionReaderMap.values());
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return processes;
	}
	
	@Override
	public int compareTo(WorkflowReader o) {
		return this.name.compareTo(o.getName());
	}

	/**
	 * This method set inside each {@link ProcessActionReader} of this {@link WorkflowReader}
	 * the Workflow that will be instantiated after that this action will be completed.
	 *  
	 * @param workflows - All the workflows of the {@link WorkflowMonitor}
	 */
	public void setWfsInsideProcessActions(Map<String, WorkflowReader> workflows) {
		for( ActionReader actReader : actionReaderMap.values() ) {
			if(actReader instanceof ProcessActionR) {
				
				ProcessActionR par = (ProcessActionR)actReader;
				par.setNextWorkflow(workflows);
			}
		}
	}

}
