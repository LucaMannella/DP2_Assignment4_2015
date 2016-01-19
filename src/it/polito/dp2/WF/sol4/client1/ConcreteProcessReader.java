package it.polito.dp2.WF.sol4.client1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.Process;

/**
 * This is a concrete implementation of the interface ProcessReader based on the JAX-WS framework.
 * 
 * @see {@link WorkflowReader}
 * @author Luca
 */
public class ConcreteProcessReader implements ProcessReader, Comparable<ProcessReader> {

	private Calendar startTime;
	private List<ActionStatusReader> statusActions;
	private WorkflowReader myWorkflow;

	public ConcreteProcessReader(Process process, WorkflowReader myWF) {
		this.myWorkflow = myWF;
//TODO:	if(process == null) return;	//safety lock
		this.startTime = process.getStarted().toGregorianCalendar();
		
		statusActions = new LinkedList<ActionStatusReader>();
		for( ActionStatusType action : process.getActionStatus() ) {
			ActionStatusReader asr = new ConcreteActionStatusReader(action, myWorkflow.getName());
			statusActions.add(asr);
		}
	}

	@Override
	public Calendar getStartTime() {
		return this.startTime;
	}

	@Override
	public List<ActionStatusReader> getStatus() {
		return this.statusActions;
	}

	@Override
	public WorkflowReader getWorkflow() {
		return this.myWorkflow;
	}
	
	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		
		StringBuffer buf = new StringBuffer("Process related to workflow <"+myWorkflow.getName()+"> ");
		buf.append("started at <"+dateFormat.format(startTime.getTimeInMillis())+">\n");
		
		for(ActionStatusReader asr : statusActions) {
			buf.append("\t"+asr.toString()+"\n");
		}
		return buf.toString();
	}
	
	/**
	 * This method gives a short version of the toString method.
	 * 
	 * @return A shorted version of the toStrin method.
	 */
	public String toShortString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		
		StringBuffer buf = new StringBuffer("Process started at ");
		buf.append( dateFormat.format(startTime.getTimeInMillis()) );
		buf.append(" with "+statusActions.size()+" ActionStatus.");
		
		return buf.toString();
	}

	/**
	 * The comparison is based on the starting time. 
	 * If the times are equal, it is based on the name of the relative workflows.
	 */
	@Override
	public int compareTo(ProcessReader o) {
		int toRet = startTime.compareTo(o.getStartTime());
		if(toRet == 0)
			toRet = myWorkflow.getName().compareTo(o.getWorkflow().getName());
		
		return toRet;
	}
}
