package it.polito.dp2.WF.sol4.server;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.Workflow;

/**
 * This class must be thread safe!
 * 
 * @author Luca
 */
public class WorkflowDataManager {
	
	private static Logger log = Logger.getLogger(WorkflowDataManager.class.getName());
	
	private Map<String, Workflow> workflowMap = null;
	private Map<String, Process> processMap = null;
	int pCode = 0;
	
	private List<String> workflowNames;
	
	private XMLGregorianCalendar lastWorkflowUpdate;
	private XMLGregorianCalendar lastProcessUpdate;

	public WorkflowDataManager(WorkflowMonitor wfMonitor) {
		// TODO Auto-generated constructor stub
		ObjectFactory objFactory = new ObjectFactory();
		
		workflowMap = new ConcurrentHashMap<String, Workflow>();
		processMap = new ConcurrentHashMap<String, Process>();
		
		workflowNames = new CopyOnWriteArrayList<>();
		
		for( WorkflowReader wfr : wfMonitor.getWorkflows() ) {
			Workflow wf = objFactory.createWorkflow();
			wf.setName(wfr.getName());
			
			for( ActionReader ar : wfr.getActions() ) {
				
			}
		}
	}

	public List<String> getWorkflowNames() {		// TODO Auto-generated method stub
		return this.workflowNames;
	}

	public XMLGregorianCalendar getLastWorkflowsUpdate() {		// TODO Auto-generated method stub
		return this.lastWorkflowUpdate;
	}

	public List<Workflow> getWorkflows(List<String> wfNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public XMLGregorianCalendar getLastProcessesUpdate() {
		return this.lastProcessUpdate;
	}

	public List<Process> getProcesses(List<String> wfNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public Process getProcess(String psCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public Workflow getWorkflow(String wfName) {
		// TODO Auto-generated method stub
		return null;
	}

}
