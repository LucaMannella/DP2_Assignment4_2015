package it.polito.dp2.WF.sol4.server;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.Workflow;

public class Utility {

	/**
	 * This method create a Map of {@link Workflow} starting from a Set.
	 * The name of the workflow will be the key of the Map.
	 * 
	 * @param workflowsSet
	 * @return A workflow {@link HashMap} that use the workflow's name as key
	 */
	public static Map<String, Workflow> buildWFMap(Set<Workflow> workflowsSet) {
		Map<String, Workflow> workflowsMap = new HashMap<String, Workflow>();
		for(Workflow wf : workflowsSet) {
			workflowsMap.put(wf.getName(), wf);
		}
		return workflowsMap;
	}
	
	/**
	 * This method create a Map of {@link ActionType} starting from a List.
	 * The name of Action will be the key of the Map.
	 * 
	 * @param actionsList
	 * @return An ActionType HashMap that use the action's name as key
	 */
	public static Map<String, ActionType> buildWFActionsMap(List<ActionType> actionsList) {
		Map<String, ActionType> wfActionsMap = new HashMap<String, ActionType>();
		
		for(ActionType act : actionsList) {
			wfActionsMap.put(act.getName(), act);
		}
		return wfActionsMap;
	}
	
	/**
	 * This method create a {@link XMLGregorianCalendar} instance with the current timestamp.
	 * 
	 * @return A {@link XMLGregorianCalendar} instance
	 */
	public static XMLGregorianCalendar createXMLGregCalendar() {
		GregorianCalendar cal = new GregorianCalendar();
		XMLGregorianCalendar startTime = null;
		
		try {
			startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		}
		catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return startTime;
	}
	
	/**
	 * This method create a {@link XMLGregorianCalendar} instance with the same time of the calendar given as parameter.
	 * 
	 * @param calendar - An instance of {@link Calendar} interface.
	 * @return A {@link XMLGregorianCalendar} instance
	 */
	public static XMLGregorianCalendar createXMLGregCalendar(Calendar calendar) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(calendar.getTimeInMillis());
		
		XMLGregorianCalendar startTime = null;
		try {
			startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		}
		catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		return startTime;
	}
}
