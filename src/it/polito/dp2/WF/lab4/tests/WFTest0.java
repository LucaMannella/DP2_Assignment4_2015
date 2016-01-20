package it.polito.dp2.WF.lab4.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import it.polito.dp2.WF.*;
import it.polito.dp2.WF.lab4.ServiceUnavailableException;
import it.polito.dp2.WF.lab4.WFTakeOverClient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class WFTest0 {
	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    
    class ActionReaderComparator implements Comparator<ActionReader> {
        public int compare(ActionReader f0, ActionReader f1) {
        	return f0.getName().compareTo(f1.getName());
        }
    }
    
    class WorkflowReaderComparator implements Comparator<WorkflowReader> {
        public int compare(WorkflowReader f0, WorkflowReader f1) {
        	return f0.getName().compareTo(f1.getName());
        }
    }
    
    class ProcessReaderComparator implements Comparator<ProcessReader> {
        public int compare(ProcessReader f0, ProcessReader f1) {
        	return f0.getStartTime().compareTo(f1.getStartTime());
        }
    }

    
	private static WorkflowMonitor referenceWorkflowMonitor;	// reference data generator
	private static WorkflowMonitor testWorkflowMonitor; 		// implementation under test
	private static WFTakeOverClient clientUnderTest;			// client3 under test
	private static String clientUnderTestClass = "it.polito.dp2.WF.sol4.client3.WFTakeOverClientImpl";

	// values passed in the test to the take over operation
	private static String workflowName=null;
	private static String actionName=null;
	private static final String actorName="Pippo";
	private static int numExec=0;							// number of times client3 is executed
	
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	System.out.println("Preparing the tests.");
    	// Create reference data generator
        System.setProperty("it.polito.dp2.WF.WorkflowMonitorFactory", "it.polito.dp2.WF.Random.WorkflowMonitorFactoryImpl");

        referenceWorkflowMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();

                
        // Create client3 under test
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
		if (loader == null) {
			loader = WFTest0.class.getClassLoader();
		}
		try{
			Class<?> t = null;
			t = (loader != null) ? loader.loadClass(clientUnderTestClass) : 
				 Class.forName(clientUnderTestClass);
	        clientUnderTest = (WFTakeOverClient) t.newInstance();
	        assertNotNull("The solution client3 cannot be instantiated", clientUnderTest);			
		} 
		catch (ClassNotFoundException ex)
		{
			fail("The class "+clientUnderTestClass+" cannot be found! Check your solution and the name of your class!");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			fail("An exception has occurred when initializing the test. Signal this problem to the teacher");
		}
        
		/*
        // read testcase property
        Long testcaseObj = Long.getLong("it.polito.dp2.WF.Random.testcase");
        if (testcaseObj == null)
        	testcase = 0;
        else
        	testcase = testcaseObj.longValue();
        
        // read number of processes created before starting the test
        Long numprocObj = Long.getLong("it.polito.dp2.WF.lab4.test.numproc");
        if (numprocObj == null)
        	numproc = 0;
        else
        	numproc = numprocObj.longValue();
        
        
        workflowName = System.getProperty("it.polito.dp2.WF.lab4.test.workflowName");
        if (workflowName == null) {
        	System.out.println("No workflow specified: using ArticleProduction");
        	workflowName = "ArticleProduction";
        }
        
        // read action name
        actionName = System.getProperty("it.polito.dp2.WF.lab4.test.actionName");
        if (actionName == null) {
        	System.out.println("No action specified: using Writing");
        	actionName = "Writing";
        }
        */
        
        // run client3
        if (!runClient3())
        	System.out.println("Warning: Failure when running client 3!");
        else
        	System.out.println("Test preparation finished successfully!");
        
        // Create client1 implementation under test
        System.setProperty("it.polito.dp2.WF.WorkflowMonitorFactory", "it.polito.dp2.WF.sol4.client1.WorkflowMonitorFactory");

        testWorkflowMonitor = WorkflowMonitorFactory.newInstance().newWorkflowMonitor();

        
    }

    // looks for an action that can be taken over 
    // and runs client 3 with matching parameters one or more times
    private static boolean runClient3() {
		Set<WorkflowReader> wfReaders = referenceWorkflowMonitor.getWorkflows();
		Set<ProcessReader> processes=null;
		for (WorkflowReader wfr:wfReaders) {
			Set<ProcessReader> procReaders = wfr.getProcesses();
			/*
			Set<ActionReader> actReaders = wfr.getActions();
			for (ActionReader act:actReaders)
				act.getRole();
			*/
			for (ProcessReader proc:procReaders) {
				List<ActionStatusReader> status = proc.getStatus();
				// look for a process action that has not yet been taken over
				for (ActionStatusReader astat:status) {
					if (!astat.isTakenInCharge()) {
						actionName = astat.getActionName();
						break;
					}
				}
				// if action found we have finished
				if (actionName!=null)
					break;
			}
			// if action found get the corresponding workflow name and set of processes
			if (actionName != null) {
				workflowName = wfr.getName();
				processes =wfr.getProcesses();
				break;
			}
		}
		if (workflowName == null || actionName == null) {
			System.out.println("Warning: no action to take over. Choose another seed.");
		    return false;
		} else {
			try {
				// count number of right actions that can be taken over in the found workflow
				numExec = countRightActions(processes);
				System.out.println("Number of actions to take over: "+numExec);
				// take over all those actions; return false if not all of them are taken over successfully
				for (int i=0; i<numExec; i++) {
					System.out.println("Trying to take over action...");
					if(!clientUnderTest.takeOver(workflowName, actionName, actorName)) {
						System.out.println("...failed.");
						return false;
					}
					System.out.println("...succeeded.");
				}
				return true;
			} catch (ServiceUnavailableException e) {
				return false;
			}
		}
	}

	@Before
    public void setUp() throws Exception {
        assertNotNull("Internal tester error during test setup: null reference", referenceWorkflowMonitor);
        assertNotNull("Could not run tests: the implementation under test generated a null WorkflowMonitor", testWorkflowMonitor);
    }

        
	// This test checks all the expected actions have been taken over successfully during test preparation
	// (see operations performed in setUpBeforeClass) and this results from the data read from the server.
    @Test
    public final void testRightActionTaken() {
		System.out.println("Running test testRightActionTaken");
		// get workflow readers
		WorkflowReader rwfr = referenceWorkflowMonitor.getWorkflow(workflowName);
		WorkflowReader twfr = testWorkflowMonitor.getWorkflow(workflowName);
		assertNotNull("NULL workflow reader for workflow "+workflowName, twfr);
		
		// get processes
		Set<ProcessReader> rs = rwfr.getProcesses();
		Set<ProcessReader> ts = twfr.getProcesses();
		
		// if one of the two calls returned null while the other didn't return null, the test fails
        if ((rs == null) && (ts != null) || (rs != null) && (ts == null)) {
            fail("getProcesses returns null when it should return non-null or vice versa");
            return;
        }

        // if both calls returned null, there are no processes, and the test passes
        if ((rs == null) && (ts == null)) {
            assertTrue("Warning: there are no Processes!", true);
            return;
        }
        
        // count the number of right actions that remain to be taken over in reference processes
        // and in processes under test
        int rNumAct = countRightActions(rs);
        int tNumAct = countRightActions(ts);
        System.out.println("Right actions in reference processes: "+rNumAct);
        System.out.println("Right actions in test processes: "+tNumAct);
        System.out.println("Number of actions to be taken over: "+numExec);
        
        // and check they correspond
        assertEquals(rNumAct,tNumAct+numExec);
	}

	// Counts the number of right actions that can be taken over in the specified set of processes
	// By right action we mean one that has name equal to actionName
	private static int countRightActions(Set<ProcessReader> pset) {
    	int count=0;
    	
		for (ProcessReader proc:pset) {
			List<ActionStatusReader> status = proc.getStatus();
			for (ActionStatusReader astat:status) {
				if (astat!=null && actionName.equals(astat.getActionName()) && !astat.isTakenInCharge()) {
					count++;
				}
			}
		}

    	return count;
	}
	
	// This test checks the take over operation fails for non matching parameters
	@Test
    public final void testNonMatchingParameters() {
		boolean retval;
		
		System.out.println("Running test testNonMatchingParameters");

		try {
			retval = clientUnderTest.takeOver("abc", "abc", actorName);
			assertTrue("Operation did not fail as expected", !retval);
		} catch (ServiceUnavailableException e) {
			fail("Warning: operation failed raising unexpected exception");
		}
	}
	
	// This test checks the take over operation fails if there is no action that can be taken over
	@Test
	public final void testAlreadyTakenOver() {
		boolean retval;
		
		System.out.println("Running test testAlreadyTakenOver");

		try {
			retval = clientUnderTest.takeOver(workflowName, actionName, actorName);
			assertTrue("Operation did not fail as expected", !retval);
		} catch (ServiceUnavailableException e) {
			fail("Warning: operation failed raising unexpected exception");
		}
	}

}
