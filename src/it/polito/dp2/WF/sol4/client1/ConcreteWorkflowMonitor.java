package it.polito.dp2.WF.sol4.client1;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowMonitorException;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;
import it.polito.dp2.WF.sol4.gen.WorkflowService;

/**
 * This is a concrete implementation of the interface WorkflowMonitor based on the JAX-WS framework.<BR><BR>
 * If you want more detail about the interface look to {@link it.polito.dp2.WF.WorkflowMonitor}
 * 
 * @author Luca
 */
public class ConcreteWorkflowMonitor implements WorkflowMonitor {
	
	private static final String SYS_PROP_URL = "it.polito.dp2.WF.lab4.URL";
	private static final String DEFAUL_WS_URL = "http://localhost:7071/wfinfo";
	
	private WorkflowInfoInterface proxyReader;
	
	private Map<String, WorkflowReader> workflows;
	private HashSet<ProcessReader> processes;
	private XMLGregorianCalendar lastUpdateTime;
	
	/**
	 * This method creates a map of {@link WorkflowMonitor} taking the data from SOAP a Web Service.
	 * 
	 * @throws WorkflowMonitorException If it is not possible to instantiate the {@link ConcreteWorkflowMonitor}
	 * @throws MalformedURLException	If the read URL is malformed.
	 */
	public ConcreteWorkflowMonitor() throws MalformedURLException, WorkflowMonitorException {
		
		// taking the URL of the Web Service
		String webServiceURLString = System.getProperty(SYS_PROP_URL);
		if(webServiceURLString == null)
			webServiceURLString = DEFAUL_WS_URL;
		
		// creating the URL object
		URL webServiceURL = new URL(webServiceURLString);
		
		// taking the port: WorkflowInfoPort (proxy) from the service
		WorkflowService service = new WorkflowService(webServiceURL);
		proxyReader = service.getWorkflowInfoPort();
		
		// building the maps
		System.out.println("...Building the WorkflowMonitor...");
		buildWorkflowReaders();
		System.out.println(workflows.size()+" workflows were created.");
		buildProcessReaders();
		System.out.println(processes.size()+" processes were created.");
	}
	
	/**
	 * This method creates a map of {@link WorkflowReader} object taking the data from SOAP a Web Service.
	 * 
	 * @throws WorkflowMonitorException If something wrong or unexpected happens.
	 */
	private void buildWorkflowReaders() throws WorkflowMonitorException {		
		workflows = new HashMap<String, WorkflowReader>();
		
		Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();
		Holder<List<String>> workflowNamesHolder = new Holder<List<String>>();
		proxyReader.getWorkflowNames(calendarHolder, workflowNamesHolder);
		
		Holder<List<Workflow>> workflowsHolder = new Holder<List<Workflow>>();
		System.out.println("...Retrieving the workflows...");
		try {
			proxyReader.getWorkflows(workflowNamesHolder.value, calendarHolder, workflowsHolder);
			lastUpdateTime = calendarHolder.value;
		} catch (UnknownNames_Exception e) {
			throw new WorkflowMonitorException("Error retrieving the workflows: "+e.getMessage());
		}
		
		// build the WorkflowReader Map
		for( Workflow wf: workflowsHolder.value ) {
			WorkflowReader wfr = new ConcreteWorkflowReader(wf);
			workflows.put(wfr.getName(), wfr);
		}
		// this loop is to managing the ProcessActions	//TODO: fix here!
		for( WorkflowReader wf : workflows.values() ) {
			if(wf instanceof ConcreteWorkflowReader)
				((ConcreteWorkflowReader)wf).setWfsInsideProcessActions(workflows);
		}
	}

	/**
	 * This method creates a map of {@link ProcessReader} object taking the data from SOAP a Web Service.
	 * 
	 * @throws WorkflowMonitorException If something wrong or unexpected happens.
	 */
	private void buildProcessReaders() throws WorkflowMonitorException {
		processes = new HashSet<ProcessReader>();
		
		Holder<List<String>> wokflowNamesHolder = new Holder<List<String>>();
		Holder<XMLGregorianCalendar> calendarHolder = new Holder<XMLGregorianCalendar>();
		proxyReader.getWorkflowNames(calendarHolder, wokflowNamesHolder);
		
		// if the workflows have been updated I re-build them
		if(calendarHolder.value.compare(lastUpdateTime) == DatatypeConstants.GREATER)
			buildWorkflowReaders();
		
		Holder<List<Process>> processesHolder = new Holder<List<Process>>();
		System.out.println("...Retrieving the processes...");
		try {
			proxyReader.getProcesses(wokflowNamesHolder.value, calendarHolder, processesHolder);
			lastUpdateTime = calendarHolder.value;
		} catch (UnknownNames_Exception e) {
			throw new WorkflowMonitorException("Error retrieving the processes: "+e.getMessage());
		}
		
		// build the ProcessReader Map
		for( Process p : processesHolder.value ) {
			WorkflowReader wfr = workflows.get(p.getWorkflow());
			ProcessReader pr = new ConcreteProcessReader(p, wfr);
			processes.add(pr);
		}
	}

	@Override
	public Set<ProcessReader> getProcesses() {
		return this.processes;
	}

	@Override
	public WorkflowReader getWorkflow(String name) {
		return workflows.get(name);
	}

	@Override
	public Set<WorkflowReader> getWorkflows() {
		return new LinkedHashSet<WorkflowReader>(workflows.values());
	}

	@Override
	public String toString(){
		StringBuffer buf = new StringBuffer("Inside this WorkflowMonitor there are:\n");
		
		if((workflows==null) || (workflows.isEmpty()))
			buf.append("\tNo Workflows\n");
		else {
			for(WorkflowReader wfr : workflows.values())
				buf.append("\t"+wfr.toString()+"\n");
		}
		
		if((processes==null) || (processes.isEmpty()))
			buf.append("\tNo Processes\n");
		else {
			buf.append("Processes:\n");
			for(ProcessReader pr : processes)
				buf.append("\t"+pr.toString()+"\n");
		}
		
		return buf.toString();
	}
}
