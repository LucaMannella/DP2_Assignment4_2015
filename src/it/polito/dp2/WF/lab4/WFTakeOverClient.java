package it.polito.dp2.WF.lab4;

public interface WFTakeOverClient {
	/**
	 * Tries to take over an action with the specified name in a process
	 * belonging to the specified workflow, by an actor with the specified name.
	 * If there is more than one action that can be taken over with the
	 * specified features, only one of them is taken over, no matter which one.
	 * @param workflowName the name of the workflow in which we want to take over an action
	 * @param actionName the name of the action we want to take over
	 * @param actorname the name of the actor who wants to take over the action
	 * @return true if the action has been taken over, false if there is no action that can be taken over with the specified parameters
	 * @throws ServiceUnavailableException if the operation cannot be completed because of problems with the web service (e.g. non-reachable service)
	 */
	boolean takeOver(String workflowName, String actionName, String actorName) throws ServiceUnavailableException;
}
