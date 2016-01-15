package it.polito.dp2.WF.sol4.server;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;

@WebService(name = "WorkflowInfoInterface", 
			targetNamespace = "http://lucamannella.altervista.org/WorkflowManager/",
			serviceName = "WorkflowService",
			portName = "WorkflowInfoPort",
			endpointInterface = "it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface")
@XmlSeeAlso({ObjectFactory.class})
public class WorkflowInformation implements WorkflowInfoInterface {
	
	private WorkflowDataManager manager;
	private static Logger log = Logger.getLogger(WorkflowInformation.class.getName());

	public WorkflowInformation(WorkflowDataManager wfManager) {	// TODO Auto-generated constructor stub
		
		this.manager = wfManager;
	}

	@WebMethod
	@Override
	public void getWorkflowNames(Holder<XMLGregorianCalendar> lastModTime, Holder<List<String>> names) {	//TODO: Check me
		log.entering(log.getName(), "getWorkflowNames");
		
		names.value = manager.getWorkflowNames();
		
		GregorianCalendar cal = manager.getLastWorkflowsUpdate();
		try {
			lastModTime.value = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
			//startTime = new XMLGregorianCalendarImpl(cal);
		}
		
		log.exiting(log.getName(), "getWorkflowNames", lastModTime.value.toString());
	}

	@WebMethod
	@Override
	public void getWorkflows(List<String> wfNames, Holder<XMLGregorianCalendar> lastModTime, Holder<List<Workflow>> workflows) 
			throws UnknownNames_Exception {		//TODO: Check me
		log.entering(log.getName(), "getWorkflows", wfNames.toString());
		
		workflows.value = manager.getWorkflows(wfNames);
		
		GregorianCalendar cal = manager.getLastWorkflowsUpdate();
		try {
			lastModTime.value = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
			//startTime = new XMLGregorianCalendarImpl(cal);
		}
		
		log.exiting(log.getName(), "getWorkflows", lastModTime.value.toString());
	}

	@WebMethod
	@Override
	public void getProcesses(List<String> wfNames, Holder<XMLGregorianCalendar> lastModTime, Holder<List<Process>> processes) 
			throws UnknownNames_Exception {		//TODO: Check me
		log.entering(log.getName(), "getProcesses", wfNames.toString());
		
		processes.value = manager.getProcesses(wfNames);
		
		GregorianCalendar cal = manager.getLastProcessesUpdate();
		try {
			lastModTime.value = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
			//startTime = new XMLGregorianCalendarImpl(cal);
		}
		
		log.exiting(log.getName(), "getProcesses", lastModTime.value.toString());
	}

	@WebMethod
	@Override
	public List<ActionStatusType> getActions(String psCode) throws UnknownCode_Exception {
		// ---	This method is not required by the specifications.	--- //
		log.entering(log.getName(), "getActions", psCode);
		
		Process p = manager.getProcess(psCode);
		if(p==null) {
			String errorMessage = "The code \""+psCode+"\" is wrong written or does not exists in the structure.";
			UnknownCode fault = new UnknownCode();
			fault.setMessage(errorMessage);
			throw new UnknownCode_Exception(errorMessage, fault);
		}
		
		log.exiting(log.getName(), "getActions", p.getActionStatus().toString());
		return p.getActionStatus();
	}

}
