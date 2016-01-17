package it.polito.dp2.WF.sol4.client1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.Workflow;

public class ConcreteWorkflowReader implements WorkflowReader {

	public ConcreteWorkflowReader(Workflow wf) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ActionReader getAction(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ActionReader> getActions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setWfsInsideProcessActions(Map<String, WorkflowReader> workflows) {
		// TODO Auto-generated method stub
		
	}

}
