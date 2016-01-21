package it.polito.dp2.WF.sol4.client1;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.Workflow;

/**
 * This is a concrete implementation of the interface {@link WorkflowReader} based on the JAX-WS framework.
 * 
 * @author Luca
 */
public class ConcreteWorkflowReader implements WorkflowReader, Comparable<WorkflowReader> {

	private String name;
	private Map<String, ActionReader> actionReaderMap;
	private Set<ProcessReader> processes;
	
	public ConcreteWorkflowReader(Workflow workflow) {
		actionReaderMap = new HashMap<String, ActionReader>();
		processes = new TreeSet<ProcessReader>();
		
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
	
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Workflow Name: "+name+"\n");
			
		buf.append("\tActions:\n");
		for(ActionReader ar : actionReaderMap.values()) {
			buf.append("\t\t"+ar.toString()+"\n");
		}
		
		buf.append("\tProcesses:\n");
		if(processes.isEmpty()){
			buf.append("\t\t No Processes \n");
		}
		else {
			for(ProcessReader pr : processes) {
				if(pr instanceof ConcreteProcessReader) {
					ConcreteProcessReader cpr = (ConcreteProcessReader) pr;
					buf.append("\t\t"+cpr.toShortString()+"\n");
				}
				else {
					buf.append("\t\t"+pr.toString()+"\n");
				}
			}
		}
		
		return buf.toString();
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

	/**
	 * 	/**
	 * This method add a {@link ProcessReader} to this {@link WorkflowReader}.
	 * It is necessary that the process is an instance of this workflow.
	 * If the object is not the same, the method want that the workflow name is the same.
	 * 
	 * @param pr - The processe that you want to add to this workflow.
	 * @return <code>True</code> if the insertion happens, <code>false</code> otherwise.
	 */
	public boolean setProcesses(ProcessReader pr) {
		if( (pr.getWorkflow() == this) || (this.compareTo(pr.getWorkflow()) == 0) ) {
			return processes.add(pr);
		}
		else
			return false;
	}

}
