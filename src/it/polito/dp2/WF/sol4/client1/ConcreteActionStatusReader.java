package it.polito.dp2.WF.sol4.client1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.Actor;
import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ActionType;

/**
 * This is a concrete implementation of the interface {@link ActionStatusReader} based on the JAX-WS framework.
 * 
 * @author Luca
 */
public class ConcreteActionStatusReader implements ActionStatusReader, Comparable<ActionStatusReader> {

	private String name;
	private Actor actor;
	private Calendar terminationTime;
	private boolean takenInCharge;
	private boolean terminated;
	
	public ConcreteActionStatusReader(ActionStatusType action, String workflowName) {
//TODO:	if((action == null) return;	//safety lock
		String actorRole = null;
		
		if( action.getAction() instanceof ActionType ) {
			ActionType azione = (ActionType) action.getAction();
			
			// retrieving the action name
			this.name = azione.getName().replace(workflowName+"_", "");
			// retrieving the role related to the actor
			actorRole = azione.getRole();
		}
		else
			System.err.println("\n Error! The IDREF does not refer to an ActionType! \n"
					+ "It is impossible to set the name of the ActionStatusReader and the role of the Actor!");
		
		this.takenInCharge = action.isTakenInCharge();
		this.terminated = action.isTerminated();
		this.terminationTime = new GregorianCalendar();
			terminationTime.setTimeInMillis(0);			//default value
		
		if(takenInCharge) {
			String actorName = action.getActor();
			this.actor = new Actor(actorName, actorRole);
		}
		if(terminated) {
			this.terminationTime = action.getTimestamp().toGregorianCalendar();
		}
	}

	@Override
	public String getActionName() {
		return this.name;
	}

	@Override
	public Actor getActor() {
		return this.actor;
	}

	@Override
	public Calendar getTerminationTime() {
		return this.terminationTime;
	}

	@Override
	public boolean isTakenInCharge() {
		return this.takenInCharge;
	}

	@Override
	public boolean isTerminated() {
		return this.terminated;
	}

	@Override
	public String toString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
		
		StringBuffer buf = new StringBuffer("Action name: "+this.name);
		if(takenInCharge) {
			buf.append(" - taken in charge by: "+actor.getName());
			if(terminated)
				buf.append(" - terminated at: "+dateFormat.format(terminationTime.getTimeInMillis()));
			else
				buf.append(" - not yet terminated");
		}
		else
			buf.append(" - not taken in charge by anyone");

		return buf.toString();
	}

	@Override
	public int compareTo(ActionStatusReader o) {
		if( terminated && (o.isTerminated()) )
			return terminationTime.compareTo(o.getTerminationTime());
		else if( terminated && (!o.isTerminated()) )
			return -1;
		else if( (!terminated) && (o.isTerminated()) )
			return 1;
		else if( takenInCharge && (!o.isTakenInCharge()))
			return -1;
		else if( (!takenInCharge) && (o.isTakenInCharge()) )
			return 1;
		else
			return 0;
	}
	
}
