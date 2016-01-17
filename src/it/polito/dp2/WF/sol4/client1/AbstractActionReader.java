package it.polito.dp2.WF.sol4.client1;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionType;

/**
 * This is an abstract implementation of the interface ActionReader based on JAX-WS framework.<BR>
 * If you want to use that class you have to instantiate one of the following implementation.<BR>
 * {@link it.polito.dp2.WF.sol4.client1.SimpleActionR}<BR>{@link it.polito.dp2.WF.sol4.client1.ProcessActionR}<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.ActionReader}
 * 
 * @author Luca
 */
public abstract class AbstractActionReader implements ActionReader {
	
	private String id;
	private String name;
	private String role;
	private boolean automInst;
	private WorkflowReader parent;

	public AbstractActionReader(ActionType action, WorkflowReader workflowReader) {
//TODO:	if(action == null)	return;
		this.id = action.getId();
		this.name = action.getName();
		this.role = action.getRole();
		this.automInst = action.isAutomInst();
		this.parent = workflowReader;
	}
	
	@Override
	public WorkflowReader getEnclosingWorkflow() {
		return this.parent;
	}
	
	public String getId() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getRole() {
		return this.role;
	}

	@Override
	public boolean isAutomaticallyInstantiated() {
		return automInst;
	}
	
	@Override
	public String toString() {
		return "\tAction: "+name+" - Requested Role: "+role+" - Parent workflow: "+parent.getName()+" - AutomInst: "+automInst;
	}

}
