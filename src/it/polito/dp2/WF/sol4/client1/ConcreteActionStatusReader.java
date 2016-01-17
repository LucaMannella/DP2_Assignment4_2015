package it.polito.dp2.WF.sol4.client1;

import java.util.Calendar;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.sol4.gen.ActionStatusType;

public class ConcreteActionStatusReader implements ActionStatusReader {

	public ConcreteActionStatusReader(ActionStatusType action, String workflowName) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getActionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Actor getActor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Calendar getTerminationTime() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTakenInCharge() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

}
