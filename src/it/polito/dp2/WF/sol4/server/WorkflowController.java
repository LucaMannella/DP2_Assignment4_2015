package it.polito.dp2.WF.sol4.server;

import it.polito.dp2.WF.lab4.gen.Actor;
import it.polito.dp2.WF.lab4.gen.UnknownActionName_Exception;
import it.polito.dp2.WF.lab4.gen.UnknownActorName_Exception;
import it.polito.dp2.WF.lab4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.lab4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.lab4.gen.UnknownNextActionName_Exception;
import it.polito.dp2.WF.lab4.gen.WorkflowControllerInterface;

import javax.xml.ws.WebServiceFeature;

public class WorkflowController extends WebServiceFeature implements
		WorkflowControllerInterface {

	@Override
	public boolean createNewProcess(String wfName)
			throws UnknownNames_Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean takeOverAction(String psCode, Actor actor)
			throws UnknownActorName_Exception, UnknownCode_Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean completeAction(String actionStatusName, String nextActionName)
			throws UnknownActionName_Exception, UnknownNextActionName_Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

}
