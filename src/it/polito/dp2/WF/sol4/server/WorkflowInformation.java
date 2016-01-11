package it.polito.dp2.WF.sol4.server;

import java.util.List;

import it.polito.dp2.WF.lab4.gen.ActionStatusType;
import it.polito.dp2.WF.lab4.gen.Process;
import it.polito.dp2.WF.lab4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.lab4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.lab4.gen.Workflow;
import it.polito.dp2.WF.lab4.gen.WorkflowInfoInterface;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceFeature;

public class WorkflowInformation extends WebServiceFeature implements
		WorkflowInfoInterface {

	@Override
	public void getWorkflowNames(Holder<XMLGregorianCalendar> lastModTime,
			Holder<List<String>> name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getWorkflows(List<String> wfName,
			Holder<XMLGregorianCalendar> lastModTime,
			Holder<List<Workflow>> workflow) throws UnknownNames_Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void getProcesses(List<String> wfName,
			Holder<XMLGregorianCalendar> lastModTime,
			Holder<List<Process>> process) throws UnknownNames_Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ActionStatusType> getActions(String psCode)
			throws UnknownCode_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
