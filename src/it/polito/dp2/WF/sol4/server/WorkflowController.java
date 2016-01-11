package it.polito.dp2.WF.sol4.server;

import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.UnknownActionName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownActorName_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownCode_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownNextActionName_Exception;
import it.polito.dp2.WF.sol4.gen.WorkflowControllerInterface;

public class WorkflowController implements WorkflowControllerInterface {

	@Override
	public boolean createNewProcess(String wfName) throws UnknownNames_Exception {
		
		// TODO Implement me
		
		return false;
	}

	@Override
	public boolean takeOverAction(String psCode, Actor actor)
			throws UnknownActorName_Exception, UnknownCode_Exception {
		
		// ---	This method is not required by the specifications.	--- //
		return false;
	}

	@Override
	public boolean completeAction(String actionStatusName, String nextActionName)
			throws UnknownActionName_Exception, UnknownNextActionName_Exception {
		
		// ---	This method is not required by the specifications.	--- //
		return false;
	}

}
