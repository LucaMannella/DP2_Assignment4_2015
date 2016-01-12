package it.polito.dp2.WF.sol4.wsdlGeneration;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.WorkflowReader;

@WebService(endpointInterface="it.polito.dp2.WF.sol4.wsdlGeneration.WorkflowService")
public class ConcreteWorkflowService implements WorkflowService {

	@Override
	public List<String> getWorkflowNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, WorkflowReader> getWorkflows(List<String> workflowNames) throws UnknownWorkflow {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProcessReader> getProcesses(List<String> workflowNames) throws UnknownWorkflow {
		// TODO Auto-generated method stub
		return null;
	}

}
