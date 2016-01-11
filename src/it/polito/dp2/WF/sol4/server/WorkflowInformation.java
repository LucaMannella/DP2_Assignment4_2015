package it.polito.dp2.WF.sol4.server;

import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WorkflowInfoInterface;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

public class WorkflowInformation implements WorkflowInfoInterface {

	@Override
	public void getWorkflowNames(Holder<XMLGregorianCalendar> lastModTime, Holder<List<String>> name) {
		
		// TODO Implement me

	}

	@Override
	public void getWorkflows(List<String> wfName, Holder<XMLGregorianCalendar> lastModTime, Holder<List<Workflow>> workflow) 
			throws UnknownNames_Exception {
		
		// TODO Implement me

	}

	@Override
	public void getProcesses(List<String> wfName, Holder<XMLGregorianCalendar> lastModTime,	Holder<List<Process>> process) 
			throws UnknownNames_Exception {
		
		// TODO Implement me

	}

	@Override
	public List<ActionStatusType> getActions(String psCode) throws UnknownCode_Exception {
		// ---	This method is not required by the specifications.	--- //
		return null;
	}

}
