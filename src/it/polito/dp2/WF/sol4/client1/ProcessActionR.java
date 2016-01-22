package it.polito.dp2.WF.sol4.client1;

import java.util.Map;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.ActionType.ProcessAction;

/**
 * This is a concrete implementation of abstract class {@link AbstractActionReader} (that implements the interface ActionReader).<BR>
 * Another implementation of that abstract class is {@link SimpleActionR}<br>
 * This implementation is based on JAX-WS framework.
 *
 * @see {@link ActionReader}, {@link AbstractActionReader}, {@link SimpleActionR}
 * @author Luca
 */
public class ProcessActionR extends AbstractActionReader implements ProcessActionReader {

	private String workflowName;
	private WorkflowReader nextWorkflow;

	public ProcessActionR(ActionType action, WorkflowReader workflowReader) {
		super(action, workflowReader);

		if(action == null)	return;
		
		ProcessAction processAction = action.getProcessAction();
		if(processAction == null) {
			System.err.println("Error! The processAction is null... Something wrong happens!\n");
			return;
		}
		workflowName = processAction.getNextProcess();		
	}

	@Override
	public WorkflowReader getActionWorkflow() {
		return this.nextWorkflow;
	}
	
	@Override
	public String toString() {
		return super.toString()+"\n\t\tNextWorkflow: "+nextWorkflow.getName();
	}

	public void setNextWorkflow(Map<String,WorkflowReader> workflows) {
		nextWorkflow = workflows.get(workflowName);
	}

}
