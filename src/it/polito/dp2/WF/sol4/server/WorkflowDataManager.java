package it.polito.dp2.WF.sol4.server;

import java.util.GregorianCalendar;
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
import it.polito.dp2.WF.sol4.gen.ObjectFactory;
import it.polito.dp2.WF.sol4.gen.Process;
import it.polito.dp2.WF.sol4.gen.UnknownNames_Exception;
import it.polito.dp2.WF.sol4.gen.Workflow;
import it.polito.dp2.WF.sol4.util.Utility;

/**
 * This class must be thread safe!
 * 
 * @author Luca
 */
public class WorkflowDataManager {
	
	private static Logger log = Logger.getLogger(WorkflowDataManager.class.getName());
	
	private Map<String, Workflow> workflowMap = null;
	private Map<String, Process> processMap = null;
	private int pCode = 0;
	
	private List<String> workflowNames;
	
	private GregorianCalendar lastWorkflowUpdate;
	private GregorianCalendar lastProcessUpdate;

	private ObjectFactory objFactory;

	public WorkflowDataManager(WorkflowMonitor wfMonitor) {	// TODO Auto-generated constructor stub
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
		lastWorkflowUpdate = new GregorianCalendar();
		
		for( ProcessReader psr : wfMonitor.getProcesses() ) {
			Process ps = buildProcess(psr);
			processMap.put("p"+pCode, ps);
			pCode++;
		}
		lastProcessUpdate = new GregorianCalendar();
		
		objFactory = null;
	}

	public List<String> getWorkflowNames() {		// TODO Auto-generated method stub
		return this.workflowNames;
	}

	public GregorianCalendar getLastWorkflowsUpdate() {		// TODO Auto-generated method stub
		return this.lastWorkflowUpdate;
	}

	public List<Workflow> getWorkflows(List<String> wfNames) throws UnknownNames_Exception{
		// TODO Auto-generated method stub
		return null;
	}

	public GregorianCalendar getLastProcessesUpdate() {
		return this.lastProcessUpdate;
	}

	public List<Process> getProcesses(List<String> wfNames) {
		// TODO Auto-generated method stub
		return null;
	}

	public Process getProcess(String psCode) {
		// TODO Auto-generated method stub
		return null;
	}

	public Workflow getWorkflow(String wfName) {
		// TODO Auto-generated method stub
		return null;
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
		
		Map<String, ActionType> newActions = new ConcurrentHashMap<String, ActionType>();
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
	private Process buildProcess(ProcessReader psr) {
		
		// - Generating XMLGregorianCalendar - //
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(psr.getStartTime().getTime());
		XMLGregorianCalendar startTime = null;
		try {
			startTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
		} catch (DatatypeConfigurationException e) {
			System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
			System.err.println(e.getMessage());
			e.printStackTrace();
			//startTime = new XMLGregorianCalendarImpl(cal);
		}
		
		// - Taking the relative workflows name - //
		String wfName = psr.getWorkflow().getName();
		
		// creating a process
		Process process = objFactory.createProcess();
		// setting its attributes
		process.setCode("p"+pCode);
		process.setStarted(startTime);
		process.setWorkflow(wfName);
		
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
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(asr.getTerminationTime().getTime());
			XMLGregorianCalendar endTime = null;
			try {
				endTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
			} catch (DatatypeConfigurationException e) {
				System.err.println("Error! There is a problem with the instantiation of the DatatypeFactory");
				System.err.println(e.getMessage());
				e.printStackTrace();
				//endTime = new XMLGregorianCalendarImpl(cal);
			}
			
			action.setTimestamp(endTime);
		}
		
		return action;
	}

}
