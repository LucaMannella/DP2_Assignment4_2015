package it.polito.dp2.WF.sol4.server;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.polito.dp2.WF.ActionReader;
import it.polito.dp2.WF.ActionStatusReader;
import it.polito.dp2.WF.ProcessActionReader;
import it.polito.dp2.WF.ProcessReader;
import it.polito.dp2.WF.SimpleActionReader;
import it.polito.dp2.WF.WorkflowMonitor;
import it.polito.dp2.WF.WorkflowReader;
import it.polito.dp2.WF.sol4.gen.ActionStatusType;
import it.polito.dp2.WF.sol4.gen.ActionType;
import it.polito.dp2.WF.sol4.gen.ActionType.SimpleAction;
import it.polito.dp2.WF.sol4.gen.Actor;
import it.polito.dp2.WF.sol4.gen.ErrorMessage;
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownCode;
import it.polito.dp2.WF.sol4.gen.UnknownNames;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.UnknownWorkflow;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.gen.WrongAction;
import it.polito.dp2.WF.sol4.gen.WrongActor;

/**
 * The WorkflowDataManager is the class that manages the data used by the WebService.
 * This class must be thread safe!
 * 
 * @author Luca
 */
public class WorkflowDataManager {
	
	private static Logger log = Logger.getLogger(WorkflowDataManager.class.getName());
	
	private Map<String, Workflow> workflowMap = null;
	private Map<String, Process> processMap = null;
	private int intCode = 0;
	
	private List<String> workflowNames;
	
	private XMLGregorianCalendar lastWorkflowUpdate;
	private XMLGregorianCalendar lastProcessUpdate;

	private ObjectFactory objFactory;

	
	/**
	 * This method returns a {@link List} containing all the name of the workflows that are inside the manager.
	 * @return a {@link List&ltString&gt}
	 */
	public List<String> getWorkflowNames() {
		return this.workflowNames;
	}

	public List<Workflow> getWorkflows(List<String> wfNames) throws UnknownNames_Exception {
		List<String> wrongNames = new LinkedList<String>();
		List<Workflow> workflows = new LinkedList<Workflow>();
		
		for(String name : wfNames) {
			Workflow wf = workflowMap.get(name);
			if(wf == null)
				wrongNames.add(name);
			else
				workflows.add(wf);
		}
		
		if(wrongNames.isEmpty()) {
			return workflows;
		}
		else {
			String errorMessage = "Error! Some names given as parameter are wrong or do not exist!";
			
			UnknownNames faultInfo = objFactory.createUnknownNames();
			faultInfo.setMessage(errorMessage);
			faultInfo.getName().addAll(wrongNames);
			
			throw new UnknownNames_Exception(errorMessage, faultInfo);
		}
	}

	/**
	 * This method returns a {@link GregorianCalendar}
	 * representing the last time that a Workflow was created or modified.
	 * 
	 * @return A {@link GregorianCalendar}
	 */
	public XMLGregorianCalendar getLastWorkflowsUpdate() {
		return this.lastWorkflowUpdate;
	}

	/**
	 * This method returns a {@link GregorianCalendar}
	 * representing the last time that a Process was created or modified.
	 * 
	 * @return A {@link GregorianCalendar}
	 */
	public XMLGregorianCalendar getLastProcessesUpdate() {
		return this.lastProcessUpdate;
	}

	public List<Process> getProcesses(List<String> wfNames) throws UnknownNames_Exception {
		List<String> wrongNames = new LinkedList<String>();
		
		List<Process> processes = new LinkedList<Process>();
		Collection<Process> processesCollection = processMap.values();
		
		for(String name : wfNames) {
			if(workflowMap.containsKey(name) == false)
				wrongNames.add(name);
		}
		if(wrongNames.isEmpty() == false) {
			String errorMessage = "Error! Some names given as parameter are wrong or do not exist!";
			
			UnknownNames faultInfo = objFactory.createUnknownNames();
			faultInfo.setMessage(errorMessage);
			faultInfo.getName().addAll(wrongNames);
			
			throw new UnknownNames_Exception(errorMessage, faultInfo);
		}
		
		for(String name : wfNames) {
			for(Process p : processesCollection) {
		// looking for a matching between the list of name and the workflow of the process
				if( p.getWorkflow().equals(name) )
					processes.add(p);
			}
		}	
			
		return processes;
	}

	/**
	 * This method returns a {@link Process} given its ProcessCode.
	 * If the process does not exists <code>null</code>} is returned.
	 * 
	 * @param psCode - The code of the process inside the manager.
	 * @return A {@link Process} object if the code exists, <code>null</code> otherwise.
	 */
	public Process getProcess(String psCode) {
		return processMap.get(psCode);
	}

	/**
	 * This method returns a {@link Workflow} given its name.
	 * If the workflow does not exists <code>null</code>} is returned.
	 * 
	 * @param wfName - The name of the workflow inside the manager.
	 * @return A {@link Workflow} object if the name exists, <code>null</code> otherwise.
	 */
	public Workflow getWorkflow(String wfName) {
		return workflowMap.get(wfName);
	}
	
	public String createNewProcess(String wfName) throws UnknownWorkflow {
		Workflow wf = workflowMap.get(wfName);
		if(wf == null) {
			String message = "Impossible to create a new process! The specified workflow \""+wfName+"\" does not exists!";
			
			ErrorMessage faultInfo = objFactory.createErrorMessage();
			faultInfo.setMessage(message);
			throw new UnknownWorkflow(message, faultInfo);
		}
		
		// creating a new process
		Process newProcess = objFactory.createProcess();
	
		newProcess.setStarted( createXMLGregCalendar() );
		newProcess.setWorkflow(wfName);
		
		// creating the actionStatus elements from the Workflow's actions
		List<ActionStatusType> actionStatusList = new LinkedList<ActionStatusType>();
		for(ActionType action : wf.getAction()) {
			if( action.isAutomInst() ) {
				ActionStatusType actionStatus = objFactory.createActionStatusType();
				
				actionStatus.setAction(action);
				actionStatus.setActor(null);			//not yet taken
				actionStatus.setTakenInCharge(false);	//not yet taken
				actionStatus.setTerminated(false);	//not yet taken so not yet finished
				
				actionStatusList.add(actionStatus);
			}
		}
		// adding the actionStatus elements to the new process
		newProcess.getActionStatus().addAll(actionStatusList);
		
		// each process must have a unique code
		String myProcCode;
		synchronized (this) {
			myProcCode = "p"+intCode;	//I create a local copy of the code
			intCode++;					//I increment the code for the next process
		}
		newProcess.setCode(myProcCode);
		processMap.put(myProcCode, newProcess);
		
		return myProcCode;
	}

	public boolean takeOverAction(String psCode, String actionName, Actor actor)
			throws UnknownCode, WrongAction, WrongActor {
		
		boolean taken = false;
		
		Process p = processMap.get(psCode);
		if(p==null) {
			String message = "The select process <"+psCode+"> does not exists!";
			
			ErrorMessage faultInfo = objFactory.createErrorMessage();
			faultInfo.setMessage(message);
			throw new UnknownCode(message, faultInfo); 
		}
		if(existsAction(actionName, p.getActionStatus()) == false) {
			String message = "The select action <"+actionName
					+"> does not exists in the process <"+psCode+">!";
			
			ErrorMessage faultInfo = objFactory.createErrorMessage();
			faultInfo.setMessage(message);
			throw new WrongAction(message, faultInfo); 
		}
		
		for( ActionStatusType actionStatus : p.getActionStatus() ){
			if(actionStatus.isTakenInCharge() == false) {
				Object o = actionStatus.getAction();
				if(o instanceof ActionType) {
					ActionType azione = (ActionType) o;
					
					if( azione.getName().equals(actionName) ){
							// if it's not the right actor for this action, exception is thrown
						if(!actor.getRole().equals(azione.getRole())) {
							String message = "The select actor <"+actor.getName()+"> does not has the right role!\n"
									+azione.getRole()+"is expected but actor is a "+actor.getRole();
							
							ErrorMessage faultInfo = objFactory.createErrorMessage();
							faultInfo.setMessage(message);
							throw new WrongActor(message, faultInfo); 
						}
							// I try to take the action
						synchronized (actionStatus) {
							if(actionStatus.isTakenInCharge() == false) {
								actionStatus.setActor(actor.getName());
								actionStatus.setTakenInCharge(true);
								
								taken = true;
							}
						}	// if I take an action I stop the method
						if(taken == true) return true;
					}
				} else {
					System.err.println("Server Error! The element is not an action type!");
				}
			}
		}
		
		return false;
	}

	public boolean completeAction(String actionStatusName, String nextActionName) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * This method create a {@link XMLGregorianCalendar} instance with the current timestamp.
	 * 
	 * @return A {@link XMLGregorianCalendar} instance
	 */
	private XMLGregorianCalendar createXMLGregCalendar() {
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
	private XMLGregorianCalendar createXMLGregCalendar(Calendar calendar) {
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

	
	private boolean existsAction(String actionName, List<ActionStatusType> actionStatus) {
		for(ActionStatusType as : actionStatus) {
			Object o = as.getAction();
			if(o instanceof ActionType) {
				ActionType action = (ActionType) o;
				if( action.getName().equals(actionName) )
					return true;
			}
		}
		return false;
	}

	/**
	 * Construct a WorkflowDataManager starting from a {@link WorkflowMonitor} class.
	 * 
	 * @param wfMonitor - a {@link WorkflowMonitor}
	 */
	public WorkflowDataManager(WorkflowMonitor wfMonitor) {
		log.entering(log.getName(), "Constructor");
		objFactory = new ObjectFactory();
		
		workflowMap = new ConcurrentHashMap<String, Workflow>();
		processMap = new ConcurrentHashMap<String, Process>();
		
		workflowNames = new CopyOnWriteArrayList<>();
		
		for( WorkflowReader wfr : wfMonitor.getWorkflows() ) {
			Workflow wf = buildWorkflow(wfr);
			
			String wfName = wf.getName();
			workflowMap.put(wfName, wf);
			workflowNames.add(wfName);
		}
		lastWorkflowUpdate = createXMLGregCalendar();
		
		for( ProcessReader psr : wfMonitor.getProcesses() ) {
			String pCode = "p"+intCode;
			
			Process ps = buildProcess(pCode, psr);
			processMap.put(pCode, ps);
			
			intCode++;	//we are in the constructor, intCode is not yet shared
		}
		lastProcessUpdate = createXMLGregCalendar();
		
		log.exiting(log.getName(), "Constructor");
	}

	/**
	 * This method is used by the constructor and create a {@link Workflow} starting from a {@link WorkflowReader} interface.
	 * @param wfr
	 * @return
	 */
	private Workflow buildWorkflow(WorkflowReader wfr) {
		String wfName = wfr.getName();

		// - Creating the workflow object and set its attribute - //
		Workflow wf = objFactory.createWorkflow();
		wf.setName(wfName);
		
		Map<String, ActionType> newActions = new HashMap<String, ActionType>();
		// - building all the actions - //
		for( ActionReader ar : wfr.getActions() ) {
			ActionType newAct = buildAction(wfName, ar, newActions);
			newActions.put(newAct.getName(), newAct);
		}
		// - linking all the actions - //
		for( ActionReader ar : wfr.getActions()) {
			if(ar instanceof SimpleActionReader) {
				linkSimpleAction( (SimpleActionReader)ar, newActions );
			}
		}
		
		if( !newActions.isEmpty() )
			wf.getAction().addAll(newActions.values());
		else
			System.out.println("\nDEBUG [WfDataManager - buildWorkflow()]: The workflow "+wfName+" does not have actions!\n");
		
		return wf;
	}
	
	/** 
	 * This method is used by buildWorkflow (that is called by the constructor)
	 * and create an {@link ActionType} starting from a {@link ActionReader} interface.
	 *
	 * @param wfName
	 * @param ar
	 * @param createdActions
	 * @return
	 */
	private ActionType buildAction(String wfName, ActionReader ar, Map<String, ActionType> createdActions) {
		String actName = ar.getName();
		String id = wfName+"_"+actName;
		
		// - Creating the action object - //
		ActionType action = objFactory.createActionType();
		
		action.setId(id);
		action.setName(actName);
		action.setRole(ar.getRole());
		action.setAutomInst(ar.isAutomaticallyInstantiated());
		
		if (ar instanceof ProcessActionReader) {
			// - Casting the action to the right type - //
			ProcessActionReader par = (ProcessActionReader) ar;
			
			// - Creating the ProcessActionReader - //
			ActionType.ProcessAction processAction = objFactory.createActionTypeProcessAction();
			processAction.setNextProcess(par.getActionWorkflow().getName());
			
			// - Setting simpleAction & processAction inside the element - //
			action.setSimpleAction(null);
			action.setProcessAction(processAction);
		}
		else if (ar instanceof SimpleActionReader == false)
			System.err.println("Error! The ActionReader "+ar.getName()+" belongs to a not known type! \n");
	
		return action;
	}

	/**
	 * This method is used by buildWorkflow (that is called by the constructor)
	 * and link to every {@link SimpleAction} the next possible actions.
	 * 
	 * @param actReader
	 * @param createdActions
	 */
	private void linkSimpleAction(SimpleActionReader actReader, Map<String, ActionType> createdActions) {
		ActionType actType = createdActions.get(actReader.getName());
		
		// - Creating the SimpleActionReader - //
		ActionType.SimpleAction simpleAction = objFactory.createActionTypeSimpleAction();
		
		// - Save all the nextActions inside the list - //
		for(ActionReader possibleAction : actReader.getPossibleNextActions()) {
			ActionType azioneSuccessiva = createdActions.get(possibleAction.getName());
			if(azioneSuccessiva != null)
				simpleAction.getNextActions().add(azioneSuccessiva);
			else
				System.err.println("Error! Situazione inaspettata! Non esiste l'azione: "+possibleAction.getName());
		}
		
		// - Setting simpleAction & processAction inside the element - //
		actType.setSimpleAction(simpleAction);
		actType.setProcessAction(null);
		
		createdActions.put(actReader.getName(), actType);
	}

	/**
	 * This method is used by the constructor and create a {@link Process} starting from a {@link ProcessReader} interface.
	 * @param psr
	 * @param wfr 
	 * @return
	 */
	private Process buildProcess(String code, ProcessReader psr) {
		// creating a process
		Process process = objFactory.createProcess();
		
		// Taking the relative workflows name //
		String wfName = psr.getWorkflow().getName();
		
		// setting process attributes
		process.setCode(code);
		process.setWorkflow(wfName);
		
		// - Generating and setting the XMLGregorianCalendar - //
		XMLGregorianCalendar startTime = createXMLGregCalendar(psr.getStartTime());
		process.setStarted(startTime);
		
		// - preparing data for the creation of the actions - //
		Workflow wf = workflowMap.get(wfName);
		Map<String, ActionType> wfActionsTypeMap = Utility.buildWFActionsMap(wf.getAction());
		
		List<ActionStatusType> newActions = new LinkedList<ActionStatusType>();
		// - For each process taking the inner actions - //
		for ( ActionStatusReader asr : psr.getStatus() ) {
			
			ActionType actType = wfActionsTypeMap.get(asr.getActionName());
			ActionStatusType as = buildActionStatus(asr, actType);
			newActions.add(as);
		}
		
		if( !newActions.isEmpty() )
			process.getActionStatus().addAll(newActions);
		else
			System.out.println("\nDEBUG [WFInfoSerializer - createProcesses()]: The process "+process.getCode()+" does not have actions!\n");
		
		return process;
	}

	/**
	 * This method is used by buildProcess (that is called by the constructor)
	 * to create a {@link ActionStatusType} starting from a {@link ActionStatusReader} interface.
	 * @param asr
	 * @param actType
	 * @return
	 */
	private ActionStatusType buildActionStatus(ActionStatusReader asr, ActionType actType) {
		ActionStatusType action = objFactory.createActionStatusType();
		
		action.setAction(actType);
		action.setTakenInCharge(asr.isTakenInCharge());
		action.setTerminated(asr.isTerminated());
		
		if (asr.isTakenInCharge()) {		//was the action assigned?
			String actor = asr.getActor().getName();
			action.setActor(actor);
		}

		if (asr.isTerminated())	{		//was the action completed?
			// - Generating a new XMLGregorianCalendar - //
			XMLGregorianCalendar endTime = createXMLGregCalendar( asr.getTerminationTime() );
			action.setTimestamp(endTime);
		}
		
		return action;
	}

}